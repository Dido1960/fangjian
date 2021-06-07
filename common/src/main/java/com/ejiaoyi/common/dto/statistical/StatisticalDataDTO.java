package com.ejiaoyi.common.dto.statistical;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 统计项目数据DTO，用于数据封装
 * @author Mike
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class StatisticalDataDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 项目总数
     */
    private Integer totalBidSectionNum;

    /**
     * 正常项目数
     */
    private Integer normalBidSectionNum;

    /**
     * 异常项目数
     */
    private Integer abnormalBidSectionNum;

    /**
     * 统计区划详细
     */
    private List<RegDetailDTO> regDetailDTOList;

    /**
     * 标段类型详情
     */
    private List<BidTypeDetailDTO> bidTypeDetailDTOList;
}