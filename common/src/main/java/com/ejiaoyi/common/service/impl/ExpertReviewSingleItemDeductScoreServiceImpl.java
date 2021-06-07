package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.annotation.RedissonLock;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.TimeFormatter;
import com.ejiaoyi.common.mapper.ExpertReviewSingleItemDeductScoreMapper;
import com.ejiaoyi.common.service.IBidderService;
import com.ejiaoyi.common.service.IExpertReviewService;
import com.ejiaoyi.common.service.IExpertReviewSingleItemDeductScoreService;
import com.ejiaoyi.common.service.IGradeItemService;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 扣分打分 实现
 *
 * @Auther: liuguoqiang
 * @Date: 2020-11-18 15:48
 */
@Service
public class ExpertReviewSingleItemDeductScoreServiceImpl extends BaseServiceImpl<ExpertReviewSingleItemDeductScore> implements IExpertReviewSingleItemDeductScoreService {

    @Autowired
    private IExpertReviewService expertReviewService;
    @Autowired
    private ExpertReviewSingleItemDeductScoreMapper expertReviewSingleItemDeductScoreMapper;
    @Autowired
    private IGradeItemService gradeItemService;

    @Override
    @RedissonLock(key = "#expertId + '_deductScore'")
    public ExpertReview initReviewItemDeductScore(Integer bidSectionId, Integer expertId, List<Grade> grades, List<Bidder> bidders) {
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

            ExpertReviewSingleItemDeductScore deductScore = ExpertReviewSingleItemDeductScore.builder()
                    .expertId(expertId)
                    .expertReviewId(expertReview.getId())
                    .gradeId(grade.getId())
                    .build();
            for (Bidder bidder : bidders) {
                for (GradeItem gradeItem : grade.getGradeItems()) {
                    deductScore.setId(null);
                    deductScore.setBidderId(bidder.getId());
                    deductScore.setGradeItemId(gradeItem.getId());
                    expertReviewSingleItemDeductScoreMapper.insert(deductScore);
                }
            }
        }
        return expertReviewService.getExpertReview(query);
    }

    @Override
    public List<ExpertReviewSingleItemDeductScore> listHasScore(Integer expertId, Integer gradeId, Integer bidderId) {
        QueryWrapper<ExpertReviewSingleItemDeductScore> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("GRADE_ID", gradeId);
        queryWrapper.eq("EXPERT_ID", expertId);
        queryWrapper.eq("BIDDER_ID", bidderId);
        queryWrapper.ne("EVAL_SCORE", "");
        return expertReviewSingleItemDeductScoreMapper.selectList(queryWrapper);
    }

    @Override
    public List<Grade> listGradeForBidderResult(List<Grade> grades, Integer bidderId, Integer expertId) {
        for (Grade grade : grades) {
            ExpertReviewSingleItemDeductScore query = ExpertReviewSingleItemDeductScore.builder()
                    .gradeId(grade.getId())
                    .bidderId(bidderId)
                    .expertId(expertId)
                    .build();
            grade.setExpertReviewSingleItemDeductScores(listDeductScore(query));
        }
        return grades;
    }

    @Override
    public List<ExpertReviewSingleItemDeductScore> listDeductScore(ExpertReviewSingleItemDeductScore expertReviewSingleItemDeductScore) {
        List<ExpertReviewSingleItemDeductScore> list = expertReviewSingleItemDeductScoreMapper.listDeductScore(expertReviewSingleItemDeductScore);
        for (ExpertReviewSingleItemDeductScore reviewSingleItemDeductScore : list) {
            GradeItem gradeItem = gradeItemService.getById(reviewSingleItemDeductScore.getGradeItemId());
            reviewSingleItemDeductScore.setGradeItem(gradeItem);
        }
        return list;
    }

    @Override
    public ExpertReviewSingleItemDeductScore getDeductScoreById(Integer id) {
        Assert.notNull(id, "param id can not be null!");
        ExpertReviewSingleItemDeductScore expertReviewSingleItemDeductScore = expertReviewSingleItemDeductScoreMapper.selectById(id);
        expertReviewSingleItemDeductScore.setGradeItem(gradeItemService.getById(expertReviewSingleItemDeductScore.getGradeItemId()));
        return expertReviewSingleItemDeductScore;
    }

    @Override
    public Integer updateById(ExpertReviewSingleItemDeductScore updateDeductScore) {
        Assert.notNull(updateDeductScore.getId(), "param id can not be null!");
        return expertReviewSingleItemDeductScoreMapper.updateById(updateDeductScore);
    }

    @Override
    public List<ExpertReviewSingleItemDeductScore> listHasDeductScoreBySth(String[] gradeIds, Integer bidderId, Integer expertId, Integer gradeItemId, Integer reviewType) {
        return expertReviewSingleItemDeductScoreMapper.listDeductScoreBySth(gradeIds, bidderId, expertId, gradeItemId, reviewType, true);
    }

    @Override
    public List<ExpertReviewSingleItemDeductScore> listAllHasDeductScore(Integer expertId, Integer gradeId) {
        QueryWrapper<ExpertReviewSingleItemDeductScore> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("GRADE_ID", gradeId);
        queryWrapper.eq("EXPERT_ID", expertId);
        queryWrapper.ne("EVAL_SCORE", "");
        return expertReviewSingleItemDeductScoreMapper.selectList(queryWrapper);
    }

    @Override
    public boolean updateOneKeyNoDeduct(String[] gradeIds, Integer reviewType, Integer expertId) {
        try {
            Assert.notNull(reviewType, "param reviewType can not be null!");
            Assert.notNull(expertId, "param expertId can not be null!");
            expertReviewSingleItemDeductScoreMapper.updateOneKeyNoDeduct(gradeIds, reviewType, expertId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<ExpertReviewSingleItemDeductScore> listDeductScoreBySth(String[] gradeIds, Integer bidderId, Integer expertId, Integer gradeItemId, Integer reviewType) {
        return expertReviewSingleItemDeductScoreMapper.listDeductScoreBySth(gradeIds, bidderId, expertId, gradeItemId, reviewType, false);
    }

    @Override
    public boolean checkDeductScoreConsistent(String[] gradeIds, Integer reviewType, Integer bidderNum) {
        List<Integer> list = expertReviewSingleItemDeductScoreMapper.listDeductScoreConsistent(gradeIds, reviewType);
        for (Integer integer : list) {
            if (integer > bidderNum) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getAvgDeductScore(Integer gradeId, Integer bidderId, Integer expertNum) {
        return expertReviewSingleItemDeductScoreMapper.getAvgDeductScore(gradeId, bidderId, expertNum);
    }

    @Override
    public Boolean checkBidderResultConsistent(String[] gradeIds, Integer reviewType, Integer bidderId, Integer itemNum) {
        return itemNum.equals(expertReviewSingleItemDeductScoreMapper.countDeductScoreResult(gradeIds, reviewType, bidderId));
    }

    @Override
    public String getAvgDeductScoreForReview(String[] gradeIds, Integer reviewType, Integer bidderId, Integer expertNum) {
        return expertReviewSingleItemDeductScoreMapper.getAvgDeductScoreForReview(gradeIds, reviewType, bidderId, expertNum);
    }

    @Override
    public Integer deleteByGradeIds(Integer[] gradeIds) {
        QueryWrapper<ExpertReviewSingleItemDeductScore> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("GRADE_ID",gradeIds);
        return expertReviewSingleItemDeductScoreMapper.delete(queryWrapper);
    }
}
