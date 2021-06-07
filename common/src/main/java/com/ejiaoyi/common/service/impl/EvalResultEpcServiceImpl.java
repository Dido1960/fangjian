package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.dto.BidderResultDTO;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.Enabled;
import com.ejiaoyi.common.enums.EvalProcess;
import com.ejiaoyi.common.enums.ReviewType;
import com.ejiaoyi.common.mapper.EvalResultEpcMapper;
import com.ejiaoyi.common.mapper.EvalResultJlMapper;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * epc排名结果 实现
 *
 * @author liuguoqiang
 * @since 2020-11-21 17:28
 */
@Service
public class EvalResultEpcServiceImpl extends BaseServiceImpl implements IEvalResultEpcService {

    @Autowired
    private EvalResultEpcMapper evalResultEpcMapper;
    @Autowired
    private IBidderService bidderService;
    @Autowired
    private IBidderReviewResultScoreService bidderReviewResultScoreService;
    @Autowired
    private ITenderDocService tenderDocService;
    @Autowired
    private IExpertReviewSingleItemScoreService expertReviewSingleItemScoreService;
    @Autowired
    private IExpertUserService expertUserService;
    @Autowired
    private IGradeService gradeService;
    @Autowired
    private IBidSectionService bidSectionService;
    @Autowired
    private IQuoteScoreResultService quoteScoreResultService;
    @Autowired
    private IQuoteScoreResultAppendixService quoteScoreResultAppendixService;

    @Override
    public List<EvalResultEpc> listRankingBidderByBsId(Integer bidSectionId) {
        return evalResultEpcMapper.listRankingBidderByBsId(bidSectionId);
    }

    @Override
    public List<EvalResultEpc> addResultByBsId(Integer bidSectionId) {
        //清除原有的当前标段的数据
        deleteByBsId(bidSectionId);
        //获取通过初步评审的投标人
        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        gradeIds = gradeService.getGradeIds(gradeIds, EvalProcess.DETAILED.getCode());
        List<EvalResultEpc> list = new ArrayList<>();
        for (Bidder bidder : bidders) {
            EvalResultEpc evalResultEpc = EvalResultEpc.builder()
                    .bidderId(bidder.getId())
                    .bidSectionId(bidSectionId)
                    .bidPrice(bidder.getBidderOpenInfo().getBidPrice())
                    .build();
            //详细评审得分
            List<BidderResultDTO> bidderResultDTOS = expertReviewSingleItemScoreService.getAvgScoreBySth(gradeIds, null, bidSectionId, bidder.getId(), null);
            String detailedScore = bidderResultDTOS.get(0).getArithmeticScore();
            evalResultEpc.setDetailedScore(new BigDecimal(detailedScore).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            //报价得分
            String priceScore;
            if (Enabled.YES.getCode().equals(bidSection.getUpdateScoreStatus())){
                priceScore = quoteScoreResultAppendixService.getQuoteScoreResultAppendix(bidder.getId()).getBidPriceScore();
            }else {
                priceScore =quoteScoreResultService.getQuoteScoreResultByBidderId(bidder.getId()).getBidPriceScore();
            }
            evalResultEpc.setBusinessScore(new BigDecimal(priceScore).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            //计算总分
            double total = Double.parseDouble(detailedScore)
                    + Double.parseDouble(priceScore);
            evalResultEpc.setTotalScore(new BigDecimal(total).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            list.add(evalResultEpc);
        }
        //对list进行排序
        list.sort((r1, r2) -> {
            double diff = Double.parseDouble(r2.getTotalScore()) - Double.parseDouble(r1.getTotalScore());
            if (diff > 0) {
                return 1;
            } else if (diff < 0) {
                return -1;
            } else {
                double technical = Double.parseDouble(r2.getBidPrice()) - Double.parseDouble(r1.getBidPrice());
                if (technical > 0) {
                    return -1;
                } else if (technical < 0) {
                    return 1;
                } else {
                    double bidderId = r2.getBidderId() - r1.getBidderId();
                    if (bidderId > 0){
                        return -1;
                    }else if (technical < 0) {
                        return 1;
                    }else {
                        return 0;
                    }
                }
            }
        });

        int orderNo = 0;
        for (EvalResultEpc evalResultEpc : list) {
            orderNo++;
            evalResultEpc.setId(null);
            evalResultEpc.setOrderNo(orderNo);
            evalResultEpcMapper.insert(evalResultEpc);
        }

        return listRankingBidderByBsId(bidSectionId);
    }

    @Override
    public Integer deleteByBsId(Integer bidSectionId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null");
        QueryWrapper<EvalResultEpc> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", bidSectionId);
        return evalResultEpcMapper.delete(queryWrapper);
    }

    @Override
    public EvalResultEpc getEvalResultEpc(Integer bidSectionId, Integer bidderId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null");
        Assert.notNull(bidSectionId, "param bidderId can not be null");
        QueryWrapper<EvalResultEpc> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", bidSectionId);
        queryWrapper.eq("BIDDER_ID", bidderId);
        return evalResultEpcMapper.selectOne(queryWrapper);
    }
}
