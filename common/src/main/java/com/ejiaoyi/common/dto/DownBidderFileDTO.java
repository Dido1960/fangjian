package com.ejiaoyi.common.dto;

import com.ejiaoyi.common.entity.Fdfs;
import lombok.*;

import java.io.Serializable;

/**
 * 下载投标人文件DTO，用于数据封装
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DownBidderFileDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 投标人id
     */
    private Integer bidderId;

    /**
     * 投标人名称
     */
    private String bidderName;

    /**
     * 商务pdf文件
     */
    private Fdfs businessFilePdf;

    /**
     * 技术pdf文件
     */
    private Fdfs technicalFilePdf;

    /**
     * 资格证明文件
     */
    private Fdfs qualificationFilePdf;

    /**
     * 工程量清单 pdf文件
     */
    private Fdfs engineerQuantityListPdf;

    /**
     * 工程量清单 xml文件
     */
    private Fdfs engineerQuantityListXML;

    /**
     * 纸质标 PDF文件
     */
    private Fdfs paperBidderPdf;

    /**
     * gef投标文件
     */
    private Fdfs gefFdfs;

    /**
     * sgef投标文件
     */
    private Fdfs sgefFdfs;

    /**
     * czr存证文件
     */
    private Fdfs czrFdfs;

}