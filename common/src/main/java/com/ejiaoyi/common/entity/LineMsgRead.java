package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 网上开标消息阅读情况
 * </p>
 *
 * @author Make
 * @since 2020-08-07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("line_msg_read")
public class LineMsgRead implements Serializable {

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
     * 网上开标消息主键ID
     */
    @TableField("LINE_MSG_ID")
    private Integer lineMsgId;

    /**
     * 用户ID（投标人主键Id或代理机构主键ID）
     */
    @TableField("USER_ID")
    private Integer userId;

    /**
     * 用户类型（投标人：1投标人，2代理）
     */
    @TableField("USER_TYPE")
    private Integer userType;

    /**
     * 阅读情况(0：未读，1:已读；默认情况未读)
     */
    @TableField("READ_SITUATION")
    private Integer readSituation;


}
