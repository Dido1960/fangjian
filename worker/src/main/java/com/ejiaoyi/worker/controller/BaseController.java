package com.ejiaoyi.worker.controller;

import com.ejiaoyi.common.dto.WebSocketMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 基础 Controller
 *
 * @author Z0001
 * @since 2020-03-17
 */
public abstract class BaseController {

    @Autowired(required = false)
    private SimpMessagingTemplate simpMessagingTemplate;

    /**
     * 操作消息保存的key值
     */
    private static final String DESTINATION = "global_socket";

    /**
     * 获取sessionId
     *
     * @return sessionId
     */
    protected String getSid() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getSession().getId();
    }

    /**
     * 发送默认提示信息
     */
    protected void sendInfoWebSocketMessage() {
        simpMessagingTemplate.convertAndSendToUser(getSid(), DESTINATION, new WebSocketMessage(WebSocketMessage.Type.INFO, WebSocketMessage.DEFAULT_INFO_MSG));
    }

    /**
     * 发送默认的失败信息
     */
    protected void sendErrorWebSocketMessage() {
        simpMessagingTemplate.convertAndSendToUser(getSid(), DESTINATION, new WebSocketMessage(WebSocketMessage.Type.ERROR, WebSocketMessage.DEFAULT_ERROR_MSG));
    }

    /**
     * 发送操作成功消息
     *
     * @param content 内容
     */
    protected void sendInfoWebSocketMessage(String content) {
        simpMessagingTemplate.convertAndSendToUser(getSid(), DESTINATION, new WebSocketMessage(WebSocketMessage.Type.INFO, content));
    }

    /**
     * 发送操作成功消息
     *
     * @param content 内容
     * @param dialog  弹框方式
     */
    protected void addInfoActionMessage(String content, WebSocketMessage.Dialog dialog) {
        simpMessagingTemplate.convertAndSendToUser(getSid(), DESTINATION, new WebSocketMessage(WebSocketMessage.Type.INFO, content, dialog));
    }

    /**
     * 发送操作失败消息
     *
     * @param content 内容
     */
    protected void addErrorActionMessage(String content) {
        simpMessagingTemplate.convertAndSendToUser(getSid(), DESTINATION, new WebSocketMessage(WebSocketMessage.Type.ERROR, content));
    }

    /**
     * 发送操作失败消息
     *
     * @param content 内容
     * @param dialog  弹框方式
     */
    protected void addErrorActionMessage(String content, WebSocketMessage.Dialog dialog) {
        simpMessagingTemplate.convertAndSendToUser(getSid(), DESTINATION, new WebSocketMessage(WebSocketMessage.Type.ERROR, content, dialog));
    }
}
