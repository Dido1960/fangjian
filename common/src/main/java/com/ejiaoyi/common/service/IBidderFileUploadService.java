package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.Bidder;

import java.io.File;
import java.util.List;

/**
 * 投标文件解密后 上传接口
 * @Auther: liuguoqiang
 * @Date: 2021-1-5 11:35
 */
public interface IBidderFileUploadService {
    /**
     * 投标文件上传
     * @param bidderId 投标人Id
     * @param fileId 解密的文件Id
     * @param file 投标文件
     * @param isConstruction 是否为施工项目
     */
    void bidderFileUpload(Integer bidderId, Integer fileId, File file, boolean isConstruction);

    /**
     * 纸质标文件上传
     * @param bidderId 投标人Id
     * @param fileId 解密的文件Id
     * @param file 投标文件
     */
    void paperBidderFileUpload(Integer bidderId, Integer fileId, File file);

    /**
     * 文件重新上传
     * @param bidderId 投标人id
     * @param fileType 需要上传的文件类型
     */
    Boolean bidderFileReUpload(Integer bidderId, Integer fileType);

    /**
     * 纸质标文件重新上传
     * @param bidderId 投标人Id
     * @param fileType 文件类型 1 ：pdf 2 其他类型文件
     * @return
     */
    Boolean paperBidderFileReUpload(Integer bidderId, Integer fileType);


    /**
     * 得到列表结核病xml的url
     * 获取当前标段，所有投标人工程量清单url
     *
     * @param bidSectionId 报价部分id
     * @param bidder       投标人
     * @return {@link String}
     */
    String getTbXmlUrl(Integer bidSectionId, Bidder bidder);
}
