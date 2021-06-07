package com.ejiaoyi.common.dto;

import lombok.*;

import java.io.Serializable;

/**
 * 评审流程完成情况
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProcessCompletionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 小组评审完成的grade数量
     */
    private Integer completeNum;

    /**
     * 小组评审未完成的grade数量
     */
    private Integer noCompleteNum;

}
