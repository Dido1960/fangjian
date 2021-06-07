package com.ejiaoyi.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ejiaoyi.common.entity.LoggingEvent;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 日志事件表 Mapper 接口
 * </p>
 *
 * @author Z0001
 * @since 2020-04-03
 */
@Component
public interface LoggingEventMapper extends BaseMapper<LoggingEvent> {

    /**
     * 按照时间差，查询运行日志事件
     *
     * @param timdeDiff 时间差
     * @return 运行日志事件list
     */
    List<LoggingEvent> listRuntimeLog(@Param("timeDiff") long timdeDiff);


}
