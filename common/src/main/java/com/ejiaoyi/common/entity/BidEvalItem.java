package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 评分结果单项
 *
 * @author Z0001
 * @since 2020-07-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bid_eval_item")
public class BidEvalItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 数据时间戳
     */
    @TableField("INSERT_TIME")
    private String insertTime;

    /**
     * 投标人主键ID
     */
    @TableField("BIDDER_ID")
    private Integer bidderId;

    /**
     * 评分标准ID
     */
    @TableField("GRADE_ID")
    private Integer gradeId;

    /**
     * 评分项信息ID
     */
    @TableField("GRADE_ITEM_ID")
    private Integer gradeItemId;

    /**
     * 评审专家ID
     */
    @TableField("EVAL_EXPERT_ID")
    private Integer evalExpertId;

    /**
     * 专家评审表ID
     */
    @TableField("EXPERT_GRADE_TABLE_ID")
    private Integer expertGradeTableId;

    /**
     * 评审类别
     */
    @TableField("GRADE_TYPE")
    private String gradeType;

    /**
     * 评审方式(0打分;1:(不)通过)
     */
    @TableField("EVAL_TYPE")
    private String evalType;

    /**
     * 评审结果(evalType为1时 0:不通过,1:通过)
     */
    @TableField("EVAL_SCORE")
    private String evalScore;

    /**
     * 评审意见
     */
    @TableField("EVAL_COMMENTS")
    private String evalComments;

    /******************************自定义字段******************************/
}
