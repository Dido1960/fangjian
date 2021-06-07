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
 * API推送日志
 *
 * @author Kevin
 * @since 2020-10-28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("api_push_log")
public class ApiPushLog implements Serializable {

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
     * 请求地址
     */
    @TableField("API_URI")
    private String apiUri;

    /**
     * 请求参数
     */
    @TableField("API_PARAMS")
    private String apiParams;

    /**
     * 请求说明
     */
    @TableField("API_REMARK")
    private String apiRemark;

    /**
     * 日志创建时间
     */
    @TableField("CREATE_API_TIME")
    private LocalDateTime createApiTime;

    /**
     * 响应状态值
     */
    @TableField("RESPONSE_CODE")
    private Integer responseCode;

    /**
     * 响应返回值
     */
    @TableField("RESPONSE_CONTENT")
    private String responseContent;
}
