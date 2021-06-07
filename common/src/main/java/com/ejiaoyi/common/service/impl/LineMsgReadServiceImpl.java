package com.ejiaoyi.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.entity.LineMsg;
import com.ejiaoyi.common.entity.LineMsgRead;
import com.ejiaoyi.common.mapper.LineMsgReadMapper;
import com.ejiaoyi.common.service.ILineMsgReadService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ejiaoyi.common.service.ILineMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 网上开标消息阅读情况 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-08-07
 */
@Service
public class LineMsgReadServiceImpl extends BaseServiceImpl implements ILineMsgReadService {

    @Autowired
    private ILineMsgService lineMsgService;

    @Autowired
    private LineMsgReadMapper lineMsgReadMapper;

    @Override
    public Integer getUnReadCount(LineMsgRead lineMsgRead, Integer bidSectionId) {
        List<LineMsg> lineMsg = lineMsgService.listLineMsg(bidSectionId);
        List<Integer> lineMsgIds = new ArrayList<>();
        for (LineMsg msg : lineMsg) {
            lineMsgIds.add(msg.getId());
        }

        QueryWrapper<LineMsgRead> queryWrapper = new QueryWrapper();
        queryWrapper.eq("READ_SITUATION", 0);
        queryWrapper.eq("USER_TYPE", lineMsgRead.getUserType());
        queryWrapper.eq("USER_ID", lineMsgRead.getUserId());
        if (lineMsgIds.size() > 0) {
            queryWrapper.in("LINE_MSG_ID", lineMsgIds);
            return lineMsgReadMapper.selectCount(queryWrapper);
        }
        return null;
    }

    @Override
    public void updateUserLineMsgRead(Integer bidSectionId, Integer userId, Integer roleType) {
        List<LineMsg> lineMsg = lineMsgService.listLineMsg(bidSectionId);
        List<Integer> lineMsgIds = new ArrayList<>();
        for (LineMsg msg : lineMsg) {
            lineMsgIds.add(msg.getId());
        }
        QueryWrapper<LineMsgRead> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("READ_SITUATION", 0);
        queryWrapper.eq("USER_TYPE", roleType);
        queryWrapper.eq("USER_ID", userId);
        queryWrapper.in("LINE_MSG_ID", lineMsgIds);

        List<LineMsgRead> lineMsgReads = lineMsgReadMapper.selectList(queryWrapper);
        for (LineMsgRead lineMsgRead : lineMsgReads) {
            LineMsgRead read = LineMsgRead.builder()
                    .id(lineMsgRead.getId())
                    .readSituation(1)
                    .build();
            lineMsgReadMapper.updateById(read);
        }
    }
}
