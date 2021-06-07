package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 评分项信息
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
@TableName("grade_item")
public class GradeItem implements Serializable {

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
    @TableField("GRADE_ID")
    private Integer gradeId;

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
     * 分数类型（拓展字段：酌情打分  固定分值）范围 range 固定 fixed
     */
    @TableField("SCORE_TYPE")
    private String scoreType;

    /**
     * 分数范围
     */
    @TableField("SCORE_RANGE")
    private String scoreRange;

    /**
     * 备注说明
     */
    @TableField("REMARK")
    private String remark;

    /******************************自定义字段******************************/
}
