package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.constant.UserType;
import com.ejiaoyi.common.dto.MessageDTO;
import com.ejiaoyi.common.entity.Bidder;
import com.ejiaoyi.common.mapper.BidderMapper;
import com.ejiaoyi.common.service.IMessageService;
import com.ejiaoyi.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 消息
 * </p>
 *
 * @author Make
 * @since 2020-10-15
 */
@Service
public class MessageServiceImpl implements IMessageService {

    @Autowired
    private BidderMapper bidderMapper;

    @Override
    public void setMessageToRedis(Integer bidSectionId, String message, Integer userType, Integer messageType) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null!");
        Assert.notNull(userType, "param userType can not be null!");
        Assert.notNull(messageType, "param messageType can not be null!");

        QueryWrapper<Bidder> wrapper = new QueryWrapper<>();
        wrapper.eq("BID_SECTION_ID", bidSectionId);
        wrapper.eq("DELETE_FLAG", 0);
        // 获取需要推送的消息投标人
        List<Bidder> bidders = bidderMapper.selectList(wrapper);

        String key;
        if (UserType.SYSTEM.equals(userType)) {
            // 系统消息推送给投标人
            for (Bidder bidder : bidders) {
                key = "BID_OPEN_MESSAGE_BIDDER:" + bidder.getId();
                MessageDTO messageDTO = MessageDTO.builder()
                        .roleType(messageType)
                        .message(message)
                        .build();
                RedisUtil.set(key, messageDTO);
            }
        } else if (UserType.BIDDER.equals(userType)) {
            // 投标人消息推送给代理
            key = "BID_OPEN_MESSAGE_AGENCY:" + bidSectionId;
            MessageDTO messageDTO = MessageDTO.builder()
                    .roleType(messageType)
                    .message(message)
                    .build();
            RedisUtil.set(key, messageDTO);
        } else if (UserType.AGENT.equals(userType)) {
            //TODO 代理消息推送根据后期需要完善
        }
    }

    @Override
    public MessageDTO getUserMessage(String key) {
        return (MessageDTO) RedisUtil.get(key);
    }

    @Override
    public void deleteNewMessage(String key) {
        RedisUtil.delete(key);
    }

}
