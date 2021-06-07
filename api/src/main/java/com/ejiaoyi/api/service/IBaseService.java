package com.ejiaoyi.api.service;

import com.ejiaoyi.api.dto.GetToken;

/**
 * 基础 服务接口类
 *
 * @author Z0001
 * @since 2020-4-21
 */
public interface IBaseService {

    /**
     * 获取Token
     *
     * @param getToken GetToken DTO
     * @return Token
     */
    String getToken(GetToken getToken);
}
