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
 * 角色
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
@TableName("role")
public class Role implements Serializable {

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
     * 角色名称
     */
    @TableField("ROLE_NAME")
    private String roleName;

    /**
     * 启用状态 参见数据字典enabled
     */
    @TableField("ENABLED")
    private Integer enabled;

    /**
     * 备注说明
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 排序
     */
    @TableField("ORDER_NO")
    private Integer orderNo;

    /*******************************自定义字段***********************/

    /**
     * 权限状态（用于前端展示）
     */
    @TableField(exist = false)
    private String status;

    /**
     * 对应菜单权限Id
     */
    @TableField(exist = false)
    private String menuId;

    /*******************************自定义方法***********************/

    public String getStatus() {
        return this.enabled == null ? "" : this.enabled == 1 ? "启用" : "禁用";
    }

}
