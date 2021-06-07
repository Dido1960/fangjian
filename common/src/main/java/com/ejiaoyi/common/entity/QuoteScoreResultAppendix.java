package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 投标人报价得分结果附录
 * </p>
 *
 * @author Make
 * @since 2020-12-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("quote_score_result_appendix")
public class QuoteScoreResultAppendix implements Serializable {

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
     * 投标人评标价
     */
    @TableField("BID_PRICE")
    private String bidPrice;

    /**
     * 对应报价偏差率
     */
    @TableField("BID_PRICE_OFFSET")
    private String bidPriceOffset;

    /**
     * 对应报价得分
     */
    @TableField("BID_PRICE_SCORE")
    private String bidPriceScore;

    /**
     * 投标人名称
     */
    @TableField(exist = false)
    private String bidderName;
}
