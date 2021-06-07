package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 投标人工程量清单服务信息
 *
 * @author Kevin
 * @since 2020-12-11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bidder_quantity")
public class BidderQuantity implements Serializable {

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
     * 招标工程量清单文件标示码
     */
    @TableField("TENDER_XML_UID")
    private String tenderXmlUid;

    /**
     * 投标工程量清单文件标示码
     */
    @TableField("BID_XML_UID")
    private String bidXmlUid;

    /**
     * 错漏项分析服务标示: 0:未请求服务 1:已请求服务
     */
    @TableField("STRUCTURE_ANALYSIS_FLAG")
    private Integer structureAnalysisFlag;

    /**
     * 错漏项分析服务序列号
     */
    @TableField("STRUCTURE_ANALYSIS_SERIAL_NUMBER")
    private String structureAnalysisSerialNumber;

    /**
     * 错漏项分析服务状态
     */
    @TableField("STRUCTURE_ANALYSIS_STATE")
    private String structureAnalysisState;

    /**
     * 错漏项分析服务进度
     */
    @TableField("STRUCTURE_ANALYSIS_PROCESS")
    private String structureAnalysisProcess;

    /**
     * 零负报价分析服务标示: 0:未请求服务 1:已请求服务
     */
    @TableField("PRICE_ANALYSIS_FLAG")
    private Integer priceAnalysisFlag;

    /**
     * 零负报价分析服务序列号
     */
    @TableField("PRICE_ANALYSIS_SERIAL_NUMBER")
    private String priceAnalysisSerialNumber;

    /**
     * 零负报价分析服务状态
     */
    @TableField("PRICE_ANALYSIS_STATE")
    private String priceAnalysisState;

    /**
     * 零负报价分析服务进度
     */
    @TableField("PRICE_ANALYSIS_PROCESS")
    private String priceAnalysisProcess;

    /**
     * 算术性分析服务标示: 0:未请求服务 1:已请求服务
     */
    @TableField("ARITHMETIC_ANALYSIS_FLAG")
    private Integer arithmeticAnalysisFlag;

    /**
     * 算术性分析服务序列号
     */
    @TableField("ARITHMETIC_ANALYSIS_SERIAL_NUMBER")
    private String arithmeticAnalysisSerialNumber;

    /**
     * 算术性分析服务状态
     */
    @TableField("ARITHMETIC_ANALYSIS_STATE")
    private String arithmeticAnalysisState;

    /**
     * 算术性分析服务进度
     */
    @TableField("ARITHMETIC_ANALYSIS_PROCESS")
    private String arithmeticAnalysisProcess;

    @TableField("RULE_ANALYSIS_FLAG")
    private Integer ruleAnalysisFlag;

    /**
     * 取费基础分析服务序列号
     */
    @TableField("RULE_ANALYSIS_SERIAL_NUMBER")
    private String ruleAnalysisSerialNumber;

    /**
     * 取费基础分析服务状态
     */
    @TableField("RULE_ANALYSIS_STATE")
    private String ruleAnalysisState;

    /**
     * 取费基础分析服务进度
     */
    @TableField("RULE_ANALYSIS_PROCESS")
    private String ruleAnalysisProcess;

    /**
     * 投标人名称
     */
    @TableField(exist = false)
    private String bidderName;

    /**
     * 投标人总进度百分比
     */
    @TableField(exist = false)
    private String bidderTotalPercentage;

}
