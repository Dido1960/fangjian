package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.ApiLog;

/**
 * API日志 服务类接口
 *
 * @author Z0001
 * @since 2020-03-18
 */
public interface IApiLogService {

    /**
     * 记录API日志
     *
     * @param apiLog API日志信息
     * @return API日志主键
     */
    Integer addLog(ApiLog apiLog);

    /**
     * 更新API日志
     *
     * @param apiLog API日志信息
     */
    void updateLog(ApiLog apiLog);

    /**
     * 获取Api日志列表
     *
     * @param methodName   请求方法名
     * @param searchTime   搜索时间
     * @param param        请求参数内容
     * @param platform     平台授权码
     * @param apiKey       API授权码
     * @param responseTime 响应时间
     * @return Api日志列表
     */
    String pagedApiLog(String methodName, String searchTime, String param, String platform, String apiKey, String responseTime);

}
