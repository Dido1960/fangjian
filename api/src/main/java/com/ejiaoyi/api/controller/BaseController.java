package com.ejiaoyi.api.controller;

import com.ejiaoyi.api.dto.GetToken;
import com.ejiaoyi.api.dto.RestResultVO;

/**
 * Api 基础控制层
 *
 * @author Z0001
 * @since 2020/3/24
 */
public interface BaseController {

    /**
     * 获取Token
     *
     * @param getToken GetTokenDTO
     * @return Token
     */
   String getToken(GetToken getToken);
}
