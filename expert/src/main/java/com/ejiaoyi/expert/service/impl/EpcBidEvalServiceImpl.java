package com.ejiaoyi.expert.service.impl;

import com.ejiaoyi.common.annotation.RedissonLock;
import com.ejiaoyi.common.dto.ArithmeticResult;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.Enabled;
import com.ejiaoyi.common.enums.EvalProcess;
import com.ejiaoyi.common.enums.ExecuteCode;
import com.ejiaoyi.common.enums.TimeFormatter;
import com.ejiaoyi.common.exception.CustomException;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.service.impl.BaseServiceImpl;
import com.ejiaoyi.common.util.CalcQuoteScoreUtil;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.DateTimeUtil;
import com.ejiaoyi.common.util.ThreadUtlis;
import com.ejiaoyi.expert.enums.ExpertUserMsgStatus;
import com.ejiaoyi.expert.service.IBidEvalService;
import com.ejiaoyi.expert.service.IEpcBidEvalService;
import com.ejiaoyi.expert.service.IExpertService;
import com.ejiaoyi.expert.support.AuthUser;
import com.ejiaoyi.expert.support.CurrentUserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 施工总承包专家评标 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-12-02
 */
@Service
public class EpcBidEvalServiceImpl extends BaseServiceImpl implements IEpcBidEvalService {

    @Autowired
    private ICalcScoreParamService calcScoreParamService;

    @Autowired
    private IBidSectionService bidSectionService;

    @Autowired
    private IBidEvalService bidEvalService;

    @Autowired
    private IQuoteScoreResultService quoteScoreResultService;

    @Autowired
    private IGradeService gradeService;

    @Autowired
    private ITenderDocService tenderDocService;

    @Autowired
    private IExpertReviewService expertReviewService;

    @Autowired
    private IQuoteScoreResultAppendixService quoteScoreResultAppendixService;

    @Autowired
    private IEvalResultEpcService evalResultEpcService;
    @Autowired
    private IExpertService expertService;
    @Autowired
    private IExpertUserService expertUserService;

    @Override
    public boolean addQuotationScore(Integer bidSectionId) throws CustomException {
        CalcScoreParam calcScoreParam = calcScoreParamService.getCalcScoreParamBySectionId(bidSectionId);
        String gtBasePriceDeduction = calcScoreParam.getGtBasePriceDeduction();
        String ltBasePriceDeduction = calcScoreParam.getLtBasePriceDeduction();
        String totalScore = calcScoreParam.getTotalScore();
        List<Bidder> bidderList = bidEvalService.listBidderPassPreliminary(bidSectionId);
        List<String> bidPrices = new ArrayList<>(bidderList.size());
        try {
            for (Bidder bidder : bidderList) {
                BidderOpenInfo bidderOpenInfo = bidder.getBidderOpenInfo();
                String calcBidPrice = bidderOpenInfo.getBidPrice();
                bidPrices.add(calcBidPrice);
            }

            // 评标基准价平均值计算方式
            ArithmeticResult arithmeticResult = CalcQuoteScoreUtil.calcQuoteScoreOne(bidPrices, totalScore, gtBasePriceDeduction, ltBasePriceDeduction);

            if (ExecuteCode.SUCCESS.getCode().toString().equals(arithmeticResult.getStatus().getCode())) {
                CalcScoreParam scoreParam = CalcScoreParam.builder()
                        .id(calcScoreParam.getId())
                        .basePrice(arithmeticResult.getBasePrice())
                        .build();
                calcScoreParamService.updateCalcScoreParam(scoreParam);
                for (int i = 0; i < bidderList.size(); i++) {
                    Bidder bidder = bidderList.get(i);
                    String scoreStr = arithmeticResult.getBidPriceSroces().get(i);
                    BigDecimal bigDecimal = new BigDecimal(scoreStr);
                    String bidPriceScore = scoreStr;
                    // 如果得分为负数，代表报价得分扣除得已经大于总分分值了，则该项得分为0分
                    if (bigDecimal.compareTo(new BigDecimal(0)) < 0) {
                        bidPriceScore = "0";
                    }
                    QuoteScoreResult quoteScoreResult = QuoteScoreResult.builder()
                            .bidSectionId(bidSectionId)
                            .bidderId(bidder.getId())
                            .bidPrice(arithmeticResult.getBidPrices().get(i))
                            .bidPriceOffset(arithmeticResult.getBidPriceOffsets().get(i))
                            .bidPriceScore(bidPriceScore)
                            .build();
                    quoteScoreResultService.addOrUpdateQuoteScore(quoteScoreResult);
                }
            }

            BidSection bidSection = BidSection.builder()
                    .id(bidSectionId)
                    .priceRecordStatus(1)
                    .build();
            return bidSectionService.updateBidSectionById(bidSection) == 1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    @RedissonLock(key = "'price_'+#bidSectionId+'_'+#expertId")
    public ExpertReview initExpertPriceReview(Integer bidSectionId, Integer expertId, List<Grade> grades, List<Bidder> bidders) {
        ExpertReview query = ExpertReview.builder()
                .gradeId(grades.get(0).getId())
                .bidSectionId(bidSectionId)
                .expertId(expertId)
                .build();
        ExpertReview review = expertReviewService.getExpertReview(query);
        if (!CommonUtil.isEmpty(review)) {
            return review;
        }

        for (Grade grade : grades) {
            ExpertReview expertReview = ExpertReview.builder()
                    .gradeId(grade.getId())
                    .initStatus(1)
                    .bidSectionId(bidSectionId)
                    .expertId(expertId)
                    .startTime(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS))
                    .build();
            expertReviewService.insertExpertReview(expertReview);
        }
        return expertReviewService.getExpertReview(query);
    }

    @Override
    public Boolean epcPriceGroupEnd(Integer bidSectionId) {
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        List<Grade> grades = gradeService.listGrade(tenderDoc.getGradeId().split(","), EvalProcess.CALC_PRICE_SCORE.getCode());
        int num = 0;
        for (Grade grade : grades) {
            Grade build = Grade.builder()
                    .id(grade.getId())
                    .groupEnd(Enabled.YES.getCode())
                    .build();
            if (!gradeService.updateGrade(build)) {
                ++num;
            }
        }
        //正常结束后对排名数据进行存储
        ThreadUtlis.run(() -> {
            evalResultEpcService.addResultByBsId(bidSectionId);
        });
        return num == 0;
    }

    @Override
    public List<QuoteScoreResultAppendix> initQuoteScoreResultAppendix(Integer bidSectionId) {
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        // 筛选参加报价分计算的投标人
        List<QuoteScoreResultAppendix> quoteScoreResultAppendices = new ArrayList<>();
        List<Bidder> bidders;
        if (!Enabled.YES.getCode().equals(bidSection.getPriceRecordStatus())) {
            bidders = bidEvalService.listBidderPassPreliminary(bidSectionId);
            for (Bidder bidder : bidders) {
                QuoteScoreResultAppendix appendix = quoteScoreResultAppendixService.getQuoteScoreResultAppendix(bidder.getId());
                if (appendix == null) {
                    QuoteScoreResultAppendix quoteScoreResultAppendix = QuoteScoreResultAppendix.builder()
                            .bidSectionId(bidSectionId)
                            .bidderId(bidder.getId())
                            .bidPrice(bidder.getBidderOpenInfo().getBidPrice())
                            .build();
                    quoteScoreResultAppendixService.save(quoteScoreResultAppendix);
                    quoteScoreResultAppendix.setBidderName(bidder.getBidderName());
                    quoteScoreResultAppendices.add(quoteScoreResultAppendix);
                } else {
                    appendix.setBidderName(bidder.getBidderName());
                    quoteScoreResultAppendices.add(appendix);
                }
            }
        } else {
            bidders = bidEvalService.listBidderQuoteScore(bidSectionId);
            for (Bidder bidder : bidders) {
                QuoteScoreResultAppendix appendix = quoteScoreResultAppendixService.getQuoteScoreResultAppendix(bidder.getId());
                if (appendix == null) {
                    QuoteScoreResultAppendix quoteScoreResultAppendix = QuoteScoreResultAppendix.builder()
                            .bidSectionId(bidSectionId)
                            .bidderId(bidder.getId())
                            .bidPrice(bidder.getQuoteScoreResult().getBidPrice())
                            .bidPriceOffset(bidder.getQuoteScoreResult().getBidPriceOffset())
                            .bidPriceScore(bidder.getQuoteScoreResult().getBidPriceScore())
                            .build();
                    quoteScoreResultAppendixService.save(quoteScoreResultAppendix);
                    quoteScoreResultAppendix.setBidderName(bidder.getBidderName());
                    quoteScoreResultAppendices.add(quoteScoreResultAppendix);
                } else {
                    appendix.setBidderName(bidder.getBidderName());
                    quoteScoreResultAppendices.add(appendix);
                }
            }
        }
        return quoteScoreResultAppendices;
    }

    @Override
    public JsonData checkEpcPriceGroupEnd() {
        JsonData result = new JsonData();

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        if (bidEvalService.isFreeBackApplying(bidSectionId)){
            result.setCode("2");
            result.setMsg("评审回退尚未审核，操作失败!");
            return result;
        }

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.CALC_PRICE_SCORE.getCode());

        for (Grade grade : grades) {
            // 检查所有专家是否结束个人评审
            if (!bidEvalService.validAllExpertEval(bidSectionId, grade.getId())) {
                result.setCode("2");
                List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);
                List<ExpertUser> list = expertService.listLinkNoFinishExpertUsers(expertUsers, grade.getId());
                List<String> expertNames = list.stream().map(ExpertUser::getExpertName).collect(Collectors.toList());
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < expertNames.size(); i++) {
                    stringBuilder.append(i+1).append("、").append(expertNames.get(i)).append("<br>");
                }
                result.setMsg("<b>以下专家未结束:</b><br>"+stringBuilder);
                return result;
            }
        }
        bidEvalService.expertUserMsgPush(bidSectionId, EvalProcess.CALC_PRICE_SCORE.getCode(), ExpertUserMsgStatus.GROUP_END);
        result.setCode("1");
        return result;
    }

}
