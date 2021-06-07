package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.annotation.RedissonLock;
import com.ejiaoyi.common.dto.BidderResultDTO;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.TimeFormatter;
import com.ejiaoyi.common.mapper.ExpertReviewSingleItemScoreMapper;
import com.ejiaoyi.common.service.IExpertReviewService;
import com.ejiaoyi.common.service.IExpertReviewSingleItemScoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ejiaoyi.common.service.IGradeItemService;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 专家对企业评审单项评审结果 打分制 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
@Service
@Slf4j
public class ExpertReviewSingleItemScoreServiceImpl extends ServiceImpl<ExpertReviewSingleItemScoreMapper, ExpertReviewSingleItemScore> implements IExpertReviewSingleItemScoreService {
    @Autowired
    private ExpertReviewSingleItemScoreMapper expertReviewSingleItemScoreMapper;
    @Autowired
    private IExpertReviewService expertReviewService;
    @Autowired
    private IGradeItemService gradeItemService;

    @Override
    @RedissonLock(key = "'score_'+#bidSectionId+'_'+#expertId")
    public ExpertReview initReviewItemScore(Integer bidSectionId, Integer expertId, List<Grade> grades, List<Bidder> bidders) {
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
            ExpertReviewSingleItemScore score = ExpertReviewSingleItemScore.builder()
                    .expertId(expertId)
                    .expertReviewId(expertReview.getId())
                    .gradeId(grade.getId())
                    .build();
            for (Bidder bidder : bidders) {
                for (GradeItem gradeItem : grade.getGradeItems()) {
                    score.setId(null);
                    score.setBidderId(bidder.getId());
                    score.setGradeItemId(gradeItem.getId());
                    expertReviewSingleItemScoreMapper.insert(score);
                }
            }
        }
        return expertReviewService.getExpertReview(query);
    }

    @Override
    public List<ExpertReviewSingleItemScore> listHasScore(Integer expertId, Integer gradeId, Integer bidderId) {
        return expertReviewSingleItemScoreMapper.listHasScore(expertId, gradeId, bidderId);
    }

    @Override
    public List<Grade> listGradeForBidderResult(List<Grade> grades, Integer bidderId, Integer expertId) {
        for (Grade grade : grades) {
            ExpertReviewSingleItemScore query = ExpertReviewSingleItemScore.builder()
                    .gradeId(grade.getId())
                    .bidderId(bidderId)
                    .expertId(expertId)
                    .build();
            grade.setExpertReviewSingleItemScores(listScoreResult(query));
        }
        return grades;
    }

    @Override
    public List<ExpertReviewSingleItemScore> listScoreResult(ExpertReviewSingleItemScore expertReviewSingleItemScore) {
        List<ExpertReviewSingleItemScore> list = expertReviewSingleItemScoreMapper.listScoreResult(expertReviewSingleItemScore);
        for (ExpertReviewSingleItemScore reviewSingleItemDeductScore : list) {
            GradeItem gradeItem = gradeItemService.getById(reviewSingleItemDeductScore.getGradeItemId());
            reviewSingleItemDeductScore.setGradeItem(gradeItem);
        }
        return list;
    }

    @Override
    public ExpertReviewSingleItemScore getScoreById(Integer id) {
        Assert.notNull(id, "param id can not be null!");
        ExpertReviewSingleItemScore expertReviewSingleItemScore = expertReviewSingleItemScoreMapper.selectById(id);
        expertReviewSingleItemScore.setGradeItem(gradeItemService.getById(expertReviewSingleItemScore.getGradeItemId()));
        return expertReviewSingleItemScore;
    }

    @Override
    public List<ExpertReviewSingleItemScore> listHasScoreBySth(String[] gradeIds, Integer bidderId, Integer expertId, Integer gradeItemId, Integer evalProcess, Integer reviewType) {
        return expertReviewSingleItemScoreMapper.listScoreBySth(gradeIds, bidderId, expertId, gradeItemId, evalProcess, reviewType, true);
    }

    @Override
    public List<ExpertReviewSingleItemScore> listAllHasScore(Integer expertId, Integer gradeId) {
        QueryWrapper<ExpertReviewSingleItemScore> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("GRADE_ID", gradeId);
        queryWrapper.eq("EXPERT_ID", expertId);
        queryWrapper.ne("EVAL_SCORE", "");
        return expertReviewSingleItemScoreMapper.selectList(queryWrapper);
    }

    @Override
    public List<ExpertReviewSingleItemScore> listScoreBySth(String[] gradeIds, Integer bidderId, Integer expertId, Integer gradeItemId, Integer evalProcess, Integer reviewType) {
        return expertReviewSingleItemScoreMapper.listScoreBySth(gradeIds, bidderId, expertId, gradeItemId, evalProcess, reviewType, false);
    }

    @Override
    public boolean checkScoreConsistent(String[] gradeIds, Integer reviewType, Integer bidderNum) {
        List<Integer> list = expertReviewSingleItemScoreMapper.listScoreConsistent(gradeIds, reviewType);
        for (Integer integer : list) {
            if (integer > bidderNum) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getAvgScore(Integer gradeId, Integer bidderId, Integer size) {
        return expertReviewSingleItemScoreMapper.getAvgScore(gradeId, bidderId, size);
    }

    @Override
    public Boolean checkBidderResultConsistent(String[] gradeIds, Integer reviewType, Integer bidderId, Integer itemNum) {
        return itemNum.equals(expertReviewSingleItemScoreMapper.countScoreResult(gradeIds, reviewType, bidderId));
    }

    @Override
    public String getAvgScoreForReview(String[] gradeIds, Integer evalProcess, Integer reviewType, Integer bidderId, Integer expertNum) {
        return expertReviewSingleItemScoreMapper.getAvgScoreForReview(gradeIds, evalProcess, reviewType, bidderId, expertNum);
    }


    @Override
    public List<BidderResultDTO> getAvgScoreBySth(String[] gradeIds, Integer gradeId, Integer bidSectionId, Integer bidderId, Integer gradeItemId) {
        return expertReviewSingleItemScoreMapper.getAvgScoreBySth(gradeIds, gradeId, bidSectionId, bidderId, gradeItemId);
    }

    @Override
    public Integer deleteByGradeIds(Integer[] gradeIds) {
        QueryWrapper<ExpertReviewSingleItemScore> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("GRADE_ID",gradeIds);
        return expertReviewSingleItemScoreMapper.delete(queryWrapper);
    }
}
