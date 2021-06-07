package com.ejiaoyi.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ejiaoyi.common.dto.ExpertReviewDetailDTO;
import com.ejiaoyi.common.entity.GradeItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
* 评分项信息 Mapper 接口
*
* @author Z0001
* @since 2020-07-03
*/
@Component
public interface GradeItemMapper extends BaseMapper<GradeItem> {

    /**
     * 通过条件获取所有的item
     * @param gradeIds 虽有的gradeid
     * @param evalProcess 评审环节
     * @param reviewType 评审类型
     * @return 通过条件获取所有的item
     */
    List<GradeItem> listGradeItemBySth(@Param("gradeIds") String[] gradeIds, @Param("evalProcess") Integer evalProcess, @Param("reviewType")  Integer reviewType);

    /**
     * 获取 “某评分项”的“所有专家”对“某投标人”的评审明细 合格制
     * @param bidSectionId 标段id
     * @param bidderId 投标人id
     * @param gradeItemId 评分项id
     * @return
     */
    List<ExpertReviewDetailDTO> listAllExpertGradeItem(@Param("bidSectionId") Integer bidSectionId,@Param("bidderId") Integer bidderId,@Param("gradeItemId") Integer gradeItemId);

    /**
     * 获取 “某评分项”的“所有专家”对“某投标人”的评审明细 打分制
     * @param bidSectionId 标段id
     * @param bidderId 投标人id
     * @param gradeItemId 评分项id
     * @return
     */
    List<ExpertReviewDetailDTO> listAllExpertGradeItemScore(@Param("bidSectionId") Integer bidSectionId, @Param("bidderId") Integer bidderId, @Param("gradeItemId") Integer gradeItemId);

    /**
     * 获取 “某评分项”的“所有专家”对“某投标人”的评审明细 扣分分值
     * @param bidSectionId 标段id
     * @param bidderId 投标人id
     * @param gradeItemId 评分项id
     * @return
     */
    List<ExpertReviewDetailDTO> listAllExpertGradeItemDeductScore(@Param("bidSectionId") Integer bidSectionId, @Param("bidderId") Integer bidderId, @Param("gradeItemId") Integer gradeItemId);
}
