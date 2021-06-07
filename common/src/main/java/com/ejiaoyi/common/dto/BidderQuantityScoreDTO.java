package com.ejiaoyi.common.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 单个投标人工程量清单报价得分结果 DTO
 *
 * @author Kevin
 * @since 2020-09-10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BidderQuantityScoreDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("name")
    @JSONField(name = "name")
    @ApiModelProperty(value = "投标人名称", position = 1, example = "北京百度网讯科技有限公司")
    private String name;

    @JsonProperty("credit_code")
    @JSONField(name = "credit_code")
    @ApiModelProperty(value = "统一社会信用代码", position = 2, example = "91110000802100433B")
    private String creditCode;

    @JsonProperty("bid_xml_uid")
    @JSONField(name = "bid_xml_uid")
    @ApiModelProperty(value = "投标工程量文件标识", position = 3, example = "1ec254e4-6810-41d0-b206-a75a031828bb")
    private String bidXmlUid;

    @JsonProperty("score_a")
    @JSONField(name = "score_a")
    @ApiModelProperty(value = "分部分项工程量清单报价A 得分", position = 4, example = "0.00")
    private BigDecimal scoreA;

    @JsonProperty("score_b1")
    @JSONField(name = "score_b1")
    @ApiModelProperty(value = "措施项目清单报价B1 得分", position = 5, example = "0.00")
    private BigDecimal scoreB1;

    @JsonProperty("score_b2")
    @JSONField(name = "score_b2")
    @ApiModelProperty(value = "措施项目清单报价B2 得分", position = 6, example = "0.00")
    private BigDecimal scoreB2;

    @JsonProperty("score_c")
    @JSONField(name = "score_c")
    @ApiModelProperty(value = "其它项目清单C(总承包服务费) 得分", position = 7, example = "0.00")
    private BigDecimal scoreC;

    @JsonProperty("score_d")
    @JSONField(name = "score_d")
    @ApiModelProperty(value = "规费清单报价D 得分", position = 8, example = "0.00")
    private BigDecimal scoreD;

    @JsonProperty("score_e")
    @JSONField(name = "score_e")
    @ApiModelProperty(value = "税金清单报价E 得分", position = 9, example = "0.00")
    private BigDecimal scoreE;

    @JsonProperty("score_f")
    @JSONField(name = "score_f")
    @ApiModelProperty(value = "综合单价F 得分", position = 10, example = "0.00")
    private BigDecimal scoreF;

    @JsonProperty("score_g")
    @JSONField(name = "score_g")
    @ApiModelProperty(value = "主要材料设备单价G 得分", position = 11, example = "0.00")
    private BigDecimal scoreG;

    @JsonProperty("score")
    @JSONField(name = "score")
    @ApiModelProperty(value = "总得分", position = 12, example = "0.00")
    private BigDecimal score;
}
