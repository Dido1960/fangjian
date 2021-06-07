package com.ejiaoyi.common.dto.statistical;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * 标讯信息参数 DTO
 *
 * @author Mike
 * @since 2021-04-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EnterSignInDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 投标人是否参标 (1:参标 0：未参标)
     */
    @ApiModelProperty(value = "投标人是否参标", example = "1", position = 1)
    private Integer participateStatus;

    /**
     * 文件是否上传成功 (1:成功 0：未上传)
     */
    @ApiModelProperty(value = "文件是否上传成功", example = "1", position = 2)
    private Integer fileUploadStatus;

    /**
     * 文件上传回执单地址
     */
    @ApiModelProperty(value = "文件上传回执单", example = "http://www.lyjcd.cn:8089/***.pdf", position = 3)
    private String receiptUrl;

    /**
     * 签到状态(1:认证成功  0：未签到  2：紧急签到成功)
     */
    @ApiModelProperty(value = "签到状态", example = "1", position = 4)
    private Integer signStatus;

    /**
     * 当前能否进行签到 (1:可以签到  0：未到签到时间  2：签到时间已过)
     */
    @ApiModelProperty(value = "当前能否进行签到", example = "1", position = 5)
    private Integer canDoSignStatus;

    /**
     * 投标人ID
     */
    @ApiModelProperty(value = "投标人ID", example = "1", position = 6)
    private Integer bidderId;
}
