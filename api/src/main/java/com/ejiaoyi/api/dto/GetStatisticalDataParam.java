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
 * 请求统计项目情况所需参数 DTO
 *
 * @author Mike
 * @since 2021-03-23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "统计项目情况", description = "统计项目情况")
public class GetStatisticalDataParam extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "筛选类型（按标段类型筛选：1， 按区划筛选：2）", required = true, example = "1", position = 1)
    @NotNull(message = "filterType不能为空")
    @Size(min = 1, max = 2, message = "筛选类型长度 不合法")
    private String filterType;

    @ApiModelProperty(value = "标段分类代码(勘察：A03，设计：A04，监理：A05，工程施工：A08，资格预审：A10，电梯采购与安装：A11，EPC：A12) ,传空不筛选"
            , example = "A08", position = 2)
    private String bidSectionClassifyCode;

    @ApiModelProperty(value = "行政区划（如：酒泉市本级）,传空不筛选", example = "酒泉市", position = 3)
    private String regName;

    @ApiModelProperty(value = "筛选时间（格式：yyyy 或 yyyy-MM 或 yyyy-MM-dd） ,传空不筛选", example = "2020-10", position = 4)
    private String filterTime;
}