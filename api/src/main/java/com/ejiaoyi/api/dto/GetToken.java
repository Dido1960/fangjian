package com.ejiaoyi.api.dto;

import com.alibaba.fastjson.annotation.JSONField;
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
import java.io.Serializable;

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
@ApiModel(value = "getToken", description = "getToken")
public class GetToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("platform")
    @JSONField(name = "platform")
    @ApiModelProperty(value = "平台授权码", position = 1, required = true, example = "02510a79-784d-4880-8df8-3e917900e22d")
    @NotNull(message = "平台授权码platform不能为空")
    @Size(min = 32, max = 36, message = "账号长度 不合法")
    private String platform;

    @JsonProperty("api_key")
    @JSONField(name = "api_key")
    @ApiModelProperty(value = "API KEY", position = 2, required = true, example = "cd033f02-5a36-480e-838a-8055a8063701")
    @NotNull(message = "apiKey不能为空")
    @Size(min = 32, max = 36, message = "账号长度 不合法")
    private String apiKey;


}
