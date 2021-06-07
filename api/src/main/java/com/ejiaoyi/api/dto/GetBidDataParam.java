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

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

/**
 * getToken 方法 DTO
 *
 * @author Z0001
 * @since 2020-4-22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "GetBidDataParam", description = "标段查询参数")
public class GetBidDataParam extends GetPageInfo {

    private static final long serialVersionUID = 1L;

    /**
     * 几天内开标项目
     */
    @JsonProperty("bidOpenScopeDay")
    @JSONField(name = "bidOpenScopeDay")
    @ApiModelProperty(value = "几天内开标项目,大于0的整数", position = 1, required = true, example = "1")
    private Integer bidOpenScopeDay;

    /**
     * 开标状态
     */
    @JsonProperty("bidOpenStatus")
    @JSONField(name = "bidOpenStatus")
    @ApiModelProperty(value = "开标状态(0:未开标,1:正在开标,2:开标结束)", position = 1, required = true, example = "0")
    private Integer bidOpenStatus;

    /**
     * 项目编号或名称
     */
    @JsonProperty("projectCodeOrName")
    @JSONField(name = "projectCodeOrName")
    @ApiModelProperty(value = "项目编号或名称", position = 1, required = true, example = "JYT-SG-TEST-00101")
    private String projectCodeOrName;

}
