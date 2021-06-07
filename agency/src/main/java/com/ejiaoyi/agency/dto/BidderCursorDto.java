package com.ejiaoyi.agency.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 唱标：投标人信息
 *
 * @author fengjunhong
 * @since 2020-7-20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BidderCursorDto {

    /**
     * 投标人主键
     */
    Integer id;

    /**
     * 投标人名称
     */
    String bidderName;

    /**
     * 投标报价
     */
    String bidPrice;

    /**
     * 投标报价类型
     */
    String bidPriceType;

    /**
     * 投标保证金状态
     */
    String marginPay;

    /**
     * 投标工期
     */
    String timeLimit;

    /**
     * 工程质量
     */
    String quality;

    /**
     * 质询状态
     */
    String questionStatus;


}
