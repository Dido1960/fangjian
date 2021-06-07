package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 行政区划
 * </p>
 *
 * @author fengjunhong
 * @since 2020-03-25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("reg")
public class Reg implements Serializable {

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
     * 父级区划Id
     */
    @TableField("PARENT_ID")
    private Integer parentId;

    /**
     * 区划名称
     */
    @TableField("REG_NAME")
    private String regName;

    /**
     * 排序
     */
    @TableField("ORDER_NO")
    private Integer orderNo;

    /**
     * 拼音 全拼
     */
    @TableField("ALL_SPELLING")
    private String allSpelling;

    /**
     * 拼音 首字母
     */
    @TableField("FIRST_SPELLING")
    private String firstSpelling;

    /**
     * 行政区划代码
     */
    @TableField("REG_NO")
    private String regNo;

    /**
     * 启用状态
     */
    @TableField("ENABLED")
    private Integer enabled;

    /******************************自定义字段******************************/

    /**
     *子集列表
     */
    @TableField(exist = false)
    private List<Reg> subsetList;
}
