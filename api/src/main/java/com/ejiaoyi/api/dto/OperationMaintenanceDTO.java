package com.ejiaoyi.api.dto;

import lombok.*;

import java.io.Serializable;

/**
 * <p>
 * 运维跟踪记录DTO
 * </p>
 *
 * @author Mike
 * @since 2021-03-23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OperationMaintenanceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 运维人员名称
     */
    private String operationMaintenanceName;

    /**
     * 是否异常(1:正常   0：异常)
     */
    private String abnormal;

    /**
     * 异常处理时长（分钟）
     */
    private Integer abnormalTime;

    /**
     * 跟踪记录
     */
    private String remark;


}
