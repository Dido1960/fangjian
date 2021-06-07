package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 用户和CA关系表
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
@TableName("user_cert")
public class UserCert implements Serializable {

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
     * 锁号
     */
    @TableField("UKEY_NUM")
    private String ukeyNum;

    /**
     * 用户类型
     */
    @TableField("USER_TYPE")
    private Integer userType;

    /**
     * 锁的名称
     */
    @TableField("CA_NAME")
    private String caName;

    /**
     * 是否允许登录
     */
    @TableField("LOGIN_FLAG")
    private Integer loginFlag;

    /**
     * 是否删除
     */
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    /******************************自定义字段******************************/
}
