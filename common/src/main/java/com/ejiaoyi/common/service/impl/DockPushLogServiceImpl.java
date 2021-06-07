package com.ejiaoyi.common.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ejiaoyi.common.entity.DockPushLog;
import com.ejiaoyi.common.mapper.DockPushLogMapper;
import com.ejiaoyi.common.service.IDockPushLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ejiaoyi.common.support.DataSourceKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 对接推送日志 服务实现类
 * </p>
 *
 * @author Mike
 * @since 2020-12-31
 */
@DS(DataSourceKey.LOG)
@Slf4j
@Service
public class DockPushLogServiceImpl extends ServiceImpl<DockPushLogMapper, DockPushLog> implements IDockPushLogService {

}
