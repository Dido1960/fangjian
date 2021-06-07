package com.ejiaoyi.api.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.ejiaoyi.common.dto.BaseModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 接收投标人签到信息 方法 DTO
 *
 * @author Z0001
 * @since 2020-4-22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "GetBidderSignInfo", description = "投标人签到信息参数")
public class GetBidderSignInfo extends BaseModel {

    private static final long serialVersionUID = 1L;

    @JsonProperty("bidderId")
    @JSONField(name = "bidderId")
    @ApiModelProperty(value = "投标人bidderId", position = 1, required = true, example = "1")
    @NotNull(message = "投标人bidderId不可为空")
    private Integer bidderId;

    @JsonProperty("bidSectionId")
    @JSONField(name = "bidSectionId")
    @ApiModelProperty(value = "标段id", position = 1, required = true, example = "1")
    @NotNull(message = "投标人id不可为空")
    private Integer bidSectionId;

    @JsonProperty("bidderName")
    @JSONField(name = "bidderName")
    @ApiModelProperty(value = "企业名称", position = 2, required = true, example = "mcz投标测试三")
    @Size(min = 1, max = 255, message = "企业名称长度 不合法")
    private String bidderName;

    @JsonProperty("clientName")
    @JSONField(name = "clientName")
    @ApiModelProperty(value = "委托人姓名", position = 3, required = true, example = "张三")
    @NotNull(message = "clientName不可为空")
    private String clientName;

    @JsonProperty("clientIdCard")
    @JSONField(name = "clientIdCard")
    @ApiModelProperty(value = "身份证号码（居民身份证）", position = 4, required = true, example = "110101199003074610")
    @NotNull(message = "clientIdCard不可为空")
    private String clientIdCard;

    @JsonProperty("clientPhone")
    @JSONField(name = "clientPhone")
    @ApiModelProperty(value = "联系电话", position = 5, required = true, example = "13322548710")
    @NotNull(message = "clientPhone不可为空")
    private String clientPhone;

    @JsonProperty("sqwtsUrl")
    @JSONField(name = "sqwtsUrl")
    @ApiModelProperty(value = "授权委托书地址", position = 6, required = true, example = "http://")
    @NotNull(message = "授权委托书地址 不可为空")
    private String sqwtsUrl;

    @JsonProperty("optAuthWay")
    @JSONField(name = "optAuthWay")
    @ApiModelProperty(value = "选择认证方式(1 信ID 2 支付宝)", position = 7, required = true, example = "1")
    @NotNull(message = "授权委托书地址 不可为空")
    private Integer optAuthWay;
}
