package com.ejiaoyi.common.dto.statistical;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * 解密 投标文件
 *
 * @since 2021-04-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BidderFileDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件名称
     */
    @ApiModelProperty(value = "文件名称", example = "加密文件.sgef", position = 1)
    private String fileName;

    /**
     * 文件ID
     */
    @ApiModelProperty(value = "文件ID", example = "1", position = 2)
    private Integer fileId;

    /**
     * 文件类型
     */
    @ApiModelProperty(value = "文件类型 0（非加密文件） 1（加密文件） 2（手机证书加密文件）", example = "1", position = 3)
    private Integer fileType;

}
