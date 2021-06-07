package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.annotation.RedissonLock;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.TimeFormatter;
import com.ejiaoyi.common.mapper.ExpertReviewMutualMapper;
import com.ejiaoyi.common.mapper.GradeItemMapper;
import com.ejiaoyi.common.service.IExpertReviewMutualService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ejiaoyi.common.service.IExpertReviewService;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.DateTimeUtil;
import org.apache.commons.math3.analysis.function.Exp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 专家对企业评审单项评审结果 互保共建 服务实现类
 * </p>
 *
 * @author liuguoqiang
 * @since 2020-12-28
 */
@Service
public class ExpertReviewMutualServiceImpl extends ServiceImpl<ExpertReviewMutualMapper, ExpertReviewMutual> implements IExpertReviewMutualService {

    @Autowired
    private ExpertReviewMutualMapper expertReviewMutualMapper;

    @Autowired
    private IExpertReviewService expertReviewService;


    @Override
    @RedissonLock(key = "#expertId + '_' + #gradeId")
    public ExpertReview initReviewMutual(Integer bidSectionId, Integer expertId, Integer gradeId, List<Bidder> bidders) {
        ExpertReview query = ExpertReview.builder()
                .gradeId(gradeId)
                .bidSectionId(bidSectionId)
                .expertId(expertId)
                .build();
        ExpertReview review = expertReviewService.getExpertReview(query);
        if (!CommonUtil.isEmpty(review)) {
            return review;
        }
        ExpertReview expertReview = ExpertReview.builder()
                .gradeId(gradeId)
                .initStatus(1)
                .bidSectionId(bidSectionId)
                .expertId(expertId)
                .startTime(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS))
                .build();
        expertReviewService.insertExpertReview(expertReview);

        List<ExpertReviewMutual> resultList = new ArrayList<>();

        ExpertReviewMutual expertReviewMutual = ExpertReviewMutual.builder()
                .expertId(expertId)
                .expertReviewId(expertReview.getId())
                .gradeId(gradeId)
                .build();

        for (Bidder bidder : bidders) {
            resultList.add(ExpertReviewMutual.builder()
                    .expertId(expertId)
                    .expertReviewId(expertReview.getId())
                    .gradeId(gradeId)
                    .bidderId(bidder.getId())
                    .build());
        }

        saveBatch(resultList);
        return expertReviewService.getExpertReview(query);
    }

    @Override
    public List<ExpertReviewMutual> listExpertReviewMutual(ExpertReviewMutual expertReviewMutual, boolean isHasResult) {
        return expertReviewMutualMapper.listExpertReviewMutual(expertReviewMutual, isHasResult);
    }

    @Override
    public List<ExpertReviewMutual> listMutualResultGroup(Integer gradeId, Integer bidderId) {
        Assert.notNull(gradeId,"param gradeId can not be null");
        return expertReviewMutualMapper.listMutualResultGroup(gradeId, bidderId);
    }

    @Override
    public Integer deleteByGradeIds(Integer[] gradeIds) {
        QueryWrapper<ExpertReviewMutual> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("GRADE_ID",gradeIds);
        return expertReviewMutualMapper.delete(queryWrapper);
    }
}
