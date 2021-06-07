package com.ejiaoyi.common.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.common.entity.LoggingEvent;
import com.ejiaoyi.common.enums.TimeFormatter;
import com.ejiaoyi.common.mapper.LoggingEventMapper;
import com.ejiaoyi.common.service.IRuntimeLogService;
import com.ejiaoyi.common.support.DataSourceKey;
import com.ejiaoyi.common.util.DateTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * <p>
 * 运行日志处理逻辑
 * </p>
 *
 * @author fengjunhong
 * @date 2020/4/16
 */
@Service
@DS(DataSourceKey.LOG)
public class RuntimeLogServiceImpl extends BaseServiceImpl implements IRuntimeLogService {

    @Autowired
    LoggingEventMapper loggingEventMapper;

    @Override
    public String pagedRuntimeLog(String searchTime, String logLevel) {
        Page page = this.getPageForLayUI();

        QueryWrapper<LoggingEvent> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(searchTime)) {
             String[] timeArr = searchTime.split("~");
             String startTime = timeArr[0].trim();
             String endTime = timeArr[1].trim();
             Long startTimestamp = DateTimeUtil.DateTimeToTimestamp(startTime, TimeFormatter.YYYY_HH_DD_HH_MM_SS) * 1000;
             Long endTimestamp = DateTimeUtil.DateTimeToTimestamp(endTime, TimeFormatter.YYYY_HH_DD_HH_MM_SS) * 1000;
            queryWrapper.between("timestmp", startTimestamp, endTimestamp);
        }

        if (StringUtils.isNotEmpty(logLevel)) {
            queryWrapper.like("level_string", logLevel);
        }
        queryWrapper.orderByDesc("timestmp");

        List list = loggingEventMapper.selectPage(page, queryWrapper).getRecords();
        return this.initJsonForLayUI(list, (int) page.getTotal());
    }

    @Override
    public List<LoggingEvent> listRuntimeLog(long timdeDiff) {
        return loggingEventMapper.listRuntimeLog(timdeDiff);
    }

    @Override
    public boolean delRuntimeLog(long timeDiff) {
        try {
            // 获取timeDiff天前的时间戳
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, (int) timeDiff);
            //时间差
            // 按照时间段，查询运行日志事件list
            List<LoggingEvent> loggingEventList = listRuntimeLog(calendar.getTimeInMillis());

            if (loggingEventList.size() > 0) {
                // 执行删除
                int result = loggingEventMapper.deleteBatchIds(Arrays.asList(loggingEventList.getClass()));
                System.out.println(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
