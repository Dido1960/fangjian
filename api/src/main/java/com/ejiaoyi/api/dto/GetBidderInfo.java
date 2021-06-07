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
 * 接收项目信息参数 DTO
 *
 * @author Mike
 * @since 2020-12-29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "投标人报名信息", description = "投标人报名信息（以下字段内容需要使用国密算法进行加密）")
public class GetBidderInfo extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标段编号", required = true, example = "JYT-SG-TEST-00101", position = 1)
    @NotNull(message = "bidSectionCode不能为空")
    @Size(min = 1, max = 255, message = "标段编号长度 不合法")
    private String bidSectionCode;

    @ApiModelProperty(value = "标段分类代码(勘察：A03，设计：A04，监理：A05，工程施工：A08，资格预审：A10，电梯采购与安装：A11，EPC：A12)"
            , required = true, example = "A08", position = 2)
    @NotNull(message = "bidSectionClassifyCode不能为空")
    @Size(min = 1, max = 255, message = "标段分类代码长度 不合法")
    private String bidSectionClassifyCode;

    @ApiModelProperty(value = "行政区划（如：酒泉市本级620901）", required = true, example = "620901", position = 3)
    @NotNull(message = "regCode不能为空")
    private String regCode;

    @ApiModelProperty(value = "投标人统一社会信用代码", required = true, example = "35135478102458941M", position = 4)
    @NotNull(message = "bidderOrgCode不能为空")
    @Size(min = 18, max = 18, message = "投标人统一社会信用代码长度 不合法")
    private String bidderOrgCode;

    @ApiModelProperty(value = "投标人名称", required = true, example = "兰州XXX商贸责任有限公司", position = 5)
    @NotNull(message = "bidderName不能为空")
    @Size(min = 1, max = 255, message = "投标人名称长度 不合法")
    private String bidderName;

    @ApiModelProperty(value = "投标单位项目负责人", required = true, example = "王二", position = 6)
    @NotNull(message = "bidManager不能为空")
    @Size(min = 1, max = 255, message = "投标单位项目负责人 不合法")
    private String bidManager;

    @ApiModelProperty(value = "法人或授权委托人", required = true, example = "张三", position = 7)
    @NotNull(message = "legalPerson不能为空")
    @Size(min = 1, max = 255, message = "法人或授权委托人 不合法")
    private String legalPerson;

    @ApiModelProperty(value = "联系电话", required = true, example = "13322548710", position = 8)
    @NotNull(message = "phone不能为空")
    @Size(min = 1, max = 255, message = "联系电话 不合法")
    private String phone;
}