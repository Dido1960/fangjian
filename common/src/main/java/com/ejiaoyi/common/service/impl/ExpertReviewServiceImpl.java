package com.ejiaoyi.common.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ejiaoyi.common.annotation.RedissonLock;
import com.ejiaoyi.common.constant.CacheName;
import com.ejiaoyi.common.entity.ExpertReview;
import com.ejiaoyi.common.enums.TimeFormatter;
import com.ejiaoyi.common.mapper.ExpertReviewMapper;
import com.ejiaoyi.common.service.IExpertReviewService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 专家评审表 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
@Service
public class ExpertReviewServiceImpl extends ServiceImpl<ExpertReviewMapper, ExpertReview> implements IExpertReviewService {

    @Autowired
    private ExpertReviewMapper expertReviewMapper;

    @Override
    @Cacheable(value = CacheName.EXPERT_REVIEW, key = "#expertReview.toString()", unless = "#result==null")
    public ExpertReview getExpertReview(ExpertReview expertReview) {
        QueryWrapper<ExpertReview> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(null != expertReview.getExpertId(), "EXPERT_ID", expertReview.getExpertId());
        queryWrapper.eq(null != expertReview.getBidSectionId(), "BID_SECTION_ID", expertReview.getBidSectionId());
        queryWrapper.eq(null != expertReview.getEnabled(), "ENABLED", expertReview.getEnabled());
        queryWrapper.eq(null != expertReview.getGradeId(), "GRADE_ID", expertReview.getGradeId());
        queryWrapper.in(!CommonUtil.isEmpty(expertReview.getGradeIds()), "GRADE_ID", expertReview.getGradeIds());

        return expertReviewMapper.selectOne(queryWrapper);
    }

    @Override
    public List<ExpertReview> listCompleteExpertReviewInfo(Integer gradeId) {
        QueryWrapper<ExpertReview> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("GRADE_ID", gradeId);
        queryWrapper.eq("ENABLED", 1);

        return expertReviewMapper.selectList(queryWrapper);
    }

    @Override
    public List<ExpertReview> listCompleteExpertReviewInfo(Integer bidSectionId, Integer expertId, Integer gradeId) {
        QueryWrapper<ExpertReview> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(!CommonUtil.isEmpty(bidSectionId), "BID_SECTION_ID", bidSectionId);
        queryWrapper.eq(!CommonUtil.isEmpty(gradeId), "GRADE_ID", gradeId);
        queryWrapper.eq("ENABLED", 1);
        queryWrapper.eq(!CommonUtil.isEmpty(expertId), "EXPERT_ID", expertId);

        return expertReviewMapper.selectList(queryWrapper);
    }

    @Override
    @CacheEvict(value = CacheName.EXPERT_REVIEW, allEntries = true)
    public boolean updateExpertReview(ExpertReview expertReview) {
        Assert.notNull(expertReview, "param expertReview can not be null!");
        Assert.notNull(expertReview.getId(), "param id can not be null!");
        return expertReviewMapper.updateById(expertReview) == 1;
    }

    @Override
    @CacheEvict(value = CacheName.EXPERT_REVIEW, allEntries = true)
    public Integer insertExpertReview(ExpertReview expertReview) {
        return expertReviewMapper.insert(expertReview);
    }

    @Override
    @CacheEvict(value = CacheName.EXPERT_REVIEW, allEntries = true)
    public boolean updatePersonalReviewEnd(String[] gradeIds, Integer evalProcess, Integer expertId) {
        try {
            Assert.notNull(evalProcess, "param evalProcess can not be null!");
            Assert.notNull(expertId, "param expertId can not be null!");
            expertReviewMapper.updatePersonalReviewEnd(gradeIds, evalProcess, expertId, DateUtil.formatLocalDateTime(LocalDateTime.now()));
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    @CacheEvict(value = CacheName.EXPERT_REVIEW, allEntries = true)
    public boolean updateCallPersonReview(String[] gradeIds, Integer evalProcess) {
        try {
            Assert.notNull(evalProcess, "param evalProcess can not be null!");
            expertReviewMapper.updateCallPersonReview(gradeIds, evalProcess);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    @CacheEvict(value = CacheName.EXPERT_REVIEW, allEntries = true)
    public Integer updateAllExpertReview(Integer[] gradeIds, Integer bidSectionId, int enabled) {
        UpdateWrapper<ExpertReview> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("GRADE_ID", gradeIds);
        updateWrapper.eq("BID_SECTION_ID", bidSectionId);
        updateWrapper.set("ENABLED", enabled);
        return expertReviewMapper.update(null, updateWrapper);
    }

    @Override
    @CacheEvict(value = CacheName.EXPERT_REVIEW, allEntries = true)
    public Integer deleteByGradeIds(Integer[] gradeIds, Integer bidSectionId) {
        QueryWrapper<ExpertReview> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("GRADE_ID", gradeIds);
        queryWrapper.eq("BID_SECTION_ID", bidSectionId);
        return expertReviewMapper.delete(queryWrapper);
    }
}
