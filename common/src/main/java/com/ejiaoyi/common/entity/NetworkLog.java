package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 网络日志
 * </p>
 *
 * @author fengjunhong
 * @since 2020-03-25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("network_log")
public class NetworkLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 记录插入时间
     */
    @TableField("INSERT_TIME")
    private String insertTime;

    /**
     * 用户id
     */
    @TableField("USER_ID")
    private Integer userId;

    /**
     * 用户名
     */
    @TableField("USER_NAME")
    private String userName;

    /**
     * 请求地址
     */
    @TableField("REQUEST_URI")
    private String requestUri;

    /**
     * 请求方法
     */
    @TableField("REQUEST_METHOD")
    private String requestMethod;

    /**
     * 请求参数
     */
    @TableField("REQUEST_PARAMS")
    private String requestParams;

    /**
     * 来源地址
     */
    @TableField("REMOTE_ADDRESS")
    private String remoteAddress;

    /**
     * 用户代理
     */
    @TableField("USER_AGENT")
    private String userAgent;

    /**
     * 设备名称
     */
    @TableField("DEVICE_NAME")
    private String deviceName;

    /**
     * 浏览器名称
     */
    @TableField("BROWSER_NAME")
    private String browserName;

    /**
     * 浏览器版本
     */
    @TableField("BROWSER_VERSION")
    private String browserVersion;

    /**
     * 处理耗时
     */
    @TableField("PROCESSING_TIME")
    private Long processingTime;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;


}
