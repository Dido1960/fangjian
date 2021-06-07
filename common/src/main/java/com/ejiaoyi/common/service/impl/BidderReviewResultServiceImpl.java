package com.ejiaoyi.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.annotation.RedissonLock;
import com.ejiaoyi.common.constant.EvalResult;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.BidProtype;
import com.ejiaoyi.common.enums.EvalProcess;
import com.ejiaoyi.common.mapper.BidderReviewResultMapper;
import com.ejiaoyi.common.service.IBidSectionService;
import com.ejiaoyi.common.service.IBidderReviewResultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ejiaoyi.common.service.IBidderService;
import com.ejiaoyi.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 企业评审grade单项的评审结果表 合格制 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
@Service
public class BidderReviewResultServiceImpl extends ServiceImpl<BidderReviewResultMapper, BidderReviewResult> implements IBidderReviewResultService {

    @Autowired
    private BidderReviewResultMapper bidderReviewResultMapper;

    @Autowired
    private IBidderService bidderService;

    @Autowired
    private IBidSectionService bidSectionService;

    @Override
    public List<Bidder> listBidderGradeResult(Integer bidSectionId, Integer gradeId, Integer evalProcess) {
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        List<Bidder> bidders;
        BidderReviewResult result = BidderReviewResult.builder()
                .bidSectionId(bidSectionId)
                .gradeId(gradeId)
                .build();
        if (EvalProcess.PRELIMINARY.getCode().equals(evalProcess)) {
            bidders = bidderService.listPassBidOpenBidder(bidSectionId);
            for (Bidder bidder : bidders) {
                result.setBidderId(bidder.getId());
                BidderReviewResult query = getBidderReviewResult(result);
                if (query != null) {
                    bidder.setGradeResult(query.getResult());
                }
            }
            //资格预审详细评审结果
        }else if (EvalProcess.DETAILED.getCode().equals(evalProcess) && BidProtype.QUALIFICATION.getCode().equals(bidSection.getBidClassifyCode())) {
            bidders = bidderService.listDetailedBidder(bidSectionId);
            for (Bidder bidder : bidders) {
                result.setBidderId(bidder.getId());
                BidderReviewResult query = getBidderReviewResult(result);
                if (query != null) {
                    bidder.setGradeResult(query.getResult());
                }
            }
        } else {
            return null;
        }
        return bidders;
    }


    @Override
    public BidderReviewResult getBidderReviewResult(BidderReviewResult bidderReviewResult) {
        QueryWrapper<BidderReviewResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(null != bidderReviewResult.getId(), "ID", bidderReviewResult.getId());
        queryWrapper.eq(null != bidderReviewResult.getBidderId(), "BIDDER_ID", bidderReviewResult.getBidderId());
        queryWrapper.eq(null != bidderReviewResult.getGradeId(), "GRADE_ID", bidderReviewResult.getGradeId());
        queryWrapper.eq(null != bidderReviewResult.getBidSectionId(), "BID_SECTION_ID", bidderReviewResult.getBidSectionId());

        return bidderReviewResultMapper.selectOne(queryWrapper);
    }

    @Override
    @RedissonLock(key = "'BidderReviewResult_'+#bidderReviewResult.bidderId+'_'+#bidderReviewResult.bidSectionId+'_'+#bidderReviewResult.gradeId")
    public Integer saveOrUpdateResult(BidderReviewResult bidderReviewResult) {
        if (CommonUtil.isEmpty(bidderReviewResult.getId())) {
            BidderReviewResult result = getBidderReviewResult(bidderReviewResult);
            if (null == result) {
                bidderReviewResultMapper.insert(bidderReviewResult);
            } else {
                bidderReviewResult.setId(result.getId());
                bidderReviewResultMapper.updateById(bidderReviewResult);
            }
        } else {
            bidderReviewResultMapper.updateById(bidderReviewResult);
        }
        return bidderReviewResult.getId();
    }

    @Override
    public Boolean listBidderProcessResult(Integer bidSectionId, Integer bidderId, List<Grade> grades){
        String [] gradeIds = new String[grades.size()];
        for (int i = 0; i < grades.size(); i++) {
            gradeIds[i] = grades.get(i).getId().toString();
        }
        QueryWrapper<BidderReviewResult> wrapper = new QueryWrapper<>();
        wrapper.eq("BID_SECTION_ID", bidSectionId);
        wrapper.eq("BIDDER_ID", bidderId);
        wrapper.in("GRADE_ID", gradeIds);
        wrapper.orderByAsc("ID");

        boolean isQualified = true;
        List<BidderReviewResult> bidderReviewResults = bidderReviewResultMapper.selectList(wrapper);
        for (BidderReviewResult bidderReviewResult : bidderReviewResults) {
            String result = bidderReviewResult.getResult();
            if (CommonUtil.isEmpty(result) || EvalResult.UNQUALIFIED.equals(result)) {
                isQualified = false;
            }
        }
        return isQualified;
    }

    @Override
    public Integer deleteByGradeIds(Integer[] gradeIds) {
        QueryWrapper<BidderReviewResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("GRADE_ID",gradeIds);
        return bidderReviewResultMapper.delete(queryWrapper);
    }

}
