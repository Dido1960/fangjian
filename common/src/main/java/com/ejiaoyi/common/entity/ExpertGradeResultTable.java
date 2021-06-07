package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 评审分项结果表
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
@TableName("expert_grade_result_table")
public class ExpertGradeResultTable implements Serializable {

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
     * 评审的企业ID
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
     * 评审的企业ID
     */
    @TableField("BIDDER_NAME")
    private String bidderName;

    /**
     * 评审类型
     */
    @TableField("GRADE_TYPE")
    private String gradeType;

    /**
     * 评审标准内容
     */
    @TableField("GRADE_ITEM_CONTENT")
    private String gradeItemContent;

    /**
     * 评审的结果
     */
    @TableField("RESULT")
    private String result;

    /******************************自定义字段******************************/
}
