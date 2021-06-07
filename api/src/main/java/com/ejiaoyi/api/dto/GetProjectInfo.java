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
 * 接收投标人信息参数 DTO
 *
 * @author Mike
 * @since 2020-12-28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "项目信息", description = "项目信息")
public class GetProjectInfo extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "项目编号", required = true, example = "JYT-SG-TEST-001", position = 1)
    @NotNull(message = "projectCode不能为空")
    @Size(min = 1, max = 255, message = "项目编号长度 不合法")
    private String projectCode;

    @ApiModelProperty(value = "项目名称", required = true, example = "交易通施工测试项目001", position = 2)
    @NotNull(message = "projectName不能为空")
    @Size(min = 1, max = 300, message = "项目名称长度 不合法")
    private String projectName;

    @ApiModelProperty(value = "项目地址", required = true, example = "甘肃省兰州市城关区", position = 3)
    @NotNull(message = "address不能为空")
    @Size(min = 1, max = 255, message = "项目地址长度 不合法")
    private String address;

    @ApiModelProperty(value = "资金来源", required = true, example = "自有资金", position = 4)
    @NotNull(message = "sourceFund不能为空")
    @Size(min = 1, max = 255, message = "资金来源长度 不合法")
    private String sourceFund;

    @ApiModelProperty(value = "出资比例", required = true, example = "自有资金：100%", position = 5)
    @NotNull(message = "contributionScale不能为空")
    @Size(min = 1, max = 255, message = "出资比例长度 不合法")
    private String contributionScale;

    @ApiModelProperty(value = "联系人", required = true, example = "张三", position = 6)
    @NotNull(message = "contactPerson不能为空")
    @Size(min = 1, max = 255, message = "联系人长度 不合法")
    private String contactPerson;

    @ApiModelProperty(value = "联系方式", required = true, example = "15522055505", position = 7)
    @NotNull(message = "contactInformation不能为空")
    @Size(min = 1, max = 255, message = "联系方式长度 不合法")
    private String contactInformation;

    @ApiModelProperty(value = "招标人名称", required = true, example = "", position = 8)
    @NotNull(message = "tenderName不能为空")
    @Size(min = 1, max = 255, message = "招标人名称长度 不合法")
    private String tenderName;

    @ApiModelProperty(value = "招标人统一社会信用代码", required = true, example = "95135478102458941M", position = 9)
    private String tenderCode;

    @ApiModelProperty(value = "招标方式(公开招标：1 邀请招标：2)", required = true, example = "1", position = 10)
    @NotNull(message = "tenderMode不能为空")
    @Size(min = 1, max = 255, message = "招标人统一社会信用代码长度 不合法")
    private String tenderMode;

    @ApiModelProperty(value = "代理机构名称", required = true, example = "兰州代理公司", position = 11)
    @NotNull(message = "tenderAgencyName不能为空")
    @Size(min = 1, max = 255, message = "代理机构名称长度 不合法")
    private String tenderAgencyName;

    @ApiModelProperty(value = "代理机构统一社会信用代码", required = true, example = "35135478102458941M", position = 12)
    @NotNull(message = "tenderAgencyCode不能为空")
    @Size(min = 18, max = 18, message = "代理机构统一社会信用代码长度 不合法")
    private String tenderAgencyCode;

    @ApiModelProperty(value = "标段编号", required = true, example = "JYT-SG-TEST-00101", position = 13)
    @NotNull(message = "bidSectionCode不能为空")
    @Size(min = 1, max = 255, message = "标段编号长度 不合法")
    private String bidSectionCode;

    @ApiModelProperty(value = "标段名称", required = true, example = "交易通施工测试项目001一标段", position = 14)
    @NotNull(message = "bidSectionName不能为空")
    @Size(min = 1, max = 300, message = "标段名称长度 不合法")
    private String bidSectionName;

    @ApiModelProperty(value = "标段分类代码(勘察：A03，设计：A04，监理：A05，工程施工：A08，资格预审：A10，电梯采购与安装：A11，EPC：A12)"
            , required = true, example = "A08", position = 15)
    @NotNull(message = "bidSectionClassifyCode不能为空")
    @Size(min = 1, max = 255, message = "标段分类代码长度 不合法")
    private String bidSectionClassifyCode;

    @ApiModelProperty(value = "招标文件唯一标识码（请求‘receiveDocument’接口成功后返回的唯一标识码）", required = true,
            example = "f7b44ee0-2e27-415c-aea2-79d121f43d64", position = 16)
    @NotNull(message = "bidFileUid不能为空")
    @Size(min = 1, max = 255, message = "招标文件唯一标识码 不合法")
    private String bidFileUid;

    @ApiModelProperty(value = "澄清文件唯一标识码（请求‘receiveDocument’接口成功后返回的唯一标识码）", required = true,
            example = "f7b44ee0-2e27-415c-aea2-79d121f43d64", position = 17)
    private String clarifyFileUid;

    @ApiModelProperty(value = "是否远程异地评标（1:是， 0：否）", required = true, example = "1", position = 18)
    @NotNull(message = "remoteEvaluation不能为空")
    @Size(min = 1, max = 1, message = "是否远程异地评标（1:是， 0：否）长度 不合法")
    private String remoteEvaluation;

    @ApiModelProperty(value = "主场行政区划（如：酒泉市本级620901）", required = true, example = "620901", position = 19)
    @NotNull(message = "regCode不能为空")
    @Size(min = 6, max = 8, message = "主场行政区划长度 不合法")
    private String regCode;

    @ApiModelProperty(value = "客场行政区划(非远程异地评标，该字段可以传空)", required = false, example = "620903", position = 20)
    private String awayRegCode;
}
