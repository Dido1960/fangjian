package com.ejiaoyi.common.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 投标人DTO，用于数据封装
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BidderDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 投标人id
     */
    private Integer bidderId;

    /**
     * 投标人名称
     */
    private String bidderName;

    /**
     * 投标人该评分项得结果,此结果是专家组最后的综合结果(合格制为该项是否通过，打分制为该项的平均分)
     * 说明：合格制，0/空 不通过；1:通过
     */
    private String gradeItemResult;

    /**
     * 所有grade总分(得分汇总)
     */
    private String gradesTotal;

    /**
     * 专家打分明细DTO集合，表示针对该评审项每个专家对该投标企业的评审情况
     */
    private List<ExpertReviewDetailDTO> expertReviewDetailDTOs;


    private List<GradeItemDTO> list;

    private Map<Integer, GradeItemDTO> gradeItemMap;
}