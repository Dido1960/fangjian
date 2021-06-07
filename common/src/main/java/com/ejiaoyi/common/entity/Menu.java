package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * 菜单表
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
@TableName("menu")
public class Menu implements Serializable {

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
     * 父级菜单Id
     */
    @TableField("PARENT_ID")
    private Integer parentId;

    /**
     * 菜单名称
     */
    @TableField("MENU_NAME")
    private String menuName;

    /**
     * 菜单地址
     */
    @TableField("URL")
    private String url;

    /**
     * 菜单排序号
     */
    @TableField("ORDER_NO")
    private Integer orderNo;

    /**
     * 启用状态  参见数据字典enabled
     */
    @TableField("ENABLED")
    private Integer enabled;

    /**
     * 菜单图标
     */
    @TableField("ICON_FONT")
    private String iconFont;

    /******************************自定义字段******************************/


    /**
     * 最大排序号
     */
    @TableField(exist = false)
    private String maxOrderNo;

    //
    @TableField(exist = false)
    private List<Menu> subMenuList;
}
