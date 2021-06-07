package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户区划关联表
 * </p>
 *
 * @author langwei
 * @since 2020-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_reg")
public class UserReg implements Serializable {

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
     * 用户id
     */
    @TableField("USER_ID")
    private Integer userId;

    /**
     * 区划id
     */
    @TableField("REG_ID")
    private Integer regId;


}
