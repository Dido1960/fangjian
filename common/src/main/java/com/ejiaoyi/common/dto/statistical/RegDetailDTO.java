package com.ejiaoyi.common.dto.statistical;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 统计区划项目详细情况DTO，用于数据封装
 * @author Mike
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RegDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 区划名称
     */
    private String regName;

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