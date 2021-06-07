package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.entity.BidSection;
import com.ejiaoyi.common.entity.ReevalLog;
import com.ejiaoyi.common.mapper.ReevalLogMapper;
import com.ejiaoyi.common.service.IReevalLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 复议日志 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
@Service
public class ReevalLogServiceImpl extends ServiceImpl<ReevalLogMapper, ReevalLog> implements IReevalLogService {

    @Autowired
    private ReevalLogMapper reevalLogMapper;

    @Override
    public List<ReevalLog> listLogByBidSectionId(Integer bidSectionId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null");
        QueryWrapper<ReevalLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", bidSectionId);
        return reevalLogMapper.selectList(queryWrapper);
    }

    @Override
    public Boolean insertLog(ReevalLog reevalLog) {
        return reevalLogMapper.insert(reevalLog) == 1;
    }
}
