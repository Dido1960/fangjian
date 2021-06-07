package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ejiaoyi.common.dto.ExpertReviewDetailDTO;
import com.ejiaoyi.common.entity.GradeItem;
import com.ejiaoyi.common.mapper.GradeItemMapper;
import com.ejiaoyi.common.service.IGradeItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 评分项信息 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-09-01
 */
@Service
public class GradeItemServiceImpl extends ServiceImpl<GradeItemMapper, GradeItem> implements IGradeItemService {

    @Autowired
    private GradeItemMapper gradeItemMapper;

    @Override
    public List<GradeItem> listGradeItem(Integer gradeId) {
        Assert.notNull(gradeId, "param gradeId can not be null");
        QueryWrapper<GradeItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("GRADE_ID", gradeId);

        return gradeItemMapper.selectList(queryWrapper);
    }

    @Override
    public List<GradeItem> listGradeItemBySth(String[] gradeIds, Integer evalProcess, Integer reviewType) {
        return gradeItemMapper.listGradeItemBySth(gradeIds, evalProcess, reviewType);
    }

    @Override
    public List<ExpertReviewDetailDTO> listAllExpertGradeItem(Integer bidSectionId,Integer bidderId,Integer gradeItemId) {
        return gradeItemMapper.listAllExpertGradeItem(bidSectionId,bidderId,gradeItemId);
    }

    @Override
    public List<ExpertReviewDetailDTO> listAllExpertGradeItemScore(Integer bidSectionId,Integer bidderId,Integer gradeItemId) {
        return gradeItemMapper.listAllExpertGradeItemScore(bidSectionId,bidderId,gradeItemId);
    }

    @Override
    public List<ExpertReviewDetailDTO> listAllExpertGradeItemDeductScore(Integer bidSectionId, Integer bidderId, Integer gradeItemId) {
        return gradeItemMapper.listAllExpertGradeItemDeductScore(bidSectionId,bidderId,gradeItemId);
    }
}
