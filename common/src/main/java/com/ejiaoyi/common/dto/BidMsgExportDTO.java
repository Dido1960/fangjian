package com.ejiaoyi.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息记录 区块链上传 实体
 * @Auther: liuguoqiang
 * @Date: 2021-1-21 10:16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidMsgExportDTO extends BsnChainDTO  {
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
     * 招标企业
     */
    private String agencyName;

    /**
     * 消息记录hash
     */
    private String bidMsgExportHashCode;
}
