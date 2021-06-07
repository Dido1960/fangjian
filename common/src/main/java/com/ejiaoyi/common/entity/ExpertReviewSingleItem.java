package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 专家对企业评审单项评审结果 合格制
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
@TableName("expert_review_single_item")
public class ExpertReviewSingleItem implements Serializable {

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
     * 评审类别
     */
    @TableField("GRADE_TYPE")
    private Integer gradeType;

    /**
     * 评审结果(0:不合格, 1:合格)
     */
    @TableField("EVAL_RESULT")
    private String evalResult;

    /**
     * 评审意见
     */
    @TableField("EVAL_COMMENTS")
    private String evalComments;

    /**
     * 评审该项的gradeItem名称
     */
    @TableField(exist = false)
    private String itemContent;

    /**
     * 评审该项的专家姓名
     */
    @TableField(exist = false)
    private String expertName;

    /**
     * 评审该项的投标人姓名
     */
    @TableField(exist = false)
    private String bidderName;

    /****************以上主要为系统表字段********************/
    /**
     * 标段id
     */
    @TableField(exist = false)
    private Integer bidSectionId;

    /**
     * 评审项内容
     */
    @TableField(exist = false)
    private String gradeItemContent;

}
