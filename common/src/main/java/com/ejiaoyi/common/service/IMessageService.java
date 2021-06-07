package com.ejiaoyi.common.service;

import com.ejiaoyi.common.dto.MessageDTO;

/**
 * <p>
 * 消息
 * </p>
 *
 * @author Make
 * @since 2020-08-20
 */
public interface IMessageService {

    /**
     * 设置消息在缓存中
     * @param bidSectionId 标段
     * @param message 消息
     * @param messageType 消息类型
     */
    void setMessageToRedis(Integer bidSectionId, String message, Integer userType, Integer messageType);

    /**
     * 通过key获取消息
     * @param key rediskey
     * @return
     */
    MessageDTO getUserMessage(String key);

    /**
     * 通过key删除消息
     * @param key rediskey
     * @return
     */
    void deleteNewMessage(String key);
}
