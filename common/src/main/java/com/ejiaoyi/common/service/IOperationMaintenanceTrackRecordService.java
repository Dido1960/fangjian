package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.OperationMaintenanceTrackRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 运维跟踪记录 服务类
 * </p>
 *
 * @author Mike
 * @since 2021-03-23
 */
public interface IOperationMaintenanceTrackRecordService extends IService<OperationMaintenanceTrackRecord> {

    /**
     * 通过标段id获取运维跟踪记录
     * @param bidSectionId 标段id
     * @return 运维跟踪记录
     */
    OperationMaintenanceTrackRecord getOperationMaintenanceBySectionId(Integer bidSectionId);
}
