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
 * 接受投标人公共信息 方法 DTO
 *
 * @author Z0001
 * @since 2020-4-22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "GetBidderCommonInfo", description = "投标人公共信息")
public class GetBidderCommonInfo extends BaseModel {

    private static final long serialVersionUID = 1L;

    @JsonProperty("bidderId")
    @JSONField(name = "bidderId")
    @ApiModelProperty(value = "投标人id", position = 1, required = true, example = "1")
    @NotNull(message = "投标人id不可为空")
    private Integer bidderId;

    @JsonProperty("bidSectionId")
    @JSONField(name = "bidSectionId")
    @ApiModelProperty(value = "标段id", position = 1, required = true, example = "1")
    @NotNull(message = "标段id不可为空")
    private Integer bidSectionId;

}
