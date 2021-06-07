package com.ejiaoyi.common.dto;

import com.ejiaoyi.common.entity.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 解析投标文件数据封装DTO
 * @author Make
 * @since 2020/12/28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ParseBidFileDTO implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * 项目信息
     */
    private Project project;

    /**
     * 招标项目信息
     */
    private TenderProject tenderProject;

    /**
     * 标段信息
     */
    private BidSection bidSection;

    /**
     * 标段关联信息
     */
    private BidSectionRelate bidSectionRelate;

    /**
     * 招标文件信息
     */
    private TenderDoc tenderDoc;

    /**
     * 报价得分计算参数
     */
    private CalcScoreParam calcScoreParam;

    /**
     * 企业信息
     */
    private CompanyUser companyUser;

}
