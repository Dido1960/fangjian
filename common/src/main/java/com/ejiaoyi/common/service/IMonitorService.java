package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.Monitor;

/**
 * @Description:
 * @Auther: liuguoqiang
 * @Date: 2020/7/14 09:42
 */
public interface IMonitorService {
    /**
     * 分页查询
     * @param monitor
     * @return
     */
    String pagedMonitor(Monitor monitor);

    /**
     * 更新
     * @param monitor
     * @return
     */
    Integer updateMonitor(Monitor monitor);

    /**
     * 添加
     * @param monitor
     * @return
     */
    Integer addMonitor(Monitor monitor);

    /**
     * 删除
     * @param ids
     * @return
     */
    Integer deleteMonitor(Integer[] ids);

    /**
     * 通过id获取
     * @param id
     * @return
     */
    Monitor getMonitorById(Integer id);

    /**
     * 通过regCode获取
     * @param regCode 区划代码
     * @return
     */
    Monitor getMonitorByRegCode(String regCode);
}
