package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 总承包报价得分
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
@TableName("general_contract_score")
public class GeneralContractScore implements Serializable {

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
     * 标段主键ID
     */
    @TableField("BID_SECTION_ID")
    private Integer bidSectionId;

    /**
     * 投标人主键ID
     */
    @TableField("BIDDER_ID")
    private Integer bidderId;

    /**
     * 投标报价
     */
    @TableField("BID_PRICE")
    private String bidPrice;

    /**
     * 偏差率(单位：%)
     */
    @TableField("DEVIATION_RATE")
    private String deviationRate;

    /**
     * 报价得分
     */
    @TableField("PRICE_SCORE")
    private String priceScore;

    /**
     * 版本号(对应招标文件version)
     */
    @TableField("VERSION")
    private Integer version;

    /******************************自定义字段******************************/
}
