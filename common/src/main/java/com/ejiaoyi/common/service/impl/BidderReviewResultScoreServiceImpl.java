package com.ejiaoyi.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.entity.BidderReviewResultDeduct;
import com.ejiaoyi.common.entity.BidderReviewResultScore;
import com.ejiaoyi.common.mapper.BidderReviewResultScoreMapper;
import com.ejiaoyi.common.service.IBidderReviewResultScoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ejiaoyi.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 企业评审单项的评审结果表 打分制 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
@Service
public class BidderReviewResultScoreServiceImpl extends ServiceImpl<BidderReviewResultScoreMapper, BidderReviewResultScore> implements IBidderReviewResultScoreService {

    @Autowired
    private BidderReviewResultScoreMapper bidderReviewResultScoreMapper;

    @Override
    public Integer saveOrUpdateResultScore(BidderReviewResultScore newResult) {
        if (CommonUtil.isEmpty(newResult.getId())) {
            BidderReviewResultScore result = getBidderReviewResultScore(newResult);
            if (null == result) {
                bidderReviewResultScoreMapper.insert(newResult);
            } else {
                newResult.setId(result.getId());
                bidderReviewResultScoreMapper.updateById(newResult);
            }
        } else {
            bidderReviewResultScoreMapper.updateById(newResult);
        }
        return newResult.getId();
    }

    @Override
    public BidderReviewResultScore getBidderReviewResultScore(BidderReviewResultScore bidderReviewResultScore) {
        QueryWrapper<BidderReviewResultScore> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(null != bidderReviewResultScore.getId(), "ID", bidderReviewResultScore.getId());
        queryWrapper.eq(null != bidderReviewResultScore.getBidderId(), "BIDDER_ID", bidderReviewResultScore.getBidderId());
        queryWrapper.eq(null != bidderReviewResultScore.getGradeId(), "GRADE_ID", bidderReviewResultScore.getGradeId());
        queryWrapper.eq(null != bidderReviewResultScore.getBidSectionId(), "BID_SECTION_ID", bidderReviewResultScore.getBidSectionId());

        return bidderReviewResultScoreMapper.selectOne(queryWrapper);
    }

    @Override
    public String getTotalScore(String[] gradeIds, Integer reviewType, Integer bidderId) {
        return bidderReviewResultScoreMapper.getTotalScore(gradeIds, reviewType, bidderId);
    }

    @Override
    public Integer deleteByGradeIds(Integer[] gradeIds) {
        QueryWrapper<BidderReviewResultScore> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("GRADE_ID",gradeIds);
        return bidderReviewResultScoreMapper.delete(queryWrapper);
    }
}
