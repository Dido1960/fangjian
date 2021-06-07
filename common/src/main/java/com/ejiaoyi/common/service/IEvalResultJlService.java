package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.EvalResultJl;

import java.util.List;

/**
 * 监理排名结果 接口
 * @author liuguoqiang
 * @since 2020-11-21 17:28
 */
public interface IEvalResultJlService {

    /**
     * 通过标段ID获取 投标人排名数据
     * @param bidSectionId 标段id
     * @return 投标人排名数据
     */
    List<EvalResultJl> listRankingBidderByBsId(Integer bidSectionId);

    /**
     * 存储监理排名数据
     * @param bidSectionId 标段id
     * @return 投标人排名数据
     */
    List<EvalResultJl> addResultByBsId(Integer bidSectionId);

    /**
     * 删除当前标段的所有排名数据
     * @param bidSectionId 标段id
     * @return 删除条数
     */
    Integer deleteByBsId(Integer bidSectionId);

    /**
     * 投标人监理项目简要评标结果
     * @param bidSectionId
     * @param bidderId
     * @return
     */
    EvalResultJl getEvalResultJl(Integer bidSectionId,Integer bidderId);
}
