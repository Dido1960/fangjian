package com.ejiaoyi.bidder.service;

import com.ejiaoyi.common.entity.BidSection;
import com.ejiaoyi.common.entity.Bidder;

import java.util.List;

/**
 * 投标人模块服务类
 *
 * @author Make
 * @since 2020-08-05
 */
public interface IBidderModelService {

    /**
     * 通过标段id和投标人id获取不见面开标投标人线上流程的完成情况
     *
     * @param bidder 封装标段id和投标人id
     * @return
     */
    List<String> listOnlineProcessComplete(Bidder bidder);

    /**
     * 计算排队时间
     * @param queueStartTime 开始时间
     * @param queueEndTime 技术时间
     * @return 排队时间秒
     */
    Long getQueueTime(String queueStartTime, String queueEndTime);

    /**
     * 获取开标大流程进行情况
     *
     * @param bidSectionId 标段id
     * @return
     */
    List<String> listBidderBaseFlow(Integer bidSectionId);


    /**
     * 获取不见面开标大厅项目信息
     * @return
     */
    List<Object> listBidOpenHallBidSection();

}
