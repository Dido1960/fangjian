package com.ejiaoyi.common.dto.statistical;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * 投标人名单参数 DTO
 *
 * @author yyn
 * @since 2021-04-08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BidderListDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 投标人id
     */
    @ApiModelProperty(value = "投标人id", example = "1", position = 1)
    private Integer id;

    /**
     * 投标人名称
     */
    @ApiModelProperty(value = "投标人名称", example = "mcz建筑投标测试一", position = 2)
    private String bidderName;

    /**
     * 投标保证金缴纳状态
     */
    @ApiModelProperty(value = "保证金缴纳状态(1已缴纳;2保函缴纳;其它:未缴纳)", example = "1", position = 4)
    private Integer marginPayStatus;

    /**
     * 投标保证金缴纳(中文)
     */
    @ApiModelProperty(value = "保证金缴纳状态中文(已缴纳;保函缴纳;未缴纳)", example = "已缴纳", position = 5)
    private String marginPay;

    /**
     * 法人或授权委托人姓名
     */
    @ApiModelProperty(value = "法人授权委托人", example = "小美", position = 6)
    private String clientName;



    /**
     * 保证金缴纳状态
     *
     * @return 启用状态名称
     */
    public String getMarginPay() {
        return this.marginPayStatus == null ? "未缴纳" :
                this.marginPayStatus == 0 ? "未缴纳" :
                        this.marginPayStatus == 1 ? "已缴纳" :
                                this.marginPayStatus == 2 ? "保函缴纳" :
                                        "未缴纳";
    }

}
