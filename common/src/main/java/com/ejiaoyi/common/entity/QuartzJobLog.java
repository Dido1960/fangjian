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
 * Quartz定时任务执行日志
 *
 * @author Z0001
 * @since 2020-05-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("quartz_job_log")
public class QuartzJobLog implements Serializable {

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
    @TableField("JOB_NAME")
    private String jobName;

    /**
     * 任务组
     */
    @TableField("JOB_GROUP")
    private String jobGroup;

    /**
     * 任务主键
     */
    @TableField("JOB_ID")
    private Integer jobId;

    /**
     * 任务执行时间
     */
    @TableField("JOB_TIME")
    private LocalDateTime jobTime;

    /**
     * 任务执行类
     */
    @TableField("CLASS_NAME")
    private String className;

    /******************************自定义字段******************************/
}
