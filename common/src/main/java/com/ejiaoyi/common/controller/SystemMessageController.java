package com.ejiaoyi.common.controller;

import com.ejiaoyi.common.dto.WebSocketMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 系统消息 前端控制器
 * </p>
 *
 * @author Make
 * @since 2020-08-06
 */
@RestController
@RequestMapping("/common/systemMessage")
public class SystemMessageController {

    /**
     * 前台页面响应提示信息
     */
    private static final String PAGE_RESPONSE_MESSAGE = "page_response_message";

    /**
     * 获取最新消息
     *
     * @return 消息实体类
     */
    @RequestMapping("/getNewMessage")
    public WebSocketMessage saveProjectInfo(HttpServletRequest request) {
        return (WebSocketMessage)request.getSession().getAttribute(PAGE_RESPONSE_MESSAGE);
    }

    /**
     * 删除提示消息
     *
     */
    @RequestMapping("/removeMessage")
    public void removeMessage(HttpServletRequest request) {
        request.getSession().removeAttribute(PAGE_RESPONSE_MESSAGE);
    }
}
