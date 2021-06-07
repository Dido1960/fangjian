package com.ejiaoyi.common.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

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
@ApiModel(value = "GetPageInfo", description = "GetPageInfo")
public class RestPageInfo<T> {

    private static final long serialVersionUID = 1L;

    @JsonProperty("total")
    @JSONField(name = "total")
    @ApiModelProperty(value = "总条数", required = true, example ="100")
    private long total;

    @JsonProperty("limit")
    @JSONField(name = "limit")
    @ApiModelProperty(value = "每页条数", required = true, example ="8")
    private int limit;

    @JsonProperty("page")
    @JSONField(name = "page")
    @ApiModelProperty(value = "页码", required = true, example ="1")
    private  int page;


    @JsonProperty("list")
    @JSONField(name = "list")
    @ApiModelProperty(value = "当前页码包含的数据", required = true, example ="[{},{},{}]")
    private List<T> list;

}
