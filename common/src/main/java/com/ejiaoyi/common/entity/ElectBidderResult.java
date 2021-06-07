package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 企业推选结果
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
@TableName("elect_bidder_result")
public class ElectBidderResult implements Serializable {

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
     * 被推选企业ID
     */
    @TableField("BIDDER_ID")
    private Integer bidderId;

    /**
     * 排名
     */
    @TableField("ORDER_NO")
    private Integer orderNo;

    /**
     * 理由
     */
    @TableField("REASON")
    private String reason;

    /**
     * 版本号(对应招标文件version)
     */
    @TableField("VERSION")
    private Integer version;

    /******************************自定义字段******************************/
}
