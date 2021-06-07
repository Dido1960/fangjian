package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Quartz定义任务表
 *
 * @author Z0001
 * @since 2020-05-20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("quartz_job")
public class QuartzJob implements Serializable {

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
     * 任务名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 任务组
     */
    @TableField("JOB_GROUP")
    private String jobGroup;

    /**
     * 任务描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 任务执行类
     */
    @TableField("JOB_CLASS_NAME")
    private String jobClassName;

    /**
     * 任务触发规则
     */
    @TableField("CRON_EXPRESSION")
    private String cronExpression;

    /**
     * 触发器状态
     */
    @TableField("TRIGGER_STATE")
    private String triggerState;

    /**
     * 旧的任务名称
     */
    @TableField("OLD_NAME")
    private String oldName;

    /**
     * 旧的任务组
     */
    @TableField("OLD_JOB_GROUP")
    private String oldJobGroup;

    /**
     * 启用状态  参见数据字典enabled
     */
    @TableField("ENABLED")
    private Integer enabled;
    /******************************自定义字段******************************/
}
