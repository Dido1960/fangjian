package com.ejiaoyi.admin.quartz ;

import com.ejiaoyi.admin.service.IQuartzJobLogService;
import com.ejiaoyi.admin.service.IQuartzService;
import com.ejiaoyi.admin.service.impl.QuartzServiceImpl;
import com.ejiaoyi.common.annotation.QuartzLog;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;


/**
 * 定时删除日志
 *
 * @author Legsod
 * @since 2020-12-13
 */
@Slf4j
@Component
public class CleanLogQuartzJob extends QuartzJobBean {


    @Autowired
    private IQuartzJobLogService   quartzService;


    @Override
    @QuartzLog
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        quartzService.cleanLog( );
    }


}
