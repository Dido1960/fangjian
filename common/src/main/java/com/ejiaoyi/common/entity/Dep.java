package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 部门
 * </p>
 *
 * @author fengjunhong
 * @since 2020-03-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("dep")
public class Dep implements Serializable {

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
     * 所属行政区划Id
     */
    @TableField("REG_ID")
    private Integer regId;

    /**
     * 机构名称
     */
    @TableField("DEP_NAME")
    private String depName;

    /**
     * 启用状态
     */
    @TableField("ENABLED")
    private Integer enabled;

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
     * 父级部门Id
     */
    @TableField("PARENT_ID")
    private Integer parentId;

    /**
     * 部门类别
     */
    @TableField("GOV_DEP_TYPE")
    private Integer govDepType;

    /**
     * 部门电话
     */
    @TableField("PHONE")
    private String phone;

    /**
     * 部门职责
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 统一信用代码
     */
    @TableField("CODE")
    private String code;

    /*******************************自定义字段***********************/

    /**
     * 区划名称
     */
    @TableField(exist = false)
    private String regName;

    /**
     * 部门状态（用于前端展示）
     */
    @TableField(exist = false)
    private String status;

    /*******************************自定义方法***********************/

    public String getStatus() {
        return this.enabled == null ? "" : this.enabled == 1 ? "启用" : "禁用";
    }
}
