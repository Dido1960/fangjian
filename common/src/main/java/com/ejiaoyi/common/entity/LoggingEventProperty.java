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
 * 日志属性信息表
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
@TableName("logging_event_property")
public class LoggingEventProperty implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志事件的数据库 id
     */
    @TableId(value = "event_id", type = IdType.AUTO)
    private Long eventId;

    /**
     * 属性的 key
     */
    @TableField("mapped_key")
    private String mappedKey;

    /**
     * 属性的 value
     */
    @TableField("mapped_value")
    private String mappedValue;
}
