package com.ejiaoyi.admin.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.admin.mapper.QuartzJobLogMapper;
import com.ejiaoyi.admin.service.IQuartzJobLogService;
import com.ejiaoyi.common.annotation.RedissonLock;
import com.ejiaoyi.common.entity.QuartzJobLog;
import com.ejiaoyi.common.service.impl.BaseServiceImpl;
import com.ejiaoyi.common.support.DataSourceKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Quartz定时任务执行日志 服务实现类
 *
 * @author Z0001
 * @since 2020-05-25
 */
@Service
@DS(DataSourceKey.LOG)
public class QuartzJobLogServiceImpl extends BaseServiceImpl implements IQuartzJobLogService {

    @Autowired
    private QuartzJobLogMapper quartzJobLogMapper;

    @Override
    public void addLog(QuartzJobLog quartzJobLog) {
        quartzJobLogMapper.insert(quartzJobLog);
    }

    @Override
    public String pagedQuartzJobLog(Integer jobId) {
        Page page = this.getPageForLayUI();

        QueryWrapper<QuartzJobLog> quartzJobLogQueryWrapper = new QueryWrapper<QuartzJobLog>()
                .eq("JOB_ID", jobId);

        List<QuartzJobLog> quartzJobLogList = quartzJobLogMapper.selectPage(page, quartzJobLogQueryWrapper).getRecords();

        return this.initJsonForLayUI(quartzJobLogList, (int) page.getTotal());
    }

    @Override
    @RedissonLock
    public void cleanLog() {
        quartzJobLogMapper.cleanApiLog();

        quartzJobLogMapper.cleanLoggingEvent();

        quartzJobLogMapper.cleanNetworkLog();

        quartzJobLogMapper.cleanQuartzJobLog();

        quartzJobLogMapper.cleanUserLog();
       quartzJobLogMapper.logUpdateIdAsc();
    }

}
