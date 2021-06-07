package com.ejiaoyi.common.dto.quantity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * getToken 方法 DTO
 *
 * @author Kevin
 * @since 2020-4-22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GetToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("api_key")
    @JSONField(name = "api_key")
    private String apiKey;

    @JsonProperty("platform")
    @JSONField(name = "platform")
    private String platform;
}
