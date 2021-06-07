package com.ejiaoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ejiaoyi.common.entity.QuartzJob;
import org.springframework.stereotype.Component;

/**
 * Quartz定义任务表 Mapper 接口
 *
 * @author Z0001
 * @since 2020-05-20
 */
@Component
public interface QuartzJobMapper extends BaseMapper<QuartzJob> {

}
