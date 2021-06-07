package com.ejiaoyi.common.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 评标、开标报告推送DTO，用于数据封装
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PushReportDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 时间戳
     */
    private String timeStamp;

    /**
     * 校验字段
     */
    private String signData;

    /**
     * 标段编号
     */
    private String bidNo;

    /**
     * 文件名称
     */
    private String fileName;
}