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
 * 日志异常信息表
 * </p>
 *
 * @author Z0001
 * @since 2020-04-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("logging_event_exception")
public class LoggingEventException implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志事件的数据库 id
     */
    @TableId(value = "event_id", type = IdType.AUTO)
    private Long eventId;

    /**
     * 堆栈所在的行
     */
    @TableField("i")
    private Integer i;

    /**
     * 相对应的堆栈信息
     */
    @TableField("trace_line")
    private String traceLine;


}
