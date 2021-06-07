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
 * API日志
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
@TableName("api_log")
public class ApiLog implements Serializable {

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
     * 请求接口名称
     */
    @TableField("API_NAME")
    private String apiName;

    /**
     * 请求方法名称
     */
    @TableField("METHOD_NAME")
    private String methodName;

    /**
     * 请求参数内容
     */
    @TableField("PARAMS")
    private String params;

    /**
     * 平台授权码
     */
    @TableField("PLATFORM")
    private String platform;

    /**
     * API授权码
     */
    @TableField("API_KEY")
    private String apiKey;

    /**
     * 日志创建时间
     */
    @TableField("CREATE_API_TIME")
    private LocalDateTime createApiTime;

    /**
     * 响应结果
     */
    @TableField("RESPONSE")
    private String response;

    /**
     * 响应时间
     */
    @TableField("RESPONSE_TIME")
    private LocalDateTime responseTime;

    /**
     * 响应耗时
     */
    @TableField("RESPONSE_TIME_CONSUME")
    private Long responseTimeConsume;


}
