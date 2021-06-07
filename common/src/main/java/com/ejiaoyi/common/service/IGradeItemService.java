package com.ejiaoyi.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ejiaoyi.common.dto.ExpertReviewDetailDTO;
import com.ejiaoyi.common.entity.GradeItem;

import java.util.List;

/**
 * <p>
 * 评分项信息 服务类
 * </p>
 *
 * @author Make
 * @since 2020-09-01
 */
public interface IGradeItemService extends IService<GradeItem> {

    /**
     * 通过评分标准id获取所有的评分项
     * @param gradeId
     * @return
     */
    List<GradeItem> listGradeItem(Integer gradeId);

    /**
     * 通过条件获取所有的item
     * @param gradeIds 虽有的gradeid
     * @param evalProcess 评审流程
     * @param reviewType 评审类型
     * @return 通过条件获取所有的item
     */
    List<GradeItem> listGradeItemBySth(String[] gradeIds, Integer evalProcess, Integer reviewType);

    /**
     * 获取 “某评分项”的“所有专家”对“某投标人”的评审明细 合格制
     * @param bidSectionId 标段id
     * @param bidderId    投标人id
     * @param gradeItemId 评分项id
     * @return
     */
    List<ExpertReviewDetailDTO> listAllExpertGradeItem(Integer bidSectionId,Integer bidderId,Integer gradeItemId);

    /**
     * 打分制 专家的评审明细
     * @param bidSectionId 标段id
     * @param bidderId    投标人id
     * @param gradeItemId 评分项id
     * @return
     */
    List<ExpertReviewDetailDTO> listAllExpertGradeItemScore(Integer bidSectionId,Integer bidderId,Integer gradeItemId);

    /**
     * 扣分制 专家的评审明细
     * @param bidSectionId 标段id
     * @param bidderId    投标人id
     * @param gradeItemId 评分项id
     * @return
     */
    List<ExpertReviewDetailDTO> listAllExpertGradeItemDeductScore(Integer bidSectionId,Integer bidderId,Integer gradeItemId);

}
