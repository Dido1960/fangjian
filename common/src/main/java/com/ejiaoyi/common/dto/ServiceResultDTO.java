package com.ejiaoyi.common.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 服务状态DTO
 *
 * @author Kevin
 * @since 2020-12-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ServiceResultDTO implements Serializable {

    /**
     * 服务状态
     */
    @JsonProperty("state")
    @JSONField(name = "state")
    @ApiModelProperty(value = "服务状态", position = 1, example = "active")
    private String state;

    /**
     * 服务进度
     */
    @JsonProperty("process")
    @JSONField(name = "process")
    private String process;
}
