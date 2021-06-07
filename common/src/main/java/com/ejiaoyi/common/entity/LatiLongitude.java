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
 * 投标人/游客经纬度
 * </p>
 *
 * @author samzqr
 * @since 2020-12-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("lati_longitude")
public class LatiLongitude implements Serializable {

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
     * 投标人名称
     */
    @TableField("BIDDER_NAME")
    private String bidderName;

    /**
     * 标段id
     */
    @TableField("BID_SECTION_ID")
    private String bidSectionId;

    /**
     * 经纬度
     */
    @TableField("LATI_LONGITUDE")
    private String latiLongitude;

    /**
     * 身份：0：投标人，1：游客
     */
    @TableField("TYPE")
    private Integer type;

    /**
     * 备注
     */
    @TableField("REMAKE")
    private String remake;


}
