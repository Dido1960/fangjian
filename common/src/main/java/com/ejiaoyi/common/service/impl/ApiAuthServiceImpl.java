package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.common.constant.CacheName;
import com.ejiaoyi.common.constant.Constants;
import com.ejiaoyi.common.entity.ApiAuth;
import com.ejiaoyi.common.mapper.ApiAuthMapper;
import com.ejiaoyi.common.service.IApiAuthService;
import com.ejiaoyi.common.service.IWordbookService;
import com.ejiaoyi.common.support.DataSourceKey;
import com.ejiaoyi.common.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * API接口认证信息 服务实现类
 * </p>
 *
 * @author Z0001
 * @since 2020-04-01
 */
@Service
@DS(DataSourceKey.COMMON)
public class ApiAuthServiceImpl extends BaseServiceImpl implements IApiAuthService {

    @Autowired
    private ApiAuthMapper apiAuthMapper;

    @Autowired
    private IWordbookService wordbookService;

    @Override
    public String pagedApiAuth(String apiName, String platform, String apiKey, String remark, Integer enabled) {
        Page page = this.getPageForLayUI();

        QueryWrapper<ApiAuth> apiAuthQueryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(apiName)) {
            apiAuthQueryWrapper.eq("API_NAME", apiName);
        }

        if (StringUtils.isNotEmpty(platform)) {
            apiAuthQueryWrapper.like("PLATFORM", platform);
        }

        if (StringUtils.isNotEmpty(apiKey)) {
            apiAuthQueryWrapper.like("API_KEY", apiKey);
        }

        if (StringUtils.isNotEmpty(remark)) {
            apiAuthQueryWrapper.like("REMARK", remark);
        }

        if (enabled != null) {
            apiAuthQueryWrapper.eq("ENABLED", enabled);
        }

        apiAuthQueryWrapper.orderByDesc("ID");

        List<ApiAuth> list = apiAuthMapper.selectPage(page, apiAuthQueryWrapper).getRecords();
        for (ApiAuth apiAuth : list) {
            String string = wordbookService.getValue("apiName", apiAuth.getApiName());
            apiAuth.setChineseApiName(string);
        }

        return this.initJsonForLayUI(list, (int) page.getTotal());
    }

    @Override
    public boolean addApiAuth(String apiName, String remark) {
        Assert.notEmpty(apiName, "param apiName can not be empty!");
        Assert.notEmpty(remark, "param remark can not be empty!");

        ApiAuth apiAuth = ApiAuth.builder()
                .apiName(apiName)
                .platform(UUID.randomUUID().toString())
                .apiKey(UUID.randomUUID().toString())
                .remark(remark)
                .enabled(Constants.ENABLED)
                .build();

        RedisUtil.hashSet(apiAuth.getApiName(), apiAuth.getPlatform(), apiAuth.getApiKey());

        return apiAuthMapper.insert(apiAuth) == 1;
    }

    @Override
    public boolean updateApiAuthEnabled(Integer id, Integer enabled) {
        Assert.notNull(id, "param id can not be null!");
        Assert.notNull(enabled, "param enabled can not be null!");

        ApiAuth apiAuth = apiAuthMapper.selectById(id);

        apiAuth.setEnabled(enabled);

        if (enabled.equals(Constants.ENABLED)) {
            RedisUtil.hashSet(apiAuth.getApiName(), apiAuth.getPlatform(), apiAuth.getApiKey());
        } else {
            RedisUtil.hashRemove(apiAuth.getApiName(), apiAuth.getPlatform());
        }

        return apiAuthMapper.updateById(apiAuth) == 1;
    }

    @Override
    @CachePut(value = CacheName.API_REDIS_CACHE, key = "#apiName+'-'+#platform+'-'+#apiKey")
    public boolean authentication(String apiName, String platform, String apiKey) {
        if (StringUtils.isEmpty(apiName) || StringUtils.isEmpty(platform) || StringUtils.isEmpty(apiKey)) {
            return false;
        }

        QueryWrapper<ApiAuth> apiAuthQueryWrapper = new QueryWrapper<ApiAuth>()
                .eq("API_NAME", apiName)
                .eq("PLATFORM", platform)
                .eq("ENABLED", Constants.ENABLED)
                .eq("API_KEY", apiKey);

        return apiAuthMapper.selectOne(apiAuthQueryWrapper) != null;
    }

    public Boolean deleteApiAuth(Integer[] ids) {
        apiAuthMapper.deleteBatchIds(Arrays.asList(ids));
        return true;
    }
}
