package com.ejiaoyi.common.websocket;

import com.alibaba.fastjson.JSON;
import com.ejiaoyi.common.config.CustomSpringConfigurator;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 不见面开标WebSocket配置类
 *
 * @author Make
 * @since 2020-08-06
 */
@ServerEndpoint(value = "/online/{bidSectionId}", configurator = CustomSpringConfigurator.class)
@Component
@Slf4j
public class OnlineWebSocketServer {
    /**
     * 日志记录
     */
    private Logger logger = LoggerFactory.getLogger(OnlineWebSocketServer.class);

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static Map<Integer, Set<OnlineWebSocketServer>> bidsection = new HashMap<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 标段id
     */
    private Integer bidSectionId;


    /**
     * 连接建立成功调用的方法
     *
     * @param session      websocket连接的session属性
     * @param bidSectionId 标段id
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("bidSectionId") Integer bidSectionId) throws IOException {
        this.session = session;
        this.bidSectionId = bidSectionId;
        // 根据该标段当前是否已经在别的终端登录进行添加操作
        if (bidsection.containsKey(this.bidSectionId)) {
            // 增加该标段set中的连接实例
            bidsection.get(this.bidSectionId).add(this);
        } else {
            Set<OnlineWebSocketServer> addUserSet = new HashSet<>();
            addUserSet.add(this);
            bidsection.put(this.bidSectionId, addUserSet);
        }
        logger.info("标段{}登录的终端个数是为{}", bidSectionId, bidsection.get(this.bidSectionId).size());
    }

    /**
     * @Title: onClose
     * @Description: 连接关闭的操作
     */
    @OnClose
    public void onClose() {
        //移除当前标段终端登录的websocket信息,如果该标段的所有终端都下线了，则删除该标段的记录
        if (bidsection.get(this.bidSectionId).size() == 0) {
            bidsection.remove(this.bidSectionId);
        } else {
            bidsection.get(this.bidSectionId).remove(this);
        }
        logger.info("关闭事件,标段{}登录的终端个数是为{}", this.bidSectionId, bidsection.get(this.bidSectionId).size());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        // System.out.println( "收到来自标段id为：{}的消息：{}" + this.bidsectionId + "_" + message );
        if (session == null) {
            System.out.println("session null");
        }
        //sendMessageToUser( bidsectionId, message );
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("标段id为：{}的连接发送错误", this.bidSectionId);
        error.printStackTrace();
    }

    /**
     * @param bidSectionId 标段id
     * @param message      发送的消息
     * @return 发送成功返回true，反则返回false
     */
    public static Boolean sendMessageToUser(Integer bidSectionId, Object message) {
        if (bidsection.containsKey(bidSectionId)) {
            String ms = JSON.toJSONString(message);
            for (OnlineWebSocketServer onlineWebSocketServer : bidsection.get(bidSectionId)) {
                try {
                    onlineWebSocketServer.session.getBasicRemote().sendText(ms);
                    System.out.println(message.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}