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
 * 单工程量清单数据分析 方法 DTO
 *
 * @author Kevin
 * @since 2020-6-16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DoSingleQuantityAnalysis extends BaseModel implements Serializable {

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

    @JsonProperty("xml_uid")
    @JSONField(name = "xml_uid")
    private String xmlUid;
}
