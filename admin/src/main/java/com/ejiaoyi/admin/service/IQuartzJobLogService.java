package com.ejiaoyi.admin.service;

import com.ejiaoyi.common.entity.QuartzJobLog;

/**
 * Quartz定时任务执行日志 服务类
 *
 * @author Z0001
 * @since 2020-05-25
 */
public interface IQuartzJobLogService {

    /**
     * 添加Quartz定时任务执行日志
     *
     * @param quartzJobLog Quartz定时任务执行日志
     */
    void addLog(QuartzJobLog quartzJobLog);

    /**
     * 分页查询定时任务执行日志列表数据
     *
     * @param jobId 定时任务主键
     * @return 定时任务执行日志列表数据
     */
    String pagedQuartzJobLog(Integer jobId);


    /**
     * 清除15天之前的日志
     *
     * ****/
    void cleanLog( );
}
