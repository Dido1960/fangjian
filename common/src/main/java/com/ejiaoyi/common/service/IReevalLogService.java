package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.BidSection;
import com.ejiaoyi.common.entity.ReevalLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 复议日志 服务类
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
public interface IReevalLogService extends IService<ReevalLog> {

    /**
     * 项目复议日志列表
     * @param bidSectionId 标段列表
     * @return
     */
    List<ReevalLog> listLogByBidSectionId(Integer bidSectionId);

    /**
     * 数据插入
     * @param reevalLog
     * @return
     */
    Boolean insertLog(ReevalLog reevalLog);
}
