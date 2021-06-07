package com.ejiaoyi.common.dto.clearV3;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目信息
 * @author fengjunhong
 * @version 1.0
 * @date 2021-4-21 15:12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {

    /**
     * 令牌
     */
    private String token;

    /**
     * 业务code
     */
    private String ywCode;

    /**
     * 清标模块
     */
    private String clearModule;
}
