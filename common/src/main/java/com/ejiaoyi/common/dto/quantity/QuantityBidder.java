package com.ejiaoyi.common.dto.quantity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 投标人清单清标中间表
 * </p>
 *
 * @author Kevin
 * @since 2020-04-23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class QuantityBidder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 投标人名称
     */
    @JsonProperty("name")
    @JSONField(name = "name")
    private String name;

    /**
     * 统一社会信用代码
     */
    @JsonProperty("credit_code")
    @JSONField(name = "credit_code")
    private String creditCode;

    /**
     * 投标工程量文件标识
     */
    @JsonProperty("bid_xml_uid")
    @JSONField(name = "bid_xml_uid")
    private String bidXmlUid;

    /**********************************************************自定义字段*********************************************/

}
