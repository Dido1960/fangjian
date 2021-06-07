package com.ejiaoyi.bidder.service;

import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.entity.BidSection;
import com.ejiaoyi.common.entity.BidderOpenInfo;

import java.util.Map;

/**
 * @Description:
 * @Auther: liuguoqiang
 * @Date: 2020-7-30 09:49
 */
public interface IBidFileService {
    /**
     * 文件解析判断，是否做过修改
     * @param bidderOpenInfo
     * @return
     */
    JsonData fileJudge(BidderOpenInfo bidderOpenInfo);

    /**
     * 通过标段ID和投标人id获取Boi
     * @param bidSectionId
     * @param bidderId
     * @return
     */
    BidderOpenInfo getBoiByIds(int bidSectionId, int bidderId);

    /**
     * 计算开标签到时间段
     * @param bidSectionId
     * @return
     */
    Map<String, String> getBidSectionTimes(int bidSectionId);

    /**
     * 计算开标文件上传时间段 默认开标前一天
     * @param bidSectionId
     * @return
     */
    Map<String, String> getBidSectionFileUploadTimes(int bidSectionId);

    /**
     * 纸质标文件解析判断，是否做过修改
     * @param bidderOpenInfo
     * @return
     */
    JsonData paperFileJudge(BidderOpenInfo bidderOpenInfo);
}
