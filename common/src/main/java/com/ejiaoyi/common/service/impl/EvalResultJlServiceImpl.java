package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.entity.Bidder;
import com.ejiaoyi.common.entity.EvalResultJl;
import com.ejiaoyi.common.entity.ExpertUser;
import com.ejiaoyi.common.entity.TenderDoc;
import com.ejiaoyi.common.enums.EvalProcess;
import com.ejiaoyi.common.enums.ReviewType;
import com.ejiaoyi.common.mapper.EvalResultJlMapper;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 监理排名结果 实现
 * @author liuguoqiang
 * @since 2020-11-21 17:28
 */
@Service
public class EvalResultJlServiceImpl extends BaseServiceImpl implements IEvalResultJlService {

    @Autowired
    private EvalResultJlMapper evalResultJlMapper;
    @Autowired
    private IBidderService bidderService;
    @Autowired
    private ITenderDocService tenderDocService;
    @Autowired
    private IExpertReviewSingleItemScoreService expertReviewSingleItemScoreService;
    @Autowired
    private IExpertReviewSingleItemDeductScoreService expertReviewSingleItemDeductScoreService;
    @Autowired
    private IExpertUserService expertUserService;

    @Override
    public List<EvalResultJl> listRankingBidderByBsId(Integer bidSectionId) {
        return evalResultJlMapper.listRankingBidderByBsId(bidSectionId);
    }

    @Override
    public List<EvalResultJl> addResultByBsId(Integer bidSectionId) {
        //清除原有的当前标段的数据
        deleteByBsId(bidSectionId);
        //获取通过初步评审的投标人
        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);
        List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        List<EvalResultJl> list = new ArrayList<>();
        for (Bidder bidder : bidders) {
            EvalResultJl evalResultJl = EvalResultJl.builder()
                    .bidderId(bidder.getId())
                    .bidSectionId(bidSectionId)
                    .bidPrice(bidder.getBidderOpenInfo().getBidPrice())
                    .build();
            //商务标得分
            String businessScore = expertReviewSingleItemScoreService.getAvgScoreForReview(gradeIds, EvalProcess.DETAILED.getCode(), ReviewType.BUSINESS_STANDARD.getCode(), bidder.getId(), expertUsers.size());
            evalResultJl.setBusinessScore(new BigDecimal(businessScore).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            //技术标得分
            String technicalScore = expertReviewSingleItemScoreService.getAvgScoreForReview(gradeIds, EvalProcess.DETAILED.getCode(), ReviewType.TECHNICAL_STANDARD.getCode(), bidder.getId(), expertUsers.size());
            evalResultJl.setTechnicalScore(new BigDecimal(technicalScore).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            //违章行为扣分
            String deduct =  expertReviewSingleItemDeductScoreService.getAvgDeductScoreForReview(gradeIds, ReviewType.VIOLATION.getCode(), bidder.getId(), expertUsers.size());
            evalResultJl.setViolationDeduct(new BigDecimal(deduct).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            //计算总分
            //扣分计算
            Double total = Double.parseDouble(businessScore)
                    + Double.parseDouble(technicalScore)
                    - Double.parseDouble(deduct);
            evalResultJl.setTotalScore(new BigDecimal(total).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            list.add(evalResultJl);
        }
        //对list进行排序
        list.sort((r1, r2) -> {
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
                    //TODO: 2020-11-21 报价一样的排序方式
                    return 0;
                }
            }
        });

        int orderNo = 0;
        for (EvalResultJl evalResultJl : list) {
            orderNo ++;
            evalResultJl.setId(null);
            evalResultJl.setOrderNo(orderNo);
            evalResultJlMapper.insert(evalResultJl);
        }

        return listRankingBidderByBsId(bidSectionId);
    }

    @Override
    public Integer deleteByBsId(Integer bidSectionId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null");
        QueryWrapper<EvalResultJl> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", bidSectionId);
        return evalResultJlMapper.delete(queryWrapper);
    }

    @Override
    public EvalResultJl getEvalResultJl(Integer bidSectionId, Integer bidderId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null");
        Assert.notNull(bidSectionId, "param bidderId can not be null");
        QueryWrapper<EvalResultJl> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", bidSectionId);
        queryWrapper.eq("BIDDER_ID",bidderId);
        return evalResultJlMapper.selectOne(queryWrapper);
    }
}
