package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 点赞统计
 * </p>
 *
 * @author samzqr
 * @since 2020-12-12
 */
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("like_count")
public class LikeCount implements Serializable {

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
     * 标段id
     */
    @TableField("BID_SECTION_ID")
    private Integer bidSectionId;

    /**
     * 点赞数
     */
    @TableField("COUNT")
    private Integer count;

    /**
     * 备注
     */
    @TableField("REMAKE")
    private String remake;


}
