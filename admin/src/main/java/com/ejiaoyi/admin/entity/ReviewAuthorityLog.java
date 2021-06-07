package com.ejiaoyi.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 离线审核权限写入日志
 * </p>
 *
 * @author Mike
 * @since 2021-01-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("review_authority_log")
public class ReviewAuthorityLog implements Serializable {

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
     * 操作人id
     */
    @TableField("USER_ID")
    private Integer userId;

    /**
     * 操作人名称
     */
    @TableField("USER_NAME")
    private String userName;

    /**
     * 证书名称
     */
    @TableField("CERT_NAME")
    private String certName;

    /**
     * 锁唯一序列号
     */
    @TableField("KEY_NO")
    private String keyNo;

    /**
     * 操作方式（1：写入权限， 0：取消权限）
     */
    @TableField("OPERATE_TYPE")
    private Integer operateType;


}
