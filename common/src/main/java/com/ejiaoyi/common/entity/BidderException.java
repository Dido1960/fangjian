package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 投标人异常信息
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
@TableName("bidder_exception")
public class BidderException implements Serializable {

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
     * 标段信息主键ID
     */
    @TableField("BID_SECTION_ID")
    private Integer bidSectionId;

    /**
     * 投标人主键ID
     */
    @TableField("BIDDER_ID")
    private Integer bidderId;

    /**
     * 异常类型
     */
    @TableField("EXCEPTION_TYPE")
    private Integer exceptionType;

    /**
     * 异常原因
     */
    @TableField("EXCEPTION_REASON")
    private String exceptionReason;

    /**
     * 操作人id
     */
    @TableField("OPERATOR_ID")
    private Integer operatorId;

    /**
     * 操作人名称
     */
    @TableField("OPERATOR_NAME")
    private String operatorName;

    /**
     * 操作时间
     */
    @TableField("OPERAT_TIME")
    private String operatTime;

    /**
     * 启用状态  参见数据字典enabled
     */
    @TableField("ENABLED")
    private Integer enabled;

    /******************************自定义字段******************************/

    /**
     *对应的开标信息id
     */
    @TableField(exist = false)
    private Integer BidderOpenInfoId;
}
