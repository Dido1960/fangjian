package com.ejiaoyi.common.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 评分项DTO，用于数据封装
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GradeItemDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 评分项id
     */
    private Integer gradeItemId;

    /**
     * 评分项名称
     */
    private String gradeItemName;

    /**
     * 评分项分数（合格制可以没有）
     */
    private String score;

    /**
     * 评分项描述
     */
    private String remark;

    /**
     * 评审步骤：评审环节 + 评审类型
     */
    /**
     * 评审环节
     */
    @TableField(exist = false)
    private String reviewProcess;

    /**
     * 评审类型
     */
    @TableField(exist = false)
    private String reviewType;

    /**
     * 投标人DTO集合
     */
    private List<BidderDTO> bidderDTOs;

    /**
     * 专家DTO集合
     */
    private List<ExpertReviewDetailDTO> expertReviewDetailDTOS;

}