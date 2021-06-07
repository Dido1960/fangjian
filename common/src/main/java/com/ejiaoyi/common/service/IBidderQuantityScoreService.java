package com.ejiaoyi.common.service;

import com.ejiaoyi.common.dto.BidderQuantityScoreDTO;
import com.ejiaoyi.common.entity.BidderQuantityScore;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 投标人工程量清单报价得分结果 服务类
 * </p>
 *
 * @author Make
 * @since 2020-12-19
 */
public interface IBidderQuantityScoreService extends IService<BidderQuantityScore> {

    /**
     * 保存投标人工程量清单报价得分结果
     * @param bidSectionId 标段编号
     * @param bidderQuantityScoreDTOS 第三方服务返回的报价得分结果集
     */
    void saveBidderQuantityScoreList(Integer bidSectionId, List<BidderQuantityScoreDTO> bidderQuantityScoreDTOS);

    /**
     * 保存投标人报价得分 ->  清标 V3.0
     * @param bidSectionId 标段编号
     * @param bidderQuantityScoreDTOS 第三方服务返回的报价得分结果集
     */
    void saveBidderQuantityScore(Integer bidSectionId, List<BidderQuantityScore> bidderQuantityScoreDTOS);

    /**
     * 通过投标人id获取该投标人的报价得分信息
     * @param bidderId 投标人id
     * @return
     */
    BidderQuantityScore getBidderQuantityScoreByBidderId(Integer bidderId);

    /**
     * 通过标段id获取该标段所有参与报价得分的投标人报价得分信息
     * @param bidSectionId 标段id
     * @return
     */
    List<BidderQuantityScore> listBidderQuantityScoreByBidSectionId(Integer bidSectionId);


}
