package com.ejiaoyi.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description:作为身份检查的返回实体，存储数据
 * @Auther: liuguoqiang
 * @Date: 2020/7/22 11:22
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnlineUserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 房间ID
     */
    private String roomId;


    /**
     * 房间名称
     */
    private String roomName;

    /**
     * 用户名称
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 角色类型
     *   0  创建者 （代理机构） 房间创建者 /能够私聊，能够禁言与开启禁言
     *   1  管理员  (监管) 私聊、禁言、发言
     *   2 普通成员（投标人）
     *   3 普通成员（游客） 只能查看房间内的信息
     */
    private String role;

    /**
     * 角色性别
     */
    private Integer sex;

    /**
     *  状态 是否禁言 : 0: 默认  1:禁言
     */
    private Integer kickStatus;

    /**
     *  状态 是否导出消息
     */
    private boolean importMessage;

}
