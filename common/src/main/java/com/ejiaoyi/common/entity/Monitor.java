package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 监控信息
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
@TableName("monitor")
public class Monitor implements Serializable {

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
     * 行政区划主键
     */
    @TableField("REG_ID")
    private Integer regId;

    /**
     * 行政区划名称
     */
    @TableField("REG_NAME")
    private String regName;

    /**
     * 行政区划代码
     */
    @TableField("REG_CODE")
    private String regCode;

    /**
     * 大华摄像头IP
     */
    @TableField("IP")
    private String ip;

    /**
     * 大华摄像头端口
     */
    @TableField("PORT")
    private String port;

    /**
     * 用户名
     */
    @TableField("USER_NAME")
    private String userName;

    /**
     * 密码
     */
    @TableField("PWD")
    private String pwd;

    /**
     * 启用状态  参见数据字典enabled
     */
    @TableField("ENABLED")
    private Integer enabled;

    /******************************自定义字段******************************/
}
