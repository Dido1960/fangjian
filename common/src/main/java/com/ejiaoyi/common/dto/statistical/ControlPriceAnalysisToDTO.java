package com.ejiaoyi.common.dto.statistical;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 控制价分析Total参数 DTO
 *
 * @author Mike
 * @since 2021-04-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ControlPriceAnalysisToDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 招标控制价
     */
    @ApiModelProperty(value = "控制价", example = "8888", position = 1)
    private String controlPrice;

    /**
     * 投标人集合
     */
    @ApiModelProperty(value = "投标人集合", example = "返回数组对象", position = 2)
    private List<ControlPriceAnalysisDTO> bidders;

}
