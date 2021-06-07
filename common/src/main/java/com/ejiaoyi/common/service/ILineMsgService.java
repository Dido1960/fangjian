package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.LineMsg;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.List;

/**
 * 网上开标消息 服务类
 *
 * @author Make
 * @date 2020/8/6 17:24
 */
public interface ILineMsgService {

    /**
     * 新增网上开标消息
     * @param lineMsg 网上开标消息
     * @return
     */
    boolean addLineMsg(LineMsg lineMsg);

    /**
     * 获取所有消息记录
     *
     * @param bidSectionId 标段id
     * @return 结果集
     */
    List<LineMsg> listLineMsg(Integer bidSectionId);

    /**
     * 获取最后一条消息记录
     * @param bidSectionId
     * @return
     */
    LineMsg getLastLineMsg(Integer bidSectionId);

    /**
     * 更新消息记录
     * @param lineMsg 需要更新的消息记录
     * @return 是否更新成功
     */
    boolean updateLineMsg(LineMsg lineMsg);


    /**
     * 是否确认开标结果 （有无异议）
     * @param bidSectionId 标段
     * @param bidderId 投标人id
     * @return
     */
    String sureBidResult(Integer bidSectionId, Integer bidderId);

    /**
     * 获取有（无）异议的记录（质疑信息）
     *
     * @param lineMsg 查询条件
     * @return 结果集
     */
    List<LineMsg> listDissentLineMsg(LineMsg lineMsg);

}
