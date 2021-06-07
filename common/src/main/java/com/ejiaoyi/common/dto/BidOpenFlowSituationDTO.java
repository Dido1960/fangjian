package com.ejiaoyi.common.dto;

import lombok.*;

import java.io.Serializable;

/**
 * 开标流程完成情况DTO，用于数据封装
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BidOpenFlowSituationDTO implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * 开标流程名称
     */
    public String flowName;

    /**
     * 开标流程状态 1进行中  2已完成
     */
    public Integer flowStatus;

}
