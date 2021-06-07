package com.ejiaoyi.admin.controller;

import com.ejiaoyi.admin.enums.QuartzJobs;
import com.ejiaoyi.admin.service.impl.QuartzJobLogServiceImpl;
import com.ejiaoyi.admin.service.impl.QuartzServiceImpl;
import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.entity.QuartzJob;
import com.ejiaoyi.common.enums.DMLType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

/**
 * Quartz 定时任务 前端控制器
 *
 * @author Z0001
 * @since 2020-5-20
 */
@Slf4j
@RestController
@RequestMapping("/quartzJob")
public class QuartzJobController {

    @Autowired
    private QuartzServiceImpl quartzService;

    @Autowired
    private QuartzJobLogServiceImpl quartzJobLogService;

    /**
     * 跳转定时任务管理页面
     *
     * @return
     */
    @RequestMapping("/quartzJobPage")
    public ModelAndView quartzJobPage() {
        return new ModelAndView("/quartzJob/quartzJob");
    }

    /**
     * 分页查询定时任务列表数据
     *
     * @param name 定时任务名称
     * @return 定时任务列表数据
     */
    @RequestMapping("/pagedQuartzJob")
    public String pagedQuartzJob(String name) {
        return quartzService.pagedQuartzJob(name);
    }

    /**
     * 跳转新增定时任务页面
     *
     * @return
     */
    @RequestMapping("/addQuartzJobPage")
    public ModelAndView addQuartzJobPage() {
        List<QuartzJobs> quartzJobsList = Arrays.asList(QuartzJobs.values());

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/quartzJob/addQuartzJob");
        mav.addObject("quartzJobsList", quartzJobsList);
        return mav;
    }

    /**
     * 跳转修改定时任务页面
     *
     * @param id 定时任务主键
     * @return
     */
    @RequestMapping("/updateQuartzJobPage")
    public ModelAndView updateQuartzJobPage(@RequestParam Integer id) {
        List<QuartzJobs> quartzJobsList = Arrays.asList(QuartzJobs.values());
        QuartzJob quartzJob = quartzService.getQuartzJobById(id);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/quartzJob/updateQuartzJob");
        mav.addObject("quartzJobsList", quartzJobsList);
        mav.addObject("quartzJob", quartzJob);
        return mav;
    }

    /**
     * 检查QuartzJob是否存在
     *
     * @param name     任务名称
     * @param jobGroup 任务组
     * @return true: 存在 false: 不存在
     */
    @PostMapping("/validQuartzJob")
    public boolean validQuartzJob(@RequestParam String name, @RequestParam String jobGroup, Integer notId) {
        try {
            QuartzJob quartzJob = quartzService.getQuartzJob(name, jobGroup);
            return quartzJob != null && !quartzJob.getId().equals(notId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 新增定时任务
     *
     * @param quartzJob 定时任务信息
     * @return 新增是否成功
     */
    @PostMapping("/addQuartzJob")
    @UserLog(value = "'新增定时任务:'+#quartzJob.toString()", dmlType = DMLType.INSERT)
    public boolean addQuartzJob(QuartzJob quartzJob) {
        try {
            return quartzService.addQuartzJob(quartzJob);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 修改定时任务
     *
     * @param quartzJob 定时任务信息
     * @return 修改是否成功
     */
    @PostMapping("/updateQuartzJob")
    @UserLog(value = "'修改定时任务:'+#quartzJob.toString()", dmlType = DMLType.UPDATE)
    public boolean updateQuartzJob(QuartzJob quartzJob) {
        try {
            return quartzService.updateQuartzJob(quartzJob);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 执行定时任务
     *
     * @param id 定时任务主键
     * @return 是否成功执行定时任务
     */
    @PostMapping("/triggerQuartzJob")
    @UserLog(value = "'执行定时任务:'+#id")
    public boolean triggerQuartzJob(@RequestParam Integer id) {
        try {
            QuartzJob quartzJob = quartzService.getQuartzJobById(id);
            return quartzService.triggerJob(quartzJob.getName(), quartzJob.getJobGroup());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 暂停定时任务
     *
     * @param id 定时任务主键
     * @return 是否成功暂停定时任务
     */
    @PostMapping("/pausedQuartzJob")
    @UserLog(value = "'暂停定时任务:'+#id")
    public boolean pausedQuartzJob(@RequestParam Integer id) {
        try {
            QuartzJob quartzJob = quartzService.getQuartzJobById(id);
            return quartzService.pausedJob(quartzJob.getName(), quartzJob.getJobGroup());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 恢复定时任务
     *
     * @param id 定时任务主键
     * @return 是否成功恢复定时任务
     */
    @PostMapping("/resumeQuartzJob")
    @UserLog(value = "'恢复定时任务:'+#id")
    public boolean resumeQuartzJob(@RequestParam Integer id) {
        try {
            QuartzJob quartzJob = quartzService.getQuartzJobById(id);
            return quartzService.resumeJob(quartzJob.getName(), quartzJob.getJobGroup());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 删除定时任务
     *
     * @param id 定时任务主键
     * @return 是否成功删除定时任务
     */
    @PostMapping("/delQuartzJob")
    @UserLog(value = "'删除定时任务:'+#id")
    public boolean delQuartzJob(@RequestParam Integer id) {
        try {
            QuartzJob quartzJob = quartzService.getQuartzJobById(id);
            return quartzService.delJob(quartzJob.getName(), quartzJob.getJobGroup());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 跳转定时任务执行日志页面
     *
     * @return
     */
    @RequestMapping("/quartzJobLogPage")
    public ModelAndView quartzJobLogPage() {
        return new ModelAndView("/quartzJob/quartzJobLog");
    }

    /**
     * 分页查询定时任务执行日志列表数据
     *
     * @param jobId 定时任务主键
     * @return 定时任务执行日志列表数据
     */
    @RequestMapping("/pagedQuartzJobLog")
    public String pagedQuartzJobLog(@RequestParam Integer jobId) {
        return quartzJobLogService.pagedQuartzJobLog(jobId);
    }
}
