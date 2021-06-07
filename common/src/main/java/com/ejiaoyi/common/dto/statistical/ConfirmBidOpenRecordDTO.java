package com.ejiaoyi.common.dto.statistical;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * 开标一览表确认参数 DTO
 *
 * @author Mike
 * @since 2021-04-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ConfirmBidOpenRecordDTO implements Serializable {

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
     * 投标报价
     */
    @ApiModelProperty(value = "投标报价", example = "88888", position = 3)
    private String bidPrice;

    /**
     * 投标保证金缴纳状态
     */
    @ApiModelProperty(value = "结果(已缴纳;未缴纳)", example = "已缴纳", position = 4)
    private Integer marginPayStatus;

    /**
     * 投标保证金缴纳(中文)
     */
    @ApiModelProperty(value = "保证金缴纳状态中文(已缴纳;保函缴纳;未缴纳)", example = "已缴纳", position = 5)
    private String marginPay;

    /**
     * 投标工期
     */
    @ApiModelProperty(value = "投标工期", example = "365天", position = 5)
    private String timeLimit;

    /**
     * 法人或授权委托人姓名
     */
    @ApiModelProperty(value = "法人授权委托人", example = "小美", position = 5)
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
