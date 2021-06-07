package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.ApiPushLog;

/**
 * API推送日志 服务类
 *
 * @author Kevin
 * @since 2020-10-28
 */
public interface IApiPushLogService {

    /**
     * 插入API推送日志
     *
     * @param apiPushLog API推送日志
     */
    void addLog(ApiPushLog apiPushLog);

    /**
     * 更新API推送日志
     *
     * @param apiPushLog API推送日志
     */
    void updateLogById(ApiPushLog apiPushLog);
}
