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
 * 整体分析服务参数 DTO
 *
 * @author Kevin
 * @since 2020-09-08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OverallAnalysisParam extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("ticket")
    @JSONField(name = "ticket")
    private String ticket;

    @JsonProperty("version")
    @JSONField(name = "version")
    private String version;

    @JsonProperty("server_code")
    @JSONField(name = "server_code")
    private String serverCode;

    @JsonProperty("overall_analysis_service_serial_number")
    @JSONField(name = "overall_analysis_service_serial_number")
    private String overallAnalysisServiceSerialNumber;

    @JsonProperty("float_point")
    @JSONField(name = "float_point")
    private String floatPoint;
}
