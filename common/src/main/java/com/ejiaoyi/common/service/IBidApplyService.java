package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.BidApply;

import java.util.List;

/**
 * 评标申请记录服务类接口
 *
 * @author yyb
 * @since 2020-08-26
 */
public interface IBidApplyService {

    /**
     * 通过标段id获取评标申请记录
     *
     * @param bidSectionId 标段id
     * @return 记录
     */
    BidApply getBidApplyByBidSectionId(Integer bidSectionId);

    /**
     * 插入一条记录
     *
     * @param bidSectionId 标段
     * @return 是否成功
     */
    Boolean insert(Integer bidSectionId);

    /**
     * 修改记录
     *
     * @param bidApply 评标申请记录
     * @return  是否成功
     */
    Boolean updateBidApplyById(BidApply bidApply);

    /**
     * 清除专家组长id
     * @param id id
     */
    void updateClearChairManId(Integer id);

    /**
     * 插入一条记录
     *
     * @param bidApply 标段
     * @return 插入数据的主键id
     */
    Integer saveBidApply(BidApply bidApply);

    /**
     * 通过标段id获取所有评标申请
     * @param bidSectionId 标段id
     * @return
     */
    List<BidApply> listBidApply(Integer bidSectionId);
}
