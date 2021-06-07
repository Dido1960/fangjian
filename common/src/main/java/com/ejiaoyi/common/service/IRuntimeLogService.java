package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.LoggingEvent;

import java.util.List;

/**
 * <p>
 * 运行日志 服务类
 * </p>
 *
 * @author fengjunhong
 * @since 2020-03-25
 */
public interface IRuntimeLogService {

    /**
     * 获取运行日志列表
     *
     * @param searchTime 搜索时间
     * @param logLevel   日志级别
     * @return
     */
    String pagedRuntimeLog(String searchTime, String logLevel);

    /**
     * 按照时间差，查询运行日志事件
     *
     * @param timdeDiff 时间差
     * @return 运行日志事件list
     */
    List<LoggingEvent> listRuntimeLog(long timdeDiff);

    boolean delRuntimeLog(long timeDiff);
}
