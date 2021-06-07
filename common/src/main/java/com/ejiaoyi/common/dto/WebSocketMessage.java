package com.ejiaoyi.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * WebSocket 通知消息
 *
 * @author Z0001
 * @since 2020-03-17
 */
@Data
public class WebSocketMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 默认消息
     */
    public static final String DEFAULT_INFO_MSG = "操作成功!";

    /**
     * 默认错误消息
     */
    public static final String DEFAULT_ERROR_MSG = "操作失败!";

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 消息类型
     */
    private Type type;

    /**
     * 对话框类型枚举
     */
    private Dialog dialog;

    /**
     * 消息类型枚举
     */
    public enum Type {

        /**
         * 提醒消息
         */
        INFO("INFO"),

        /**
         * 错误消息
         */
        ERROR("ERROR");

        private String type;

        Type(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

    /**
     * 对话框类型枚举
     */
    public enum Dialog {

        /**
         * alert弹框
         */
        ALERT("ALERT"),

        /**
         * 普通弹框
         */
        MSG("MSG");

        private String dialog;

        Dialog(String dialog) {
            this.dialog = dialog;
        }

        @Override
        public String toString() {
            return this.dialog;
        }
    }

    public WebSocketMessage(Type type, String msg, Dialog dialog) {
        this.type = type;
        this.msg = msg;
        this.setDialog(dialog);
    }

    public WebSocketMessage(Type type, String msg) {
        new WebSocketMessage(type, msg, Dialog.MSG);
    }
}
