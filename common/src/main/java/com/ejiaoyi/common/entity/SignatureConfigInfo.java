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
 * 签章配置信息（回执单签章）
 * </p>
 *
 * @author Mike
 * @since 2021-02-18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("signature_config_info")
public class SignatureConfigInfo implements Serializable {

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
     * 区划编号
     */
    @TableField("REG_NO")
    private String regNo;

    /**
     * 印模编号
     */
    @TableField("IMPRESSION_NO")
    private String impressionNo;

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

    /*******************************自定义方法***********************/
}
