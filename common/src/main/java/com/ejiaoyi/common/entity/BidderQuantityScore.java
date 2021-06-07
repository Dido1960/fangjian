package com.ejiaoyi.common.entity;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 投标人工程量清单报价得分结果
 * </p>
 *
 * @author Make
 * @since 2020-12-19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bidder_quantity_score")
public class BidderQuantityScore implements Serializable {

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
     * 投标人id
     */
    @TableField("BIDDER_ID")
    private Integer bidderId;

    /**
     * 投标人名称
     */
    @TableField("BIDDER_NAME")
    private String bidderName;

    /**
     * 分部分项工程量清单报价A 得分
     */
    @TableField("SCORE_A")
    private BigDecimal scoreA;

    /**
     * 措施项目清单报价B1 得分
     */
    @TableField("SCORE_B1")
    private BigDecimal scoreB1;

    /**
     * 措施项目清单报价B2 得分
     */
    @TableField("SCORE_B2")
    private BigDecimal scoreB2;

    /**
     * 其它项目清单C(总承包服务费) 得分
     */
    @TableField("SCORE_C")
    private BigDecimal scoreC;

    /**
     * 规费清单报价D 得分
     */
    @TableField("SCORE_D")
    private BigDecimal scoreD;

    /**
     * 税金清单报价E 得分
     */
    @TableField("SCORE_E")
    private BigDecimal scoreE;

    /**
     * 综合单价F 得分
     */
    @TableField("SCORE_F")
    private BigDecimal scoreF;

    /**
     * 主要材料设备单价G 得分
     */
    @TableField("SCORE_G")
    private BigDecimal scoreG;

    /**
     * 总得分
     */
    @TableField("TOTAL_SCORE")
    private BigDecimal totalScore;


}
