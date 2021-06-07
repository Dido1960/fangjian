package com.ejiaoyi.api.dto;

import com.ejiaoyi.common.dto.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 中标通知书信息参数 DTO
 *
 * @author Mike
 * @since 2020-12-30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "中标通知书信息", description = "中标通知书信息")
public class GetWiningBidFileInfo extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标段编号", required = true, example = "JYT-SG-TEST-00101", position = 1)
    @NotNull(message = "bidSectionCode不能为空")
    @Size(min = 1, max = 255, message = "标段编号长度 不合法")
    private String bidSectionCode;

    @ApiModelProperty(value = "标段类型(勘察：A03，设计：A04，监理：A05，工程施工：A08，资格预审：A10，电梯采购与安装：A11，EPC：A12)", required = true,
            example = "A08", position = 2)
    @NotNull(message = "bidSectionClassifyCode不能为空")
    @Size(min = 1, max = 255, message = "标段类型长度 不合法")
    private String bidSectionClassifyCode;

    @ApiModelProperty(value = "标段所属行政区划（如：酒泉市本级620901）", required = true, example = "620901", position = 3)
    @NotNull(message = "regCode不能为空")
    @Size(min = 1, max = 255, message = "行政区划长度 不合法")
    private String regCode;

    @ApiModelProperty(value = "中标通知书文件唯一标识码（请求‘receiveDocument’接口成功后返回的唯一标识码）", required = true,
            example = "f7b44ee0-2e27-415c-aea2-79d121f43d64", position = 16)
    @NotNull(message = "regCode不能为空")
    private String winingBidFileUid;
}