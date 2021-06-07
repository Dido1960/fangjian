package com.ejiaoyi.admin.aop;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ejiaoyi.admin.service.impl.QuartzJobLogServiceImpl;
import com.ejiaoyi.admin.service.impl.QuartzServiceImpl;
import com.ejiaoyi.common.entity.QuartzJob;
import com.ejiaoyi.common.entity.QuartzJobLog;
import com.ejiaoyi.common.enums.TimeFormatter;
import com.ejiaoyi.common.support.DataSourceKey;
import com.ejiaoyi.common.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Quartz 定时任务日志
 *
 * @author Z0001
 * @since 2020-5-25
 */
@Slf4j
@Aspect
@Component
@DS(DataSourceKey.LOG)
public class QuartzLogAspect {

    @Autowired
    private QuartzServiceImpl quartzService;

    @Autowired
    private QuartzJobLogServiceImpl quartzJobLogService;

    @Pointcut("@annotation(com.ejiaoyi.common.annotation.QuartzLog)")
    public void quartzLog() {

    }

    @AfterThrowing(pointcut = "quartzLog()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        log.error("EXCEPTION: " + e.getMessage());
    }

    @Around("quartzLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] args = proceedingJoinPoint.getArgs();
        JobExecutionContext jobExecutionContext = (JobExecutionContext) args[0];

        JobKey jobKey = jobExecutionContext.getJobDetail().getKey();

        String jobName = jobKey.getName();
        String jobGroup = jobKey.getGroup();
        String className = proceedingJoinPoint.getTarget().getClass().getName();

        QuartzJob quartzJob = quartzService.getQuartzJob(jobName, jobGroup);

        QuartzJobLog quartzJobLog = QuartzJobLog.builder()
                .jobName(jobName)
                .jobGroup(jobGroup)
                .jobId(quartzJob.getId())
                .className(className)
                .jobTime(LocalDateTime.parse(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS), DateTimeFormatter.ofPattern(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode(), Locale.CHINA)))
                .build();

        quartzJobLogService.addLog(quartzJobLog);

        return proceedingJoinPoint.proceed();
    }
}
