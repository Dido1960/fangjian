package com.ejiaoyi.common.dto;

import lombok.*;

import java.io.Serializable;

/**
 * 投标人排名结果汇总表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BidderRankingDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 标段id
     */
    private Integer bidSectionId;

    /**
     * 企业Id
     */
    private Integer bidderId;

    /**
     * 票数
     */
    private Integer poll;
}
