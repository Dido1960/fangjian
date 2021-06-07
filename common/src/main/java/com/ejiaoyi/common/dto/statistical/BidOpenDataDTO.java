package com.ejiaoyi.common.dto.statistical;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 手机 开标公用数据
 *
 * @author Mike
 * @since 2021-04-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BidOpenDataDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 开标流程 流程列表
     */
    @ApiModelProperty(value = "开标流程 流程列表", position = 1)
    private List<BidOpenProcessDTO> processList;

    /**
     * 投标人 是否被标书拒绝
     */
    @ApiModelProperty(value = "投标人 是否被标书拒绝 1：标书拒绝 0：标书未被拒绝", example = "1", position = 2)
    private Integer tenderRejection;

    /**
     * 投标人 标书拒绝原因
     */
    @ApiModelProperty(value = "投标人 标书拒绝原因", example = "1234567", position = 2)
    private String tenderRejectionReason;
}
