package com.ejiaoyi.bidder.service;

import com.ejiaoyi.common.entity.NetworkLog;

/**
 * <p>
 * 网络日志 服务类
 * </p>
 *
 * @author fengjunhong
 * @since 2020-03-25
 */
public interface INetworkService {


    /**
     * 网络日志写入
     *
     * @param networkLog 网络日志
     */
    void addLog(NetworkLog networkLog);

    /**
     * 查询系统访问日志列表
     *
     * @param userName       用户名
     * @param searchTime     日志时限
     * @param requestURI     请求地址
     * @param requestMethod  请求方法
     * @param processingTime 处理耗时
     * @return 系统访问日志列表
     */
    String pagedWebLog(String userName, String searchTime, String requestURI, String requestMethod, String processingTime);

}
