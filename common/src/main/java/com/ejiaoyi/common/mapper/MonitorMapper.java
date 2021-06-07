package com.ejiaoyi.common.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.common.entity.Monitor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
* 监控信息 Mapper 接口
*
* @author Z0001
* @since 2020-07-03
*/
@Component
public interface MonitorMapper extends BaseMapper<Monitor> {

    List<Monitor> pagedMonitor(Page page, @Param("monitor") Monitor monitor);
}
