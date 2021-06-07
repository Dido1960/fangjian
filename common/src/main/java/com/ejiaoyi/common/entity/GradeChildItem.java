package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 评分项子项信息
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
@TableName("grade_child_item")
public class GradeChildItem implements Serializable {

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
     * 评分标准ID
     */
    @TableField("GRADE_ITEM_ID")
    private Integer gradeItemId;

    /**
     * 评分项内容
     */
    @TableField("ITEM_CONTENT")
    private String itemContent;

    /**
     * 评分项分数
     */
    @TableField("SCORE")
    private String score;

    /**
     * 引用后台配置好的RuleTradeRule规则
     */
    @TableField("RULE_TRADE_CODE")
    private String ruleTradeCode;

    /**
     * 备注说明
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 打分范围
     */
    @TableField("SCORE_RANGE")
    private String scoreRange;

    /******************************自定义字段******************************/
}
