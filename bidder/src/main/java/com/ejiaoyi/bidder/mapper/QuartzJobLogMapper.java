package com.ejiaoyi.bidder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ejiaoyi.common.entity.QuartzJobLog;
import org.springframework.stereotype.Component;

/**
 * Quartz定时任务执行日志 Mapper 接口
 *
 * @author Z0001
 * @since 2020-05-25
 */
@Component
public interface QuartzJobLogMapper extends BaseMapper<QuartzJobLog> {

}
