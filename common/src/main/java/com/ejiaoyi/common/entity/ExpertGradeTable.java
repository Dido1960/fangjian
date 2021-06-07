package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 专家评审表
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
@TableName("expert_grade_table")
public class ExpertGradeTable implements Serializable {

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
     * 标段ID
     */
    @TableField("BID_SECTION_ID")
    private Integer bidSectionId;

    /**
     * 评分标准ID
     */
    @TableField("GRADE_ID")
    private Integer gradeId;

    /**
     * 评分项ID
     */
    @TableField("GRADE_ITEM_ID")
    private Integer gradeItemId;

    /**
     * 评审专家ID
     */
    @TableField("EVAL_EXPERT_ID")
    private Integer evalExpertId;

    /**
     * 评审类别
     */
    @TableField("GRADE_TYPE")
    private String gradeType;

    /**
     * 启用状态  参见数据字典enabled
     */
    @TableField("ENABLED")
    private Integer enabled;

    /******************************自定义字段******************************/
}
