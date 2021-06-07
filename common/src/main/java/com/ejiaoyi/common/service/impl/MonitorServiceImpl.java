package com.ejiaoyi.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.common.entity.Monitor;
import com.ejiaoyi.common.mapper.MonitorMapper;
import com.ejiaoyi.common.service.IMonitorService;
import com.ejiaoyi.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @Description:
 * @Auther: liuguoqiang
 * @Date: 2020/7/14 09:42
 */
@Service
public class MonitorServiceImpl extends BaseServiceImpl implements IMonitorService {
    @Autowired
    private MonitorMapper monitorMapper;

    /**
     * 分页查询
     * @param monitor
     * @return
     */
    @Override
    public String pagedMonitor(Monitor monitor) {
        Page page = this.getPageForLayUI();
        List<Monitor> list = monitorMapper.pagedMonitor(page,monitor);
        return this.initJsonForLayUI(list,(int)page.getTotal());
    }

    @Override
    public Integer updateMonitor(Monitor monitor) {
        return monitorMapper.updateById(monitor);
    }

    @Override
    public Integer addMonitor(Monitor monitor) {
        monitor.setEnabled(1);
        return monitorMapper.insert(monitor);
    }

    @Override
    public Integer deleteMonitor(Integer[] ids) {
        return monitorMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public Monitor getMonitorById(Integer id) {
        return monitorMapper.selectById(id);
    }

    @Override
    public Monitor getMonitorByRegCode(String regCode) {
        if (CommonUtil.isEmpty(regCode)) {
            return null;
        }

        QueryWrapper<Monitor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("REG_CODE", regCode);
        queryWrapper.eq("ENABLED", 1);
        return monitorMapper.selectOne(queryWrapper);
    }
}
