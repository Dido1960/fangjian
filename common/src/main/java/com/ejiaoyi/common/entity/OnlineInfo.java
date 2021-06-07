package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 在线用户信息表
 * </p>
 *
 * @author lesgod
 * @since 2021-01-16
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("online_info")
public class OnlineInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 数据时间戳
     */
    @TableField("INSERT_TIME")
    private String insertTime;

    /**
     * 登录ID
     */
    @TableField("SESSION_ID")
    private String sessionId;

    /**
     * 名字
     */
    @TableField("NAME")
    private String name;


    /**
     * 使用模块
     */
    @TableField("MODULE")
    private String module;

    /**
     * 用户ID
     */
    @TableField("USER_ID")
    private String userId;

    /**
     * 登录IP
     */
    @TableField("IP_INFO")
    private String ipInfo;

    /**
     * 地址信息
     */
    @TableField("ADDRESS_INFO")
    private String addressInfo;

}
