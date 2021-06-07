package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.CandidateResults;
import com.ejiaoyi.common.entity.CandidateSuccess;

import java.util.List;

/**
 * <p>
 * 推荐候选人结果表 服务类
 * </p>
 *
 * @author yyb
 * @since 2020-11-20
 */
public interface ICandidateSuccessService {

    /**
     * 通过条件获取推选人
     * @param bidSectionId 标段id
     * @param ranking 排名
     * @return
     */
    CandidateSuccess getCandidateSuccess(Integer bidSectionId,Integer ranking);

    /**
     * 通过条件获取推选人
     * @param bidSectionId 标段id
     * @param bidderId 投标人id
     * @return
     */
    CandidateSuccess getCandidate(Integer bidSectionId,Integer bidderId);

    /**
     * 添加中标推选人
     * @param candidateSuccess 中标候选人信息
     * @return
     */
    Boolean addCandidateSuccess(CandidateSuccess candidateSuccess);

    /**
     * 获取当前标段中标候选人名单
     * @param bidSectionId 标段主键
     * @return
     */
    List<CandidateSuccess> listCandidateSuccess(Integer bidSectionId);

    /**
     * 通过标段id批量删除
     * @param bidSectionId 标段id
     * @return
     */
    Integer deleteByBidSectionId(Integer bidSectionId);


}
