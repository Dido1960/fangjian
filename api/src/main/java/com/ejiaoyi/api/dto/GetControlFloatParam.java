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
 * 接受控制价或浮动点信息 方法 DTO
 *
 * @author Z0001
 * @since 2020-4-22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "GetControlFloatParam", description = "控制价或浮动点信息")
public class GetControlFloatParam extends BaseModel {

    private static final long serialVersionUID = 1L;

    @JsonProperty("bidSectionId")
    @JSONField(name = "bidSectionId")
    @ApiModelProperty(value = "标段id", position = 2, required = true, example = "1")
    @NotNull(message = "标段id不可为空")
    private Integer bidSectionId;

    @JsonProperty("type")
    @JSONField(name = "type")
    @ApiModelProperty(value = "类型(1:控制价,2:浮动点)，默认控制价", position = 1, required = true, example = "1")
    private Integer type;


}
