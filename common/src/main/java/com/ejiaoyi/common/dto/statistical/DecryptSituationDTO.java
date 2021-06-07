package com.ejiaoyi.common.dto.statistical;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * 解密信息
 * @since 2021-04-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DecryptSituationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 解密状态 (0:未解密  1:解密成功  2:解密失败  3：进行中)
     */
    @ApiModelProperty(value = "解密状态", example = "1", position = 1)
    private Integer decryptStatus;

    /**
     * 解密失败 提示原因
     */
    @ApiModelProperty(value = "解密失败 提示原因", example = "1", position = 2)
    private String decryptFailMsg;

    /**
     * 解密中排队人数
     */
    @ApiModelProperty(value = "解密中排队人数", example = "1", position = 3)
    private Integer queueCount;

    /**
     * 解密最后用时
     */
    @ApiModelProperty(value = "解密最后用时", example = "1秒", position = 4)
    private String decryptTime;
}
