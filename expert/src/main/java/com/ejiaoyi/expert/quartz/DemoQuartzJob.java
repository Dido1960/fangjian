package com.ejiaoyi.expert.quartz;

import com.ejiaoyi.common.annotation.QuartzLog;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * quartz定时任务 示例代码
 *
 * @author Z0001
 * @since 2020-5-20
 */
public class DemoQuartzJob extends QuartzJobBean {

    @Override
    @QuartzLog
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //TODO 以下为定时任务 逻辑
        System.out.println("do demo quartz job");

    }

}
