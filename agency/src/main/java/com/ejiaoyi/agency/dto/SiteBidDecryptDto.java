package com.ejiaoyi.agency.dto;

import com.ejiaoyi.common.entity.Bidder;
import com.ejiaoyi.common.entity.BidderFileInfo;
import com.ejiaoyi.common.entity.BidderOpenInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 现场开标解密提示信息
 *
 * @author Mike
 * @since 2021-01-07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SiteBidDecryptDto {

    /**
     * 解密状态
     */
    boolean decryptStatus;

    /**
     * 名称是否一致
     */
    boolean nameConsistentStatus;

    /**
     * 文件解析后投标人名称
     */
    String fileBidderName;

    /**
     * 数据库投标人名称
     */
    String dataBaseBidderName;

    /**
     * 解密信息
     */
    String decryptMsg;

    /**
     * 待更新的投标人信息
     */
    Bidder updateBidder;

    /**
     * 待更新的投标人开标信息
     */
    BidderOpenInfo updateBidderOpenInfo;

    /**
     * 待更新的投标人文件信息
     */
    BidderFileInfo updateBidderFileInfo;

    /**
     * 是否为施工
     */
    boolean isConstruction;

}
