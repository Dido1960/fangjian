package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.ConDetailedMethod;
import com.ejiaoyi.common.enums.Enabled;
import com.ejiaoyi.common.enums.EvalProcess;
import com.ejiaoyi.common.mapper.EvalResultSgMapper;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 施工排名结果 实现
 *
 * @author liuguoqiang
 * @since 2020-09-30
 */
@Service
public class EvalResultSgServiceImpl extends BaseServiceImpl implements IEvalResultSgService {
    @Autowired
    private EvalResultSgMapper evalResultSgMapper;
    @Autowired
    private IBidderService bidderService;
    @Autowired
    private IBidderReviewResultDeductService bidderReviewResultDeductService;
    @Autowired
    private ITenderDocService tenderDocService;
    @Autowired
    private IGradeService gradeService;
    @Autowired
    private IBidderQuantityScoreService bidderQuantityScoreService;
    @Autowired
    private IExpertReviewMutualService expertReviewMutualService;
    @Autowired
    private IExpertUserService expertUserService;

    @Override
    public List<EvalResultSg> listRankingBidderByBsId(Integer bidSectionId) {
        return evalResultSgMapper.listRankingBidderByBsId(bidSectionId);
    }

    @Override
    public List<EvalResultSg> addResultByBsId(Integer bidSectionId) {
        //清除原有的当前标段的数据
        deleteByBsId(bidSectionId);
        //获取通过初步评审的投标人
        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);
        //获取初步评审对应gradeId
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        List<EvalResultSg> list = new ArrayList<>();
        for (Bidder bidder : bidders) {
            //获取报价分
            BidderQuantityScore bidderQuantityScore = bidderQuantityScoreService.getBidderQuantityScoreByBidderId(bidder.getId());
            double businessScore = bidderQuantityScore.getTotalScore().doubleValue();

            EvalResultSg evalResultSg = EvalResultSg.builder()
                    .bidderId(bidder.getId())
                    .bidSectionId(bidSectionId)
                    .bidPrice(bidder.getBidderOpenInfo().getBidPrice())
                    .businessScore(String.valueOf(businessScore))
                    .build();

            BidderReviewResultDeduct query = BidderReviewResultDeduct.builder()
                    .bidSectionId(bidSectionId)
                    .bidderId(bidder.getId()).build();
            //施工能力扣分
            Grade abilityGrade = gradeService.getGradeByIdsAndName(gradeIds, ConDetailedMethod.CONSTRUCTION_ABILITY);
            query.setGradeId(abilityGrade.getId());
            evalResultSg.setAbility(bidderReviewResultDeductService.getBidderReviewResultDeduct(query).getDeductScore());
            //施工组织设计扣分
            Grade organizeGrade = gradeService.getGradeByIdsAndName(gradeIds, ConDetailedMethod.CONSTRUCTION_DESIGN);
            query.setGradeId(organizeGrade.getId());
            evalResultSg.setOrganize(bidderReviewResultDeductService.getBidderReviewResultDeduct(query).getDeductScore());
            //安全质量事故扣分
            Grade qualityGrade = gradeService.getGradeByIdsAndName(gradeIds, ConDetailedMethod.SAFETY_QUALITY_ACCIDENT);
            query.setGradeId(qualityGrade.getId());
            evalResultSg.setQuality(bidderReviewResultDeductService.getBidderReviewResultDeduct(query).getDeductScore());
            //建筑市场不良记录扣分
            Grade badRecordGrade = gradeService.getGradeByIdsAndName(gradeIds, ConDetailedMethod.BAD_RECORD_MARKET);
            query.setGradeId(badRecordGrade.getId());
            evalResultSg.setBadRecord(bidderReviewResultDeductService.getBidderReviewResultDeduct(query).getDeductScore());

            //判断是否有互保共建
            if (Enabled.YES.getCode().equals(tenderDoc.getMutualSecurityStatus())){
                String[] otherGradeIds = tenderDoc.getOtherGradeId().split(",");
                List<Grade> grades = gradeService.listGrade(otherGradeIds, EvalProcess.OTHER.getCode());
                ExpertUser expertUser = expertUserService.getChairmanByBidSectionId(bidSectionId);
                if (!CommonUtil.isEmpty(grades)){
                    Grade grade = grades.get(0);

                    List<ExpertReviewMutual> resultList = expertReviewMutualService.listExpertReviewMutual(ExpertReviewMutual.builder()
                            .gradeId(grade.getId())
                            .bidderId(bidder.getId())
                            .expertId(expertUser.getId())
                            .build(), false);
                    if (!CommonUtil.isEmpty(resultList)){
                        ExpertReviewMutual expertReviewMutual = resultList.get(0);
                        businessScore += Double.parseDouble(expertReviewMutual.getEvalResult());
                        evalResultSg.setMutualSecurity(expertReviewMutual.getEvalResult());
                    }
                }

            }

            //计算总分
            //扣分计算
            businessScore = new BigDecimal(businessScore).subtract(new BigDecimal(evalResultSg.getAbility()))
                    .subtract(new BigDecimal(evalResultSg.getOrganize()))
                    .subtract(new BigDecimal(evalResultSg.getQuality()))
                    .subtract(new BigDecimal(evalResultSg.getBadRecord())).doubleValue();

            evalResultSg.setTotalScore(new BigDecimal(businessScore).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            list.add(evalResultSg);
        }
        //对list进行排序
        list.sort(new Comparator<EvalResultSg>() {
            @Override
            public int compare(EvalResultSg r1, EvalResultSg r2) {
                double diff = Double.parseDouble(r2.getTotalScore()) - Double.parseDouble(r1.getTotalScore());
                if (diff > 0){
                    return 1;
                }else if (diff < 0){
                    return -1;
                }else {
                    double technical = Double.parseDouble(r2.getBidPrice()) - Double.parseDouble(r1.getBidPrice());
                    if (technical > 0){
                        return -1;
                    }else if (technical < 0){
                        return 1;
                    }else {
                        return 0;
                    }
                }
            }
        });

        int orderNo = 0;
        for (EvalResultSg evalResultSg : list) {
            orderNo ++;
            evalResultSg.setId(null);
            evalResultSg.setOrderNo(orderNo);
            evalResultSgMapper.insert(evalResultSg);
        }

        return listRankingBidderByBsId(bidSectionId);
    }

    @Override
    public Integer deleteByBsId(Integer bidSectionId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null");
        QueryWrapper<EvalResultSg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", bidSectionId);
        return evalResultSgMapper.delete(queryWrapper);
    }

    @Override
    public EvalResultSg getEvalResultSgById(Integer bidSectionId, Integer bidderId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null");
        Assert.notNull(bidderId, "param bidderId can not be null");
        QueryWrapper<EvalResultSg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", bidSectionId);
        queryWrapper.eq("BIDDER_ID", bidderId);
        return evalResultSgMapper.selectOne(queryWrapper);
    }
}
