package com.ejiaoyi.common.dto.quantity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 执行工程量清单数据对比分析 方法 DTO
 *
 * @author Kevin
 * @since 2020-10-09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DoCompareQuantityAnalysis extends BaseModel implements Serializable {

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

    @JsonProperty("tender_xml_uid")
    @JSONField(name = "tender_xml_uid")
    private String tenderXmlUid;

    @JsonProperty("bid_xml_uid")
    @JSONField(name = "bid_xml_uid")
    private String bidXmlUid;
}
