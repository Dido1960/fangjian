package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.entity.OperationMaintenanceTrackRecord;
import com.ejiaoyi.common.mapper.OperationMaintenanceTrackRecordMapper;
import com.ejiaoyi.common.service.IOperationMaintenanceTrackRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 运维跟踪记录 服务实现类
 * </p>
 *
 * @author Mike
 * @since 2021-03-23
 */
@Service
public class OperationMaintenanceTrackRecordServiceImpl extends ServiceImpl<OperationMaintenanceTrackRecordMapper, OperationMaintenanceTrackRecord> implements IOperationMaintenanceTrackRecordService {

    @Autowired
    private OperationMaintenanceTrackRecordMapper operationMaintenanceTrackRecordMapper;

    @Override
    public OperationMaintenanceTrackRecord getOperationMaintenanceBySectionId(Integer bidSectionId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null!");
        QueryWrapper<OperationMaintenanceTrackRecord> wrapper = new QueryWrapper<>();
        wrapper.eq(bidSectionId != null, "BID_SECTION_ID", bidSectionId);
        return operationMaintenanceTrackRecordMapper.selectOne(wrapper);
    }
}
