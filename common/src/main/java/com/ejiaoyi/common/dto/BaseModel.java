package com.ejiaoyi.common.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 基础 Model
 *
 * @author Z0001
 * @since 2020-4-22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "基础Model", description = "基础Model")
public class BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("platform")
    @JSONField(name = "platform")
    @ApiModelProperty(value = "平台授权码", required = true, example = "02510a79-784d-4880-8df8-3e917900e22d")
    @TableField(exist = false)
    //上面定义的是文档注解，下面用于程序校验用于统一异常处理
    @NotNull(message = "platform不能为空")
    @Size(min = 32, max = 36, message = "platform 不合法")
    private String platform;


    @JsonProperty("api_key")
    @JSONField(name = "api_key")
    @ApiModelProperty(value = "API KEY", required = true, example = "cd033f02-5a36-480e-838a-8055a8063701")
    @TableField(exist = false)
    //上面定义的是文档注解，下面用于程序校验用于统一异常处理
    @NotNull(message = "apiKey不能为空")
    @Size(min = 32, max = 36, message = "apiKey不合法")
    private String apiKey;


    @JsonProperty("token")
    @JSONField(name = "token")
    @ApiModelProperty(value = "TOKEN", required = true, example = "f3fe31f6-41ee-42ad-8786-8dccd278d20a")
    @TableField(exist = false)
    //上面定义的是文档注解，下面用于程序校验用于统一异常处理
    @NotNull(message = "token不能为空")
    @Size(min = 32, max = 36, message = "token 不合法")
    private String token;
}
