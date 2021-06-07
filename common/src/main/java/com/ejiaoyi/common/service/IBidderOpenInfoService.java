package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.BidSection;
import com.ejiaoyi.common.entity.Bidder;
import com.ejiaoyi.common.entity.BidderOpenInfo;

import java.util.List;
import java.util.Map;

/**
 * 投标人开标 服务类
 * @author fengjunhong
 * @since 2020-7-17
 */
public interface IBidderOpenInfoService {

    /**
     * 获取投标人列表
     * @param bid 标段主键
     * @param isQualification 是否是资格预审
     * @return
     */
    List<BidderOpenInfo> listBidderOpenInfo(Integer bid, boolean isQualification);

    /**
     * 修改投标人开标信息
     * @param bidderOpenInfo 投标人开标信息
     * @return
     */
    boolean updateBidderOpenInfo(BidderOpenInfo bidderOpenInfo);

    /**
     * 获取一个投标人信息
     * @param bidderOpenInfo 投标人开标信息
     * @return
     */
    BidderOpenInfo getBidderOpenInfo(BidderOpenInfo bidderOpenInfo);


    /**
     * 通过id更新数据。如果标书拒绝更新IS_PASS_BID_OPEN（defaul:1） 为0
     * @param bidderOpenInfo
     * @return
     */
    Integer updateBidderOpenInfoById(BidderOpenInfo bidderOpenInfo);

    /**
     * 获取投标人某标段的开标信息
     * @param bidderId 投标人ID
     * @param bidSectionId 标段ID
     * @return
     */
    BidderOpenInfo getBidderOpenInfo(Integer bidderId, Integer bidSectionId);

    /**
     * @Description 通过id更新
     * @Date        2020-8-5 10:39
     */
    Integer updateById(BidderOpenInfo bidderOpenInfo);

    /**
     * 通过id获取
     * @param id
     * @return
     */
    BidderOpenInfo getBidderOpenInfoById(Integer id);

    /***
     * 新增BidderOpenInfo
     * **/
    Integer insert(BidderOpenInfo bidderOpenInfo);

    /***
     * 计算未
     * @param bidderIds
     * @param bidSectionId
     *
     * **/
    Integer coutListBidderOpenInfo(Integer bidSectionId, List<Integer> bidderIds, int decryptStatus);

    /**
     * 更新身份检查信息
     * @param boiId 更新的BidderOpenInfoId
     * @param checkType 更新的类型 identity:身份验证   marginPay：保证金验证
     * @param passType 是否通过
     * @return
     */
    Integer updateClientCheck(Integer boiId,String checkType,Integer passType);


    /**
     * 获取当前boi前后boi，
     * @param bidSectionId
     * @param bidderIdTo
     * @param i -1获取前boi，1获取后boi;
     * @return
     */
    BidderOpenInfo getAroundBoi(Integer bidSectionId, Integer bidderIdTo, int i);

    /**
     * 通过投标人列表获取当前 检查以及未检查人数
     * 判断方式，只要身份检查获取缴纳金检查有一个不为null或者空串则判断为已检查
     * @@param list 投标人列表
     * @return mao isCheck: 检查人数; notCheck: 未检查人数
     */
    Map<String, Integer> getCheckNumByList(List<Bidder> list);

    /**
     * 获取指定投标人的开标信息
     * @param bidders 投标人列表
     * @return
     */
    List<BidderOpenInfo> listBidderOpenInfo(List<Bidder> bidders);

    /**
     * 踢出群聊
     * @param bidderOpenInfo
     */
    void updateByBidderId(BidderOpenInfo bidderOpenInfo);

    /**
     * 获取有关复会的投标人列表
     * @param bidSectionId 标段ID
     * @return
     */
    List<Bidder> listBidderForMeeting(Integer bidSectionId);

    /**
     * 更新投标人复会状态
     * @param bidderOpenInfo
     * @return
     */
    Integer updateDissentStatus(BidderOpenInfo bidderOpenInfo, Integer bidSectionId);

    /**
     * 查询某个解密状态的投标人个数
     * @param bidSectionId 标段id
     * @param decryptStatus 解密状态
     * @return
     */
    Integer selectDecryptStatusCount(Integer bidSectionId, Integer decryptStatus);
}
