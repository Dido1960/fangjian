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
public class BidSectionInformationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 交易中心
     */
    @ApiModelProperty(value = "交易中心", example = "兰州市本级交易中心", position = 1)
    private String tradingCenter;

    /**
     * 招标单位
     */
    @ApiModelProperty(value = "招标单位", example = "肃北县自然资源局", position = 2)
    private String biddingUnit;

    /**
     * 项目地址
     */
    @ApiModelProperty(value = "项目地址", example = "兰州市", position = 3)
    private String projectAddress;

    /**
     * 开标时间
     */
    @ApiModelProperty(value = "开标时间", example = "2021-01-30 16:46:14", position = 4)
    private String bidOpenTime;

    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称", example = "甘肃省交通道路改造项目", position = 5)
    private String projectName;

    /**
     * 项目编号
     */
    @ApiModelProperty(value = "项目编号", example = "JQZFDCG202101300001", position = 6)
    private String projectCode;

    /**
     * 招标人
     */
    @ApiModelProperty(value = "招标人", example = "李乐乐", position = 7)
    private String tenderer;

    /**
     * 代理机构
     */
    @ApiModelProperty(value = "代理机构", example = "链易见代理机构", position = 8)
    private String agencyMechanism;

    /**
     * 招标代表
     */
    @ApiModelProperty(value = "招标代表", example = "1人", position = 9)
    private String tenderCount;

    /**
     * 评标委员会
     */
    @ApiModelProperty(value = "评标委员会", example = "5人", position = 10)
    private String expertCount;

    /**
     * 招标方式
     */
    @ApiModelProperty(value = "招标方式", example = "公开招标", position = 11)
    private String tenderMethod;

    /**
     * 评标办法
     */
    @ApiModelProperty(value = "评标办法", example = "综合评标办法", position = 12)
    private String evalMethod;

    /**
     * 标段名称
     */
    @ApiModelProperty(value = "标段名称", example = "甘肃省交通道路改造项目一段", position = 13)
    private String bidSectionName;

    /**
     * 标段编号
     */
    @ApiModelProperty(value = "标段编号", example = "jyt-kccexm-test", position = 14)
    private String bidSectionCode;

    /**
     * 招标类别
     */
    @ApiModelProperty(value = "招标类别", example = "施工", position = 15)
    private String tenderCategory;

    /**
     * 标段内容
     */
    @ApiModelProperty(value = "标段内容", example = "工程-监理", position = 16)
    private String bidContent;

    /**
     * 开标状态 (0:未开始 1:进行中 2:结束)
     */
    @ApiModelProperty(value = "开标状态", example = "0", position = 17)
    private Integer bidOpenStatus;

    /**
     * 直播地址
     */
    @ApiModelProperty(value = "直播地址", example = "123456789", position = 18)
    private String liveRoom;

    /**
     * 复会状态 (0:未开始 1:进行中 2:结束)
     */
    @ApiModelProperty(value = "复会状态", example = "0", position = 19)
    private Integer resumeStatus;

    /**
     * 代理联系方式
     */
    @ApiModelProperty(value = "代理联系方式", example = "123456789", position = 20)
    private String agentPhone;
}
