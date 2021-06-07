package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 标段关联信息
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
@TableName("bid_section_relate")
public class BidSectionRelate implements Serializable {

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
     * 主场行政区划主键ID
     */
    @TableField("REG_ID")
    private Integer regId;

    /**
     * 客场行政区划主键ID
     */
    @TableField("AWAY_REG_ID")
    private Integer awayRegId;

    /**
     * 主场开标室 关联Site主键ID
     */
    @TableField("HOME_OPEN_SITE")
    private Integer homeOpenSite;

    /**
     * 主场评标室 关联Site主键ID
     */
    @TableField("HOME_EVAL_SITE")
    private Integer homeEvalSite;

    /**
     * 客场评标室 关联Site主键ID
     */
    @TableField("AWAY_EVAL_SITE")
    private Integer awayEvalSite;

    /**
     * 评标报告ID 关联fdfs主键ID
     */
    @TableField("EVALUATION_REPORT_ID")
    private Integer evaluationReportId;

    /**
     * 清标开始时间
     */
    @TableField("START_CLEAR_TIME")
    private String startClearTime;

    /**
     * 清标用时
     */
    @TableField("CLEAR_TOTAL_TIME")
    private String clearTotalTime;

    /**
     * 阶段的整体服务参数序列号 清标 经济标的视图参数（开标结束后，进入评标的投标人）
     */
    @TableField("CLEAR_ANALYSIS_UID")
    private String clearAnalysisUid;

    /**
     * 评标阶段的整体服务参数序列号 报价的视图参数(初步评审后，参与报价得分计算的投标人)
     */
    @TableField("CALC_PRICE_UID")
    private String calcPriceUid;

    /**
     * 报价得分计算服务序列号 用于获取报价计算的结果JSON（初步评审后，参与报价得分计算的投标人）
     */
    @TableField("PRICE_SCORE_UID")
    private String priceScoreUid;

    /**
     * 非预审项目，管理预审项目 bidSectionId
     */
    @TableField("PRE_RELATED_ID")
    private Integer preRelatedId;

    /**
     * 是否删除
     */
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    /**
     * 复会报告ID 关联fdfs主键ID
     */
    @TableField("RESUMPTION_REPORT_ID")
    private Integer resumptionReportId;

    /**
     * 是否老系统项目，1：老系统项目
     */
    @TableField("IS_OLD_PROJECT")
    private String isOldProject;

    /******************************自定义字段******************************/

    /**
     * 复会报告url
     */
    @TableField(exist = false)
    private String resumptionReportUrl;

}
