package com.ejiaoyi.common.dto.quantity;

import com.alibaba.fastjson.annotation.JSONField;
import com.ejiaoyi.common.annotation.ApiParamValidator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 获取服务结果 DTO
 *
 * @author Kevin
 * @since 2020-10-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GetServiceResult extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("service_serial_number")
    @JSONField(name = "service_serial_number")
    private String serviceSerialNumber;

    @JsonProperty("version")
    @JSONField(name = "version")
    private String version;

    @JsonProperty("server_code")
    @JSONField(name = "server_code")
    private String serverCode;
}
