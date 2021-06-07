package com.ejiaoyi.api.entity;

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
 * 用户日志
 * </p>
 *
 * @author fengjunhong
 * @since 2020-03-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_log")
public class UserLog implements Serializable {

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
     * 用户ID
     */
    @TableField("USER_ID")
    private Integer userId;

    /**
     * 用户名
     */
    @TableField("USERNAME")
    private String username;

    /**
     * 数据库操纵类型
     */
    @TableField("DML_TYPE")
    private String dmlType;

    /**
     * 日志内容
     */
    @TableField("CONTENT")
    private String content;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;


    /************************************** 自定义字段 *****************************/
}
