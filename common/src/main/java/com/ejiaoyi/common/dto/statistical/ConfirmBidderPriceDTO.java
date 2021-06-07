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
public class ConfirmBidderPriceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称", example = "加密文件.sgef", position = 1)
    private String companyName;

    /**
     * 投标报价
     */
    @ApiModelProperty(value = "投标报价", example = "1234567", position = 2)
    private String bidPrice;

    /**
     * 投标报价大写
     */
    @ApiModelProperty(value = "投标报价大写", example = "壹佰万", position = 3)
    private String bigBidPrice;

    /**
     * 身份证号码
     */
    @ApiModelProperty(value = "身份证号码", example = "155457", position = 3)
    private String idCard;

    /**
     * 法定授权委托人
     */
    @ApiModelProperty(value = "法定授权委托人", example = "张三", position = 3)
    private String sqwtrName;

}
