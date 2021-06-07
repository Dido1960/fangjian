package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 监理项目简要评标结果
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
@TableName("eval_result_jl")
public class EvalResultJl implements Serializable {

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
     * 投标报价
     */
    @TableField("BID_PRICE")
    private String bidPrice;

    /**
     * 商务标得分
     */
    @TableField("BUSINESS_SCORE")
    private String businessScore;

    /**
     * 技术标得分
     */
    @TableField("TECHNICAL_SCORE")
    private String technicalScore;

    /**
     * 违章行为扣分
     */
    @TableField("VIOLATION_DEDUCT")
    private String violationDeduct;

    /**
     * 最终得分
     */
    @TableField("TOTAL_SCORE")
    private String totalScore;

    /**
     * 排名
     */
    @TableField("ORDER_NO")
    private Integer orderNo;

    /******************************自定义字段******************************/


    @TableField(exist = false)
    private String bidderName;
}
