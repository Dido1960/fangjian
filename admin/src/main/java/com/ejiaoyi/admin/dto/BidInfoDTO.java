package com.ejiaoyi.admin.dto;

import lombok.*;

/**
 * 招标信息数据
 *
 * @author Mike
 * @since 2021/4/6
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BidInfoDTO {

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
     * 文件递交时间
     */
    private String bidDocReferEndTime;

    /**
     * 开标时间
     */
    private String bidOpenTime;

    /**
     * 招标人名称
     */
    private String tendererName;

    /**
     * 招标代理名称
     */
    private String agencyName;

    /**
     * 评标委员会人数
     */
    private String expertCount;

    /**
     * 业主代表人数
     */
    private String representativeCount;

}
