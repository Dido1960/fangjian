package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * API接口认证信息
 *
 * @author Z0001
 * @since 2020-07-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("api_auth")
public class ApiAuth implements Serializable {

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
     * 接口名称
     */
    @TableField("API_NAME")
    private String apiName;

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
     * 授权说明
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 启用状态  参见数据字典enabled
     */
    @TableField("ENABLED")
    private Integer enabled;

    /******************************自定义字段******************************/
    /**
     * 中文API NAME
     */
    @TableField(exist = false)
    private String chineseApiName;
}
