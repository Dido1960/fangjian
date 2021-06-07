package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 小组评审结果
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
@TableName("group_result")
public class GroupResult implements Serializable {

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
     * 标段信息主键ID
     */
    @TableField("BID_SECTION_ID")
    private Integer bidSectionId;

    /**
     * 投标人主键ID
     */
    @TableField("BIDDER_ID")
    private Integer bidderId;

    /**
     * 企业名称
     */
    @TableField("BIDDER_NAME")
    private String bidderName;

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
     * 评审类别
     */
    @TableField("GRADE_TYPE")
    private String gradeType;

    /**
     * 评审项内容
     */
    @TableField("GRADE_ITEM_NAME")
    private String gradeItemName;

    /**
     * 评审名称
     */
    @TableField("GRADE_NAME")
    private String gradeName;

    /**
     * 小组意见
     */
    @TableField("GROUP_COMMENTS")
    private String groupComments;

    /******************************自定义字段******************************/
}
