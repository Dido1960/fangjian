package com.ejiaoyi.api.service;

import com.ejiaoyi.api.dto.*;
import com.ejiaoyi.common.entity.UploadFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IServicePlatformDockService extends IBaseService {

    /**
     * 接收文件
     * @param file 文件流
     * @param fileSM3 文件SM3值
     * @return 上传的文件信息
     */
    UploadFile receiveDocument(MultipartFile file, String fileSM3) throws Exception;

    /**
     * 接收项目信息存储到数据库
     *
     * @param getProjectInfo 接收的项目信息
     */
    void receiveProjectInfo(GetProjectInfo getProjectInfo) throws Exception;

    /**
     * 接收投标人信息存储到数据库
     *
     * @param getBidderInfo 接收的投标人信息
     */
    void receiveBidderInfo(GetBidderInfo getBidderInfo) throws Exception;

    /**
     * 接收投标人保证金缴纳信息
     * @param getBidderMarginInfo 接收得投标人保证金缴纳信息
     */
    void receiveMarginInfo(GetBidderMarginInfo getBidderMarginInfo) throws Exception;

    /**
     * 接收评标专家信息存储到数据库
     * @param getBidderExpertInfo 接收的评标专家信息
     */
    void receiveExpertInfo(GetExpertInfo getBidderExpertInfo) throws Exception;

    /**
     * 接收中标通知书信息
     * @param getWiningBidFileInfo 中标通知书其他信息
     */
    void receiveWiningBidDocument(GetWiningBidFileInfo getWiningBidFileInfo) throws Exception;
}
