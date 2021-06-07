package com.ejiaoyi.common.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 推送结果信息 DTO
 *
 * @author Mike
 * @since 2020-12-31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PushResultInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 项目编号
     */
    @JsonProperty("PROJECTNO")
    @JSONField(name = "PROJECTNO")
    private String projectCode;

    /**
     * 项目名称
     */
    @JsonProperty("PROJECTNAME")
    @JSONField(name = "PROJECTNAME")
    private String projectName;

    /**
     * 标段编号
     */
    @JsonProperty("BIAODUANNO")
    @JSONField(name = "BIAODUANNO")
    private String bidSectionCode;

    /**
     * 标段名称
     */
    @JsonProperty("BIAODUANNAME")
    @JSONField(name = "BIAODUANNAME")
    private String bidSectionName;

    /**
     * 评标是否结束（1 结束 其他值均为 未结束）
     */
    @JsonProperty("PINGBIAOISEND")
    @JSONField(name = "PINGBIAOISEND")
    private String isBidEvalEnd;

    /**
     * 开标是否结束（1 结束 其他值均为 未结束）
     */
    @JsonProperty("KAIBIAOISEND")
    @JSONField(name = "KAIBIAOISEND")
    private String isBidOpenEnd;

    /**
     * 开标开始时间（时间格式为 yyyy-MM-dd HH:mm:ss）
     */
    @JsonProperty("KBSTARTDATE")
    @JSONField(name = "KBSTARTDATE")
    private String bidOpenTime;

    /**
     * 开标结束时间（时间格式为 yyyy-MM-dd HH:mm:ss）
     */
    @JsonProperty("KBENDDATE")
    @JSONField(name = "KBENDDATE")
    private String bidOpenEndTime;

    /**
     * 评标开始时间（时间格式为 yyyy-MM-dd HH:mm:ss）
     */
    @JsonProperty("PBSTARTDATE")
    @JSONField(name = "PBSTARTDATE")
    private String evalStartTime;

    /**
     * 评标结束时间（时间格式为 yyyy-MM-dd HH:mm:ss）
     */
    @JsonProperty("PBENDDATE")
    @JSONField(name = "PBENDDATE")
    private String evalEndTime;

    /**
     * 评标办法
     */
    @JsonProperty("PINGBIAOMETHOD")
    @JSONField(name = "PINGBIAOMETHOD")
    private String bidEvalMethod;

    /**
     * 投标人的评标结果情况
     */
    @JsonProperty("DANWEIINFO")
    @JSONField(name = "DANWEIINFO")
    private List<BidderEvalResultDTO> bidderEvalResultDTOS;
}
