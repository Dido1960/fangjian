package com.ejiaoyi.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评标报告 区块链上传 实体
 * @Auther: liuguoqiang
 * @Date: 2021-1-21 10:16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidEvalReportDTO extends BsnChainDTO {
    private static final long serialVersionUID = 1L;

    /**
     * 标段名称
     */
    private String noName;

    /**
     * 标段编号
     */
    private String noNum;

    /**
     * 开标时间
     */
    private String openBidTime;

    /**
     * 评标开始时间
     */
    private String bidEvalStartTime;

    /**
     * 招标企业
     */
    private String agencyName;

    /**
     * 评标报告hash
     */
    private String bidEvalReportHashCode;
}
