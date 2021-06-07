package com.ejiaoyi.agency.service;

import com.ejiaoyi.common.dto.EndingCheck;
import com.ejiaoyi.common.entity.*;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Auther: liuguoqiang
 * @Date: 2020/7/21 09:52
 */
public interface IClientCheckService {
    /**
     * 是否结束身份检查
     * @param bidSectionId
     * @return true结束
     */
    Boolean isEndCheck(Integer bidSectionId);

    /**
     * 判断是否为资格预审
     * @param bidSectionId
     * @return true是资格预审
     */
    Boolean isA10Bid(Integer bidSectionId);

    /**
     * 获取投标人列表
     * @param bidSectionId
     * @return
     */
    List<Bidder> listCheckBidder(Integer bidSectionId);

    /**
     * 通过投标人列表获取当前检查以及未见差人数
     * 判断方式，只要身份检查获取缴纳金检查有一个不为null或者空串则判断为已检查
     * @return mao isCheck: 检查人数; notCheck: 未检查人数
     */
    Map<String, Integer> getCheckNumByList(List<Bidder> list);

    /**
     * 更新身份检查
     * @param boiId 更新的BidderOpenInfoId
     * @param checkType 更新的类型 identity:身份验证   marginPay：保证金验证
     * @param passType 是否通过
     * @return
     */
    Integer updateClientCheck(Integer boiId,String checkType,Integer passType);

    /**
     * 通过BidderOpenInfo主键获取 BidderOpenInfo 信息
     * @param bioId
     * @return
     */
    BidderOpenInfo getBidderOpenInfoById(Integer bioId);

    /**
     * 获取当前boi前后boi，
     * @param bidSectionId
     * @param bidderIdTo
     * @param i -1获取前boi，1获取后boi;
     * @return
     */
    BidderOpenInfo getAroundBoi(Integer bidSectionId, Integer bidderIdTo, int i);

    /**
     * 通过bio的标段id，投标人id，异常类型获取异常理由，查不到返回null;
     * @param bidderOpenInfo
     * @param exceType
     * @return
     */
    BidderException getExceptionReson(BidderOpenInfo bidderOpenInfo, int exceType);

    /**
     * 更新是否检查结束
     * @param lineStatus
     * @return
     */
    boolean updateCheckStatus(LineStatus lineStatus);

    /**
     * 添加标书拒绝理由，通过id查询
     * @param bidderOpenInfo
     * @return
     */
    Integer addTenderReason(BidderOpenInfo bidderOpenInfo);

    /**
     * 初始化检查状态，第一次更新时，初始化检查状态
     * 判断依据，状态值为null则初始化为1
     * 未更新返回0
     * @param bidderIdTo
     * @return
     */
    Integer checkStatusInfo(Integer bidderIdTo);

    /**
     * 获取boi列表通过标段id，其中获取投标人异常原因
     * @param endingCheck
     * @return
     */
    List<BidderOpenInfo> getBoiListForReason(EndingCheck endingCheck);

    /**
     * 存储原因列表
     * 没有id则新增
     * @param list
     * @return
     */
    Boolean saveReasonList(List<BidderException> list);

    /**
     * 公布投标人环节是否结束
     * @param bidSectionId 标段id
     * @return
     */
    Boolean isPublishBidderEnd(Integer bidSectionId);
}
