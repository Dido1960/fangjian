package com.ejiaoyi.api.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.ejiaoyi.common.dto.BaseModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 接受投标人解密信息 方法 DTO
 *
 * @author Z0001
 * @since 2020-4-22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "GetBidderDecryptParam", description = "投标人解密信息")
public class GetBidderDecryptParam extends BaseModel {

    private static final long serialVersionUID = 1L;

    @JsonProperty("bidderId")
    @JSONField(name = "bidderId")
    @ApiModelProperty(value = "投标人id", position = 1, required = true, example = "1")
    @NotNull(message = "投标人id不可为空")
    private Integer bidderId;

    @JsonProperty("bidSectionId")
    @JSONField(name = "bidSectionId")
    @ApiModelProperty(value = "标段id", position = 2, required = true, example = "1")
    @NotNull(message = "标段id不可为空")
    private Integer bidSectionId;

    @JsonProperty("fileId")
    @JSONField(name = "fileId")
    @ApiModelProperty(value = "文件id", position = 3, required = true, example = "22")
    @NotNull(message = "文件id不可为空")
    private Integer fileId;

    @JsonProperty("fileType")
    @JSONField(name = "fileType")
    @ApiModelProperty(value = "文件类型（0非加密文件，1: 加密文件 2：手机加密文件）", position = 4, required = true, example = "0")
    @NotNull(message = "文件类型不可为空")
    private Integer fileType;

    @JsonProperty("phoneCertNo")
    @JSONField(name = "phoneCertNo")
    @ApiModelProperty(value = "手机证书编号", position = 5, required = true, example = "000000")
    private String phoneCertNo;
}
