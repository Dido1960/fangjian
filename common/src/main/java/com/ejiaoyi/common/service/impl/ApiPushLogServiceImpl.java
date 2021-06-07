package com.ejiaoyi.common.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ejiaoyi.common.entity.ApiPushLog;
import com.ejiaoyi.common.mapper.ApiPushLogMapper;
import com.ejiaoyi.common.service.IApiPushLogService;
import com.ejiaoyi.common.support.DataSourceKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * API推送日志 实现类
 *
 * @author Kevin
 * @since 2020-10-28
 */
@DS(DataSourceKey.LOG)
@Slf4j
@Service
public class ApiPushLogServiceImpl extends ServiceImpl<ApiPushLogMapper, ApiPushLog> implements IApiPushLogService {

    @Autowired
    private ApiPushLogMapper apiPushLogMapper;

    @Override
    public void addLog(ApiPushLog apiPushLog) {
        apiPushLogMapper.insert(apiPushLog);
    }

    @Override
    public void updateLogById(ApiPushLog apiPushLog) {
        apiPushLogMapper.updateById(apiPushLog);
    }
}
