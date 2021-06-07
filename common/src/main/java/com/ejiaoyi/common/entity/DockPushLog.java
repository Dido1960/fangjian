package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 对接推送日志
 * </p>
 *
 * @author Mike
 * @since 2020-12-31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dock_push_log")
public class DockPushLog implements Serializable {

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
     * 请求参数(明文)
     */
    @TableField("API_PARAMS_LAWS")
    private String apiParamsLaws;

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
