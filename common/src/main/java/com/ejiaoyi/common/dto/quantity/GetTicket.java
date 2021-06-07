package com.ejiaoyi.common.dto.quantity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * getTicket 方法 DTO
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
public class GetTicket extends BaseModel implements Serializable {

    @JsonProperty("version")
    @JSONField(name = "version")
    private String version;

    @JsonProperty("type")
    @JSONField(name = "type")
    private Integer type;

    @JsonProperty("server_code")
    @JSONField(name = "server_code")
    private String serverCode;
}
