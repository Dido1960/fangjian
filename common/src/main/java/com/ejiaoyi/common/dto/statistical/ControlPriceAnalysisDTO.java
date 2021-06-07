package com.ejiaoyi.common.dto.statistical;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * 控制价分析参数 DTO
 *
 * @author Mike
 * @since 2021-04-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ControlPriceAnalysisDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 投标人id
     */
    @ApiModelProperty(value = "投标人id", example = "1", position = 1)
    private Integer bidderId;

    /**
     * 投标人名称
     */
    @ApiModelProperty(value = "投标人名称", example = "mcz建筑投标测试一", position = 2)
    private String bidderName;

    /**
     * 投标报价
     */
    @ApiModelProperty(value = "投标报价", example = "88888", position = 3)
    private String bidPrice;

    /**
     * 控制价分析结果
     */
    @ApiModelProperty(value = "结果(1正常，2异常)", example = "1", position = 4)
    private Integer result;
}
