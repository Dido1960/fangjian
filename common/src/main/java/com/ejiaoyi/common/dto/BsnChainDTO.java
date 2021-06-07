package com.ejiaoyi.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 上传区块链 基础参数
 * @Auther: liuguoqiang
 * @Date: 2021-1-21 10:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BsnChainDTO {
    private static final long serialVersionUID = 1L;

    /**
     * 平台授权码
     */
    private String platform;

    /**
     * API_KEY
     */
    private String api_key;

    /**
     * token
     */
    private String token;

}
