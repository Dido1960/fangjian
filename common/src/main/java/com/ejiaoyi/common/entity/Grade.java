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
 * 评分标准表
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
@TableName("grade")
public class Grade implements Serializable {

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
     * 评分标准名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 评分标准类型
     */
    @TableField("GRADE_TYPE")
    private String gradeType;

    /**
     * 评审形式(0:打分;1:合格制;2扣分制；3互保共建选择制)
     */
    @TableField("TYPE")
    private Integer type;

    /**
     * 1:加分 2:扣分
     */
    @TableField("CALC_TYPE")
    private Integer calcType;

    /**
     * 评审环节(1:初步评审; 2:详细评审)
     */
    @TableField("REVIEW_PROCESS")
    private Integer reviewProcess;

    /**
     * 评审类型  参考ReviewType
     */
    @TableField("REVIEW_TYPE")
    private Integer reviewType;

    /**
     * 总分
     */
    @TableField("SCORE")
    private String score;

    /**
     * 是否含有评分子项GradeChildItem
     */
    @TableField("HAS_ITEM")
    private Integer hasItem;

    /**
     * 小组评审是否结束 1:评审结束
     */
    @TableField("GROUP_END")
    private Integer groupEnd;

    /**
     * 创建人ID
     */
    @TableField("USER_ID")
    private Integer userId;

    /**
     * 备注说明
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 是否移除评分标准
     */
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    /******************************自定义字段******************************/


    /**
     * 评分项集合
     */
    @TableField(exist = false)
    private List<GradeItem> gradeItems;


    /**
     * 专家对企业评审单项评审结果集合
     */
    @TableField(exist = false)
    private List<ExpertReviewSingleItem> expertReviewSingleItems;

    /**
     * 专家对企业评审单项评审扣分制结果集合
     */
    @TableField(exist = false)
    private List<ExpertReviewSingleItemDeduct> expertReviewSingleItemDeducts;

    /**
     * 专家对企业评审单项评审扣分打分制结果集合
     */
    @TableField(exist = false)
    private List<ExpertReviewSingleItemDeductScore> expertReviewSingleItemDeductScores;

    /**
     * 专家对企业评审单项评审打分制结果集合
     */
    @TableField(exist = false)
    private List<ExpertReviewSingleItemScore> expertReviewSingleItemScores;
}
