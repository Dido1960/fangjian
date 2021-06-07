package com.ejiaoyi.common.dto.statistical;

import com.ejiaoyi.common.enums.BidOpenProcessPhone;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 手机 开标流程
 *
 * @author Mike
 * @since 2021-04-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BidOpenProcessDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 开标流程详情
     */
    @ApiModelProperty(value = "开标流程详情", position = 1)
    private BidOpenProcessPhone process;

    /**
     * 流程状态
     */
    @ApiModelProperty(value = "流程状态 0：未开始 1：进行中 2：结束", example = "1", position = 2)
    private Integer processStatus;

}
