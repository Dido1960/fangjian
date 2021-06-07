package com.ejiaoyi.admin.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.admin.mapper.QuartzJobMapper;
import com.ejiaoyi.admin.service.IQuartzService;
import com.ejiaoyi.common.constant.Constants;
import com.ejiaoyi.common.entity.QuartzJob;
import com.ejiaoyi.common.enums.QuartzState;
import com.ejiaoyi.common.service.impl.BaseServiceImpl;
import com.ejiaoyi.common.support.DataSourceKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Quartz定时任务 实现类
 *
 * @author Z0001
 * @since 2020-5-20
 */
@Slf4j
@Component
@DS(DataSourceKey.COMMON)
public class QuartzServiceImpl extends BaseServiceImpl implements IQuartzService {

    private static final String TRIGGER_PREFIX = "trigger";

    @Autowired
    private QuartzJobMapper quartzJobMapper;

    @Autowired
    private Scheduler scheduler;

    @Override
    public void schedulerJob(QuartzJob quartzJob) throws Exception {
        // 构建 JOB
        Class clazz = Class.forName(quartzJob.getJobClassName());

        JobDetail jobDetail = JobBuilder.newJob(clazz)
                .withIdentity(quartzJob.getName(), quartzJob.getJobGroup())
                .withDescription(quartzJob.getDescription())
                .build();


        // 创建触发规则
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(quartzJob.getCronExpression().trim());
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(TRIGGER_PREFIX + quartzJob.getName(), quartzJob.getJobGroup())
                .startNow()
                .withSchedule(cronScheduleBuilder)
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }

    @Override
    public boolean addQuartzJob(QuartzJob quartzJob) {
        try {
            this.schedulerJob(quartzJob);

            quartzJob.setEnabled(Constants.ENABLED);
            quartzJob.setTriggerState(QuartzState.RUNNING.getState());
            quartzJob.setOldName(quartzJob.getName());
            quartzJob.setOldJobGroup(quartzJob.getJobGroup());

            quartzJobMapper.insert(quartzJob);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean triggerJob(String jobName, String jobGroup) {
        JobKey jobKey = new JobKey(jobName, jobGroup);

        try {
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            log.error(e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean pausedJob(String jobName, String jobGroup) {
        JobKey jobKey = new JobKey(jobName, jobGroup);

        try {
            scheduler.pauseJob(jobKey);

            this.updateQuartzJobState(jobName, jobGroup, QuartzState.PAUSED);
        } catch (SchedulerException e) {
            log.error(e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean resumeJob(String jobName, String jobGroup) {
        JobKey jobKey = new JobKey(jobName, jobGroup);

        try {
            scheduler.resumeJob(jobKey);

            this.updateQuartzJobState(jobName, jobGroup, QuartzState.RUNNING);
        } catch (SchedulerException e) {
            log.error(e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean delJob(String jobName, String jobGroup) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(TRIGGER_PREFIX + jobName, jobGroup);
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));

            this.delQuartzJob(jobName, jobGroup);
        } catch (SchedulerException e) {
            log.error(e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public String pagedQuartzJob(String name) {
        Page page = this.getPageForLayUI();

        QueryWrapper<QuartzJob> quartzJobQueryWrapper = new QueryWrapper<QuartzJob>()
                .eq("ENABLED", Constants.ENABLED);

        if (StringUtils.isNotEmpty(name)) {
            quartzJobQueryWrapper.like("NAME", name);
            quartzJobQueryWrapper.orderByDesc("ID");
        }

        List<QuartzJob> quartzJobs = quartzJobMapper.selectPage(page, quartzJobQueryWrapper).getRecords();

        return this.initJsonForLayUI(quartzJobs, (int) page.getTotal());
    }

    @Override
    public QuartzJob getQuartzJob(String jobName, String jobGroup) {
        QueryWrapper<QuartzJob> quartzJobQueryWrapper = new QueryWrapper<QuartzJob>()
                .eq("NAME", jobName)
                .eq("JOB_GROUP", jobGroup)
                .eq("ENABLED", Constants.ENABLED);

        return quartzJobMapper.selectOne(quartzJobQueryWrapper);
    }

    @Override
    public QuartzJob getQuartzJobById(Integer id) {
        return quartzJobMapper.selectById(id);
    }

    @Override
    public boolean updateQuartzJob(QuartzJob quartzJob) {
        return quartzJobMapper.updateById(quartzJob) == 1;
    }

    @Override
    public List<QuartzJob> listQuartzJobEnabled() {
        QueryWrapper<QuartzJob> quartzJobQueryWrapper = new QueryWrapper<QuartzJob>()
                .eq("ENABLED", Constants.ENABLED);

        return quartzJobMapper.selectList(quartzJobQueryWrapper);
    }

    /**
     * 更新QuartzJob定时任务状态
     *
     * @param jobName     定时任务名称
     * @param jobGroup    定时任务组
     * @param quartzState QuartzState定时任务状态枚举
     */
    private void updateQuartzJobState(String jobName, String jobGroup, QuartzState quartzState) {
        QuartzJob quartzJob = this.getQuartzJob(jobName, jobGroup);

        if (quartzJob != null) {
            quartzJob.setTriggerState(quartzState.getState());
            quartzJobMapper.updateById(quartzJob);
        }
    }

    /**
     * 删除QuartzJob定时任务
     *
     * @param jobName  定时任务名称
     * @param jobGroup 定时任务组
     */
    private void delQuartzJob(String jobName, String jobGroup) {
        QuartzJob quartzJob = this.getQuartzJob(jobName, jobGroup);

        if (quartzJob != null) {
            quartzJob.setEnabled(Constants.DISABLED);
            quartzJobMapper.updateById(quartzJob);
        }
    }
}