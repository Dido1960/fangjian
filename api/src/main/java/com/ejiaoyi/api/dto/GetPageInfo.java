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

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

/**
 * getToken 方法 DTO
 *
 * @author Z0001
 * @since 2020-4-22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "GetPageInfo", description = "GetPageInfo")
public class GetPageInfo extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 用于统一异常处理
     */
    @JsonProperty("page")
    @JSONField(name = "page")
    @ApiModelProperty(value = "页码", position = 1, required = true, example = "1")
    @NotNull(message = "分页查询页码不可为空")
    private Integer page;

    /**
     * 参数验证异常
     */
    @JsonProperty("limit")
    @JSONField(name = "limit")
    @ApiModelProperty(value = "每页条数（1-99)建议取值15条", position = 1, required = true, example = "10")
    @NotNull(message = "每页条数")
    @Digits(integer = 2,fraction=0,message = "（1-99）每页条数不合理")
    private Integer limit;

}
