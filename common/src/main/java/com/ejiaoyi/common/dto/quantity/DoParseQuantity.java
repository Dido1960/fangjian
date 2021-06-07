package com.ejiaoyi.common.dto.quantity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * DoParseQuantity 方法 DTO
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
public class DoParseQuantity extends BaseModel implements Serializable {

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

    @JsonProperty("xml_path")
    @JSONField(name = "xml_path")
    private String xmlPath;

    @JsonProperty("xml_md5")
    @JSONField(name = "xml_md5")
    private String xmlMd5;

    @JsonProperty("path_type")
    @JSONField(name = "path_type")
    private String pathType;

    /*******************************************自定义字段******************************************/
}
