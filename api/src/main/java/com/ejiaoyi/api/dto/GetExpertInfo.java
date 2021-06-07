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
 * 接收评标专家信息参数 DTO
 *
 * @author Mike
 * @since 2020-12-30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "评标专家信息", description = "评标专家信息")
public class GetExpertInfo extends BaseModel implements Serializable {

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

    @ApiModelProperty(value = "专家名称", required = true, example = "赵六", position = 4)
    @NotNull(message = "expertName不能为空")
    @Size(min = 1, max = 255, message = "专家名称长度 不合法")
    private String expertName;

    @ApiModelProperty(value = "专家评标行政区划（如：酒泉市本级620901）", required = true, example = "620901", position = 4)
    @NotNull(message = "expertRegCode不能为空")
    @Size(min = 1, max = 255, message = "专家评标行政区划长度 不合法")
    private String expertRegCode;

    @ApiModelProperty(value = "手机号码", required = true, example = "15566354810", position = 5)
    @NotNull(message = "phone不能为空")
    @Size(min = 1, max = 20, message = "手机号码长度 不合法")
    private String phone;

    @ApiModelProperty(value = "证件号码(居民身份证)", required = true, example = "110101199003074610", position = 6)
    @NotNull(message = "idCard不能为空")
    @Size(min = 18, max = 18, message = "证件号码(居民身份证)长度 不合法")
    private String idCard;

    @ApiModelProperty(value = "工作单位", required = true, example = "酒泉某某某责任有限公司", position = 7)
    @NotNull(message = "company不能为空")
    @Size(min = 1, max = 255, message = "工作单位长度 不合法")
    private String company;

    @ApiModelProperty(value = "专家类别（1：经济标专家 2：技术标专家 3：招标人代表）", required = true, example = "1", position = 8)
    @NotNull(message = "category不能为空")
    @Size(min = 1, max = 255, message = "专家类别长度 不合法")
    private String category;

    @ApiModelProperty(value = "数据类型(1:正常抽取专家 2:补抽专家)", required = true, example = "1", position = 9)
    @NotNull(message = "dataType不能为空")
    @Size(min = 1, max = 255, message = "数据类型长度 不合法")
    private String dataType;

    @ApiModelProperty(value = "是否可用（0：不可用  1：可用）", required = true, example = "0", position = 10)
    @NotNull(message = "enabled不能为空")
    @Size(min = 1, max = 255, message = "是否可用长度 不合法")
    private String enabled;

    @ApiModelProperty(value = "不可用原因", required = true, example = "由于某些原因，专家不能到场", position = 11)
    @Size(min = 1, max = 255, message = "不可用原因长度 不合法")
    private String unavailableReason;
}