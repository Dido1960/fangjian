package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.EvalResultEpc;
import com.ejiaoyi.common.entity.EvalResultJl;

import java.util.List;

/**
 * epc排名结果 接口
 * @author liuguoqiang
 * @since 2020-11-21 17:28
 */
public interface IEvalResultEpcService {

    /**
     * 通过标段ID获取 投标人排名数据
     * @param bidSectionId 标段id
     * @return 投标人排名数据
     */
    List<EvalResultEpc> listRankingBidderByBsId(Integer bidSectionId);

    /**
     * 存储监理排名数据
     * @param bidSectionId 标段id
     * @return 投标人排名数据
     */
    List<EvalResultEpc> addResultByBsId(Integer bidSectionId);

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
    EvalResultEpc getEvalResultEpc(Integer bidSectionId,Integer bidderId);
}
