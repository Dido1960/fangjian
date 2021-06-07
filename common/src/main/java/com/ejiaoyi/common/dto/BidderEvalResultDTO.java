package com.ejiaoyi.common.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

/**
 * 投标人评标结果信息 DTO
 *
 * @author Mike
 * @since 2020-12-31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BidderEvalResultDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 组织机构代码或者统一信用代码
     */
    @JsonProperty("UNITORGNUM")
    @JSONField(name = "UNITORGNUM")
    private String bidderOrgCode;

    /**
     * 单位名称
     */
    @JsonProperty("DANWEINAME")
    @JSONField(name = "DANWEINAME")
    private String bidderName;

    /**
     * 投标人报价(投标人报价，内容为数字格式，不要以科学计数法显示)
     */
    @JsonProperty("BAOJIA")
    @JSONField(name = "BAOJIA")
    private String bidderPrice;

    /**
     * 投标人报价单位(此字段包含如下值：元、万元、%)
     */
    @JsonProperty("TOUBIAOPRICEUNIT")
    @JSONField(name = "TOUBIAOPRICEUNIT")
    private String priceUint;

    /**
     * 最终报价（该报价的报价单位取TouBiaoPriceUnit）
     */
    @JsonProperty("LASTTOTALPRICE")
    @JSONField(name = "LASTTOTALPRICE")
    private String finalPrice;

    /**
     * 项目经理姓名(投标单位该项目的项目经理姓名)
     */
    @JsonProperty("TBPM")
    @JSONField(name = "TBPM")
    private String projectManagerName;

    /**
     * 工期（单位为天）
     */
    @JsonProperty("GONGQI")
    @JSONField(name = "GONGQI")
    private String constructionPeriod;

    /**
     * 质量要求
     */
    @JsonProperty("ZHILIANG")
    @JSONField(name = "ZHILIANG")
    private String quality;

    /**
     * 保证金缴纳金额（单位为元）
     */
    @JsonProperty("BZJMONEY")
    @JSONField(name = "BZJMONEY")
    private String marginAmount;

    /**
     * 标书密封情况
     */
    @JsonProperty("FILEMIFENG")
    @JSONField(name = "FILEMIFENG")
    private String bidderFileSealStatus;

    /**
     * 投标单位签到时间（时间格式为 yyyy-MM-dd HH:mm:ss）
     */
    @JsonProperty("COMETIME")
    @JSONField(name = "COMETIME")
    private String signTime;

    /**
     * 商务分
     */
    @JsonProperty("JJBFS")
    @JSONField(name = "JJBFS")
    private String businessScore;

    /**
     * 技术分
     */
    @JsonProperty("JSBFS")
    @JSONField(name = "JSBFS")
    private String technologyScore;

    /**
     * 业绩分
     */
    @JsonProperty("ZHBFS")
    @JSONField(name = "ZHBFS")
    private String performanceScore;

    /**
     * 其他分
     */
    @JsonProperty("QTFS")
    @JSONField(name = "QTFS")
    private String otherScore;

    /**
     * 汇总分
     */
    @JsonProperty("LASTMARK")
    @JSONField(name = "LASTMARK")
    private String totalScore;

    /**
     * 排名(该单位的评标排名)
     */
    @JsonProperty("PAIMING")
    @JSONField(name = "PAIMING")
    private Integer rank;

    /**
     * 是否中标(1 中标)
     */
    @JsonProperty("ISZB")
    @JSONField(name = "ISZB")
    private String isWinBid;

    /**
     * 是否废标(1 废标)
     */
    @JsonProperty("ISFEIBIAO")
    @JSONField(name = "ISFEIBIAO")
    private String isAbolish;

    /**
     * 废标原因
     */
    @JsonProperty("FEIBIAORESON")
    @JSONField(name = "FEIBIAORESON")
    private String abolishReason;

    /**
     * 资审开标备注
     */
    @JsonProperty("KBREMARK")
    @JSONField(name = "KBREMARK")
    private String ZGYSOpenBidDesc;

    /**
     * 资审文件开启时间（时间格式为 yyyy-MM-dd HH:mm:ss）
     */
    @JsonProperty("DECRTBJieMiDATE")
    @JSONField(name = "DECRTBJieMiDATE")
    private String openTimeReviewZGYSFile;

    /**
     * 资格预审是否通过(1 通过)
     */
    @JsonProperty("ISZiGEYSPASS")
    @JSONField(name = "ISZiGEYSPASS")
    private String isPassZGYS;
}
