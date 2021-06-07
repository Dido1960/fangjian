package com.ejiaoyi.admin.enums;

import com.ejiaoyi.admin.quartz.CleanLogQuartzJob;
import com.ejiaoyi.admin.quartz.SysTimeQuartzJob;

/**
 * Quartz 定时任务组
 *
 * @author Kevin
 * @since 2020-05-20
 */
public enum QuartzJobs {

    // 系统定时清理日志
    SYS_CLEAN_LOG(CleanLogQuartzJob.class),
    // 自动更新服务器时间,防止服务器时间不准确
    SYS_TIME_QUARTZ_JOB(SysTimeQuartzJob.class) ;

    private final Class clazz;

    public Class getClazz() {
        return this.clazz;
    }

    QuartzJobs(Class clazz) {
        this.clazz = clazz;
    }
}
