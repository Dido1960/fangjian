package com.ejiaoyi.api.service.impl;

import com.ejiaoyi.api.constant.Constants;
import com.ejiaoyi.api.dto.GetToken;
import com.ejiaoyi.api.service.IBaseService;
import com.ejiaoyi.common.constant.CacheName;
import com.ejiaoyi.common.util.DesUtil;
import com.ejiaoyi.common.util.RedisUtil;

import java.util.UUID;

/**
 * 基础 服务实现类
 *
 * @author Z0001
 * @since 2020-4-21
 */
public class BaseServiceImpl implements IBaseService {

    @Override
    public String getToken(GetToken getToken) {
        String uuid = UUID.randomUUID().toString();

        //String encrypt = DesUtil.encrypt(getToken.getApiKey() + "-" + getToken.getPlatform());
        RedisUtil.set(CacheName.TOKEN+uuid , "1", Constants.TOKEN_TIME);
        //RedisUtil.set(uuid, encrypt, Constants.TOKEN_TIME );

        return uuid;
    }
}
