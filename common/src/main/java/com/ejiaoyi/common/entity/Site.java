package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 场地信息
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
@TableName("site")
public class Site implements Serializable {

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
     * 场地名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 场地类型
     */
    @TableField("TYPE")
    private Integer type;

    /**
     * 行政区划主键
     */
    @TableField("REG_ID")
    private Integer regId;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /******************************自定义字段******************************/

    /**
     * 区划名称
     */
    @TableField(exist = false)
    private String regName;

    /**
     * 场地类型
     */
    @TableField(exist = false)
    private String typeName;

    /*******************************自定义方法***********************/
}
