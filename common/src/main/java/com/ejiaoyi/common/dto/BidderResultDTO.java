package com.ejiaoyi.common.dto;

import lombok.*;

import java.io.Serializable;

/**
 * 某环节的投标人结果汇总表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BidderResultDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 企业Id
     */
    private Integer bidderId;

    /**
     * 评分标准id
     */
    private Integer gradeId;

    /**
     * 评分标准id
     */
    private Integer gradeItemId;

    /**
     * 通过项数量
     */
    private Integer passSum;

    /**
     * 不通过项数量
     */
    private Integer noPassSum;

    /**
     * 不扣分项数量
     */
    private Integer noDeductSum;

    /**
     * 扣分项数量
     */
    private Integer deductSum;

    /**
     * 算术平均分
     */
    private String arithmeticScore;

    /**
     * 当项item分数
     */
    private String score;

    /**
     * 打分情况是否一致！
     */
    private Boolean isConsistent;

    /**
     * 当前分数的出现次数
     */
    private Integer scoreCont;

    /**
     * 当前结果为合格制或扣分选择制 的打分结果 1：合格/不扣分 0：不合格/扣分
     */
    private Integer passResult;

}
