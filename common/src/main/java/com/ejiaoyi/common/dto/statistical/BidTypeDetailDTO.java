package com.ejiaoyi.common.dto.statistical;

import lombok.*;

import java.io.Serializable;

/**
 * 统计标段类型详细情况DTO，用于数据封装
 * @author Mike
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BidTypeDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 标段类型
     */
    private String bidType;

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

}