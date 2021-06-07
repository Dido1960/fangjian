package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.Bidder;
import com.ejiaoyi.common.entity.EvalResultSg;

import java.util.List;

/**
 * 施工排名结果 接口
 * @author liuguoqiang
 * @since 2020-09-30
 */
public interface IEvalResultSgService {
    /**
     * 通过标段ID获取 投标人排名数据
     * @param bidSectionId 标段id
     * @return 投标人排名数据
     */
    List<EvalResultSg> listRankingBidderByBsId(Integer bidSectionId);

    /**
     * 存储施工排名数据
     * @param bidSectionId 标段id
     * @return 投标人排名数据
     */
    List<EvalResultSg> addResultByBsId(Integer bidSectionId);

    /**
     * 删除当前标段的所有排名数据
     * @param bidSectionId 标段id
     * @return 删除条数
     */
    Integer deleteByBsId(Integer bidSectionId);

    /**
     * 根据标段id和投标人id获取该投标人的评审结果
     * @param bidSectionId
     * @param bidderId
     * @return
     */
    EvalResultSg getEvalResultSgById(Integer bidSectionId, Integer bidderId);
}
