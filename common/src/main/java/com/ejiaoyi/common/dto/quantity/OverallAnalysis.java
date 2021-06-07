package com.ejiaoyi.common.dto.quantity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 整体清单数据对比分析 DTO
 *
 * @author Kevin
 * @since 2020-09-02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OverallAnalysis extends BaseModel implements Serializable {

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

    @JsonProperty("bidder_list")
    @JSONField(name = "bidder_list")
    private List<QuantityBidder> bidderList;
}
