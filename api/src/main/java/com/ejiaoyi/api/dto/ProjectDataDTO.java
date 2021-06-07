package com.ejiaoyi.api.dto;

import lombok.*;

import java.io.Serializable;

/**
 * 统计项目数据DTO，用于数据封装
 * @author Mike
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProjectDataDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 标段名称
     */
    private String bidSectionName;

    /**
     * 标段编号
     */
    private String bidSectionCode;

    /**
     * 标段类型
     */
    private String bidType;

    /**
     * 开标时间
     */
    private String bidOpenTime;

    /**
     * 开标总用时(时-分-秒)
     */
    private String bidOpenTotalTime;

    /**
     * 评标总用时(时-分-秒)
     */
    private String bidEvalTotalTime;

    /**
     * 区划名称
     */
    private String regName;

    /**
     * 总投标人数
     */
    private Integer totalBidderNum;

    /**
     * 有效投标人数
     */
    private Integer effectiveBidderNum;

    /**
     * 运维情况
     */
    private OperationMaintenanceDTO operationMaintenanceDTO;
}