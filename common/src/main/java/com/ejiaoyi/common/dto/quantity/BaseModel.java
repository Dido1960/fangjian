package com.ejiaoyi.common.dto.quantity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 基础 Model
 *
 * @author Kevin
 * @since 2020-4-22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("api_key")
    @JSONField(name = "api_key")
    private String apiKey;

    @JsonProperty("platform")
    @JSONField(name = "platform")
    private String platform;

    @JsonProperty("token")
    @JSONField(name = "token")
    private String token;
}
