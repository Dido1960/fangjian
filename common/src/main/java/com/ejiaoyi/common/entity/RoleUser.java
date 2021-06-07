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
 * 角色用户关系表
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
@TableName("role_user")
public class RoleUser implements Serializable {

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
     * 角色id
     */
    @TableField("ROLE_ID")
    private Integer roleId;

    /**
     * 用户id
     */
    @TableField("USER_ID")
    private Integer userId;

    /*******************************自定义字段***********************/

    /**
     * 区划名称
     */
    @TableField(exist = false)
    private String roleName;

    /*******************************自定义方法***********************/

    public String getroleName() {
        return this.roleName;
    }


}
