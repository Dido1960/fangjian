package com.ejiaoyi.admin.dto;

import lombok.*;

/**
 * 投标信息数据
 *
 * @author Mike
 * @since 2021/4/6
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TenderInfoDTO {

    /**
     * 项目编号
     */
    private String projectCode;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 标段编号
     */
    private String bidSectionCode;

    /**
     * 标段名称
     */
    private String bidSectionName;

    /**
     * 投标人
     */
    private String bidderName;

    /**
     * 投标人统一社会信用代码
     */
    private String bidderCode;

    /**
     * 法定代表人或其委托代理人
     */
    private String legalAgent;

    /**
     * 总报价
     */
    private String priceTotal;

    /**
     * 工期
     */
    private String constructionDays;

    /**
     * 费率
     */
    private String rate;

    /**
     * 工程质量
     */
    private String quality;

}
