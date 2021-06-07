package com.ejiaoyi.common.dto;

import lombok.*;

import java.io.Serializable;

/**
 * 专家打分明细DTO，用于数据封装
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ExpertReviewDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 评分项ID
     */
    private Integer gradeItemId;


    /**
     * 专家id
     */
    private Integer expertId;

    /**
     * 专家名称
     */
    private String expertName;

    /**
     * 投标企业ID
     */
    private Integer bidderId;

    /**
     * 专家该评分项对该投标人的评审明细(合格制为该项是否通过，打分制为该项的专家对投标企业打的分数,扣分项为扣分值)
     */
    private String expertReviewDetail;

    /**
     *  专家对投标人某评审因素打分情况
     */
    private ExpertReviewBidderScore expertReviewBidderScore;

    /**
     * 评标意见
     */
    private String evalComments;

}