package com.ejiaoyi.agency.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 否决投标人
 * @author fengjunhong
 * @version 1.0
 * @date 2021-4-21 15:12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CancelBidderDTO {

    /**
     * 业务code
     */
    private String ywCode;

    /**
     * 	投标人Id列表
     */
    private  String bidderIds;
}
