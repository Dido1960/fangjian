package com.ejiaoyi.common.dto.statistical;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * 群聊信息
 *
 * @since 2021-04-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GroupChatDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 房间ID
     */
    @ApiModelProperty(value = "房间ID", example = "1344", position = 1)
    private String roomId;

    /**
     * 房间名称
     */
    @ApiModelProperty(value = "房间名称", example = "人", position = 2)
    private String roomName;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", example = "1234567", position = 3)
    private String userId;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称", example = "萨达", position = 4)
    private String userName;

    /**
     * 角色类型
     *   0  创建者 （代理机构） 房间创建者 /能够私聊，能够禁言与开启禁言
     *   1  管理员  (监管) 私聊、禁言、发言
     *   2 普通成员（投标人）
     *   3 普通成员（游客） 只能查看房间内的信息
     */
    @ApiModelProperty(value = "角色类型", example = "2", position = 5)
    private String role;

    /**
     * 角色性别
     */
    @ApiModelProperty(value = "角色性别", example = "1", position = 6)
    private Integer sex;

    /**
     *  状态 是否禁言 : 0: 默认  1:禁言
     */
    @ApiModelProperty(value = "状态 是否禁言 : 0: 默认  1:禁言", example = "1", position = 7)
    private Integer kickStatus;

}
