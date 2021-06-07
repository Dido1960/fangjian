package com.ejiaoyi.admin.service;

import com.ejiaoyi.common.entity.QuartzJob;

import java.util.List;

/**
 * Quartz 定时任务 服务类
 *
 * @author Z0001
 * @since 2020-05-20
 */
public interface IQuartzService {

    /**
     * 将定时任务加入Quartz管理 安排进行处罚执行
     *
     * @param quartzJob Quartz定时任务
     * @throws Exception 异常
     */
    void schedulerJob(QuartzJob quartzJob) throws Exception;

    /**
     * 新增定时任务
     *
     * @param quartzJob Quartz定时任务
     * @return 是否成功
     */
    boolean addQuartzJob(QuartzJob quartzJob);

    /**
     * 执行定时任务
     *
     * @param jobName  定时任务名称
     * @param jobGroup 定时任务组
     * @return 是否成功
     */
    boolean triggerJob(String jobName, String jobGroup);

    /**
     * 暂停定时任务
     *
     * @param jobName  定时任务名称
     * @param jobGroup 定时任务组
     * @return 是否成功
     */
    boolean pausedJob(String jobName, String jobGroup);

    /**
     * 恢复定时任务
     *
     * @param jobName  定时任务名称
     * @param jobGroup 定时任务组
     * @return 是否成功
     */
    boolean resumeJob(String jobName, String jobGroup);

    /**
     * 删除定时任务
     *
     * @param jobName  定时任务名称
     * @param jobGroup 定时任务组
     * @return 是否成功
     */
    boolean delJob(String jobName, String jobGroup);

    /**
     * 分页查询Quartz定时任务列表
     *
     * @param name 定时任务名称
     * @return 定时任务列表
     */
    String pagedQuartzJob(String name);

    /**
     * 根据任务名称和任务组 获取QuartzJob对象
     *
     * @param jobName  任务名称
     * @param jobGroup 任务组
     * @return QuartzJob对象
     */
    QuartzJob getQuartzJob(String jobName, String jobGroup);

    /**
     * 根据任务主键 获取QuartzJob对象
     *
     * @param id 任务主键
     * @return QuartzJob对象
     */
    QuartzJob getQuartzJobById(Integer id);

    /**
     * 修改定时任务
     *
     * @param quartzJob 定时任务信息
     * @return 是否修改成功
     */
    boolean updateQuartzJob(QuartzJob quartzJob);

    /**
     * 查看启用的QuartzJob
     *
     * @return QuartzJob列表
     */
    List<QuartzJob> listQuartzJobEnabled();

}
