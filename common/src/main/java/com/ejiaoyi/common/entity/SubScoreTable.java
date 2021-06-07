package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 施工项目扣分分数表
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
@TableName("sub_score_table")
public class SubScoreTable implements Serializable {

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
     * 企业ID
     */
    @TableField("BIDDER_ID")
    private Integer bidderId;

    /**
     * 施工能力扣分分数
     */
    @TableField("ABILITY")
    private String ability;

    /**
     * 施工组织设计扣分分数
     */
    @TableField("ORGANIZE")
    private String organize;

    /**
     * 安全质量事故扣分分数
     */
    @TableField("QUALITY")
    private String quality;

    /**
     * 不良行为扣分分数
     */
    @TableField("BAD_RECORD")
    private String badRecord;

    /**
     * 互保共建加分
     */
    @TableField("MUTUAL_SECURITY")
    private String mutualSecurity;

    /******************************自定义字段******************************/
}
