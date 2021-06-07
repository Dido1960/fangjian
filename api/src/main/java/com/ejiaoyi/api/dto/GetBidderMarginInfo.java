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
 * 接收投标人保证金缴纳情况参数 DTO
 *
 * @author Mike
 * @since 2020-12-29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "投标人保证金信息", description = "投标人保证金信息")
public class GetBidderMarginInfo extends BaseModel implements Serializable {

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

    @ApiModelProperty(value = "行政区划（如：酒泉市本级620901）", required = true, example = "620901", position = 3)
    @NotNull(message = "regCode不能为空")
    @Size(min = 1, max = 255, message = "行政区划长度 不合法")
    private String regCode;

    @ApiModelProperty(value = "投标人统一社会信用代码", required = true, example = "35135478102458941M", position = 4)
    @NotNull(message = "bidderOrgCode不能为空")
    @Size(min = 1, max = 255, message = "投标人统一社会信用代码长度 不合法")
    private String bidderOrgCode;

    @ApiModelProperty(value = "保证金递交方式（保证金缴纳：1 保函缴纳：2）", required = true, example = "2", position = 5)
    @NotNull(message = "marginReceiveWay不能为空")
    @Size(min = 1, max = 255, message = "保证金递交方式长度 不合法")
    private String marginReceiveWay;

    @ApiModelProperty(value = "保证金缴纳状态（已缴纳：1 未缴纳：0）", required = true, example = "1", position = 6)
    @NotNull(message = "marginReceiveStatus不能为空")
    @Size(min = 1, max = 255, message = "保证金缴纳状态长度 不合法")
    private String marginReceiveStatus;

    @ApiModelProperty(value = "保证金递交时间（格式：yyyy-MM-dd hh:mm:ss）", required = true, example = "2020-10-10 12:00:00", position = 7)
    @NotNull(message = "marginReceiveTime不能为空")
    @Size(min = 1, max = 255, message = "保证金递交时间长度 不合法")
    private String marginReceiveTime;

}