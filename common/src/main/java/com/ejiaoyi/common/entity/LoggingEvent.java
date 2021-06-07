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
 * 日志事件表
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
@TableName("logging_event")
public class LoggingEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志事件的创建时间
     */
    @TableField("timestmp")
    private Long timestmp;

    /**
     * 经过 org.slf4j.impl.MessageFormatter 格式化后的消息
     */
    @TableField("formatted_message")
    private String formattedMessage;

    /**
     * 发出日志的 logger 名
     */
    @TableField("logger_name")
    private String loggerName;

    /**
     * 日志事件的级别
     */
    @TableField("level_string")
    private String levelString;

    /**
     * 发出日志请求所在的线程名
     */
    @TableField("thread_name")
    private String threadName;

    /**
     * 用来表示是否是异常或者与 MDC 属性相关联。它的值通过 ch.qos.logback.classic.db.DBHelper 计算得到。日志时间包含 MDC 或者 Context 时，它的值为 1。包含异常时，它的值为 2。包含两者，则值为 3。
     */
    @TableField("reference_flag")
    private Integer referenceFlag;

    /**
     * 参数1
     */
    @TableField("arg0")
    private String arg0;

    /**
     * 参数2
     */
    @TableField("arg1")
    private String arg1;

    /**
     * 参数3
     */
    @TableField("arg2")
    private String arg2;

    /**
     * 参数4
     */
    @TableField("arg3")
    private String arg3;

    /**
     * 发出日志请求的文件名
     */
    @TableField("caller_filename")
    private String callerFilename;

    /**
     * 发出日志请求的类
     */
    @TableField("caller_class")
    private String callerClass;

    /**
     * 发出日志请求的方法
     */
    @TableField("caller_method")
    private String callerMethod;

    /**
     * 发出日志请求所在的行
     */
    @TableField("caller_line")
    private String callerLine;

    /**
     * 主键ID
     */
    @TableId(value = "event_id", type = IdType.AUTO)
    private Long eventId;


}
