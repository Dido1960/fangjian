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

/**
 * 接受标段id 方法 DTO
 *
 * @author Z0001
 * @since 2020-4-22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "BidSectionOpenParam", description = "投标人开标 接口参数")
public class BidSectionOpenParam extends BaseModel {

    private static final long serialVersionUID = 1L;

    @JsonProperty("bidSectionId")
    @JSONField(name = "bidSectionId")
    @ApiModelProperty(value = "标段id", position = 1, required = true, example = "1")
    @NotNull(message = "标段id不可为空")
    private Integer bidSectionId;

    @JsonProperty("bidderOrgCode")
    @JSONField(name = "bidderOrgCode")
    @ApiModelProperty(value = "统一社会信用代码", position = 2, required = true, example = "1")
    private String bidderOrgCode;

    @JsonProperty("bidderId")
    @JSONField(name = "bidderId")
    @ApiModelProperty(value = "投标人ID", position = 3, required = true, example = "1")
    private Integer bidderId;
}
