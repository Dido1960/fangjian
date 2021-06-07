package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.constant.UserType;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.mapper.*;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.websocket.OnlineWebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 网上开标消息 服务实现类
 *
 * @author Make
 * @date 2020/8/6 17:24
 */
@Service
public class LineMsgServiceImpl extends BaseServiceImpl implements ILineMsgService {

    @Autowired
    private LineMsgMapper lineMsgMapper;

    @Autowired
    private IFDFSService fdfsService;

    @Autowired
    private LineMsgReadMapper lineMsgReadMapper;

    @Autowired
    private IBidderService bidderService;

    @Autowired
    private IBidSectionService bidSectionService;

    @Autowired
    private ITenderProjectService tenderProjectService;

    @Override
    public boolean addLineMsg(LineMsg lineMsg) {
        int insert = lineMsgMapper.insert(lineMsg);
        if (insert > 0) {
            lineMsg = lineMsgMapper.selectById(lineMsg.getId());
            OnlineWebSocketServer.sendMessageToUser(lineMsg.getBidSectionId(), lineMsg);

            List<Bidder> bidders = bidderService.getBidder(null, lineMsg.getBidSectionId());

            //新增阅读情况
            Integer roleType = lineMsg.getRoleType();
            BidSection bidSection = bidSectionService.getBidSectionById(lineMsg.getBidSectionId());
            TenderProject tenderProject = tenderProjectService.getTenderProjectById(bidSection.getTenderProjectId());
            Integer agentId = tenderProject.getUserId();
            LineMsgRead lineMsgRead = LineMsgRead.builder()
                    .lineMsgId(lineMsg.getId())
                    .userId(agentId)
                    .userType(UserType.AGENT)
                    .build();
            Bidder currentBidder = null;
            if (UserType.BIDDER.equals(roleType)) {
                currentBidder = bidderService.getBidderById(lineMsg.getBidderId());
            }
            if (UserType.AGENT.equals(roleType)) {
                //设置发布消息的代理 已读
                lineMsgRead.setReadSituation(1);
            }
            //1.添加代理阅读情况
            lineMsgReadMapper.insert(lineMsgRead);
            //2.添加投标人阅读情况
            for (Bidder bidder : bidders) {
                lineMsgRead.setReadSituation(0);
                //设置发布消息的投标人 已读（默认未读）
                if (!CommonUtil.isEmpty(currentBidder) && currentBidder.getBidderOrgCode().equals(bidder.getBidderOrgCode())) {
                    lineMsgRead.setReadSituation(1);
                }
                lineMsgRead.setUserId(bidder.getId());
                lineMsgRead.setUserType(UserType.BIDDER);
                lineMsgReadMapper.insert(lineMsgRead);
            }

        }

        return insert > 0;
    }

    @Override
    public List<LineMsg> listLineMsg(Integer bidSectionId) {
        // 获取分页对象
        List<LineMsg> list = lineMsgMapper.listLineMsg(bidSectionId);
        for (LineMsg lineMsg1 : list) {
            if (!CommonUtil.isEmpty(lineMsg1.getBidderId())) {
                if (!CommonUtil.isEmpty(lineMsg1.getObjectionFileId())) {
                    String objectionFile = fdfsService.getUrlByUpload(lineMsg1.getObjectionFileId());
                    lineMsg1.setObjectionUrl(objectionFile);
                }
            }
        }
        return list;
    }

    @Override
    public boolean updateLineMsg(LineMsg lineMsg) {
        Assert.notNull(lineMsg, "param lineMsg can not be null!");
        Assert.notNull(lineMsg.getId(), "param id can not be null!");
        return lineMsgMapper.updateById(lineMsg) > 0;
    }


    @Override
    public LineMsg getLastLineMsg(Integer bidSectionId) {
        return lineMsgMapper.getLastLineMsg(bidSectionId);
    }


    @Override
    public String sureBidResult(Integer bidSectionId, Integer bidderId) {
        QueryWrapper<LineMsg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", bidSectionId);
        queryWrapper.eq("BIDDER_ID", bidderId);

        return lineMsgMapper.selectList(queryWrapper).size() > 0 ? "已确认" : "未确认";
    }

    @Override
    public List<LineMsg> listDissentLineMsg(LineMsg lineMsg) {
        QueryWrapper<LineMsg> wrapper = new QueryWrapper<>();
        wrapper.eq("BID_SECTION_ID", lineMsg.getBidSectionId());
        wrapper.eq("BIDDER_ID", lineMsg.getBidderId());
        wrapper.eq("ROLE_TYPE", 1);
        wrapper.eq("QUESTION", lineMsg.getQuestion());

        return lineMsgMapper.selectList(wrapper);
    }

}
