package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 专家对企业评审单项评审结果 扣分制
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("expert_review_single_item_deduct")
public class ExpertReviewSingleItemDeduct implements Serializable {

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
     * 评审专家主键ID
     */
    @TableField("EXPERT_ID")
    private Integer expertId;

    /**
     * 专家评审表ID
     */
    @TableField("EXPERT_REVIEW_ID")
    private Integer expertReviewId;

    /**
     * 投标企业ID
     */
    @TableField("BIDDER_ID")
    private Integer bidderId;

    /**
     * 评分标准ID
     */
    @TableField("GRADE_ID")
    private Integer gradeId;

    /**
     * 评审项ID
     */
    @TableField("GRADE_ITEM_ID")
    private Integer gradeItemId;

    /**
     * 评审结果(0:扣分, 1:不扣分)
     */
    @TableField("EVAL_RESULT")
    private String evalResult;

    /**
     * 扣分意见
     */
    @TableField("DEDUCT_COMMENTS")
    private String deductComments;

    /**************************自定义变量***************************/

    @TableField(exist = false)
    private GradeItem gradeItem;
}
