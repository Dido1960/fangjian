package com.ejiaoyi.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * HTTP 响应 DTO
 *
 * @author Z0001
 * @since 2020-5-11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HttpResponseDTO {

    /**
     * 返回值
     */
    private String content;

    /**
     * 状态值
     */
    private int code;
}
