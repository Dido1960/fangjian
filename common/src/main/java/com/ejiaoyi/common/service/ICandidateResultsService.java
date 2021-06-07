package com.ejiaoyi.common.service;

import com.ejiaoyi.common.dto.BidderRankingDTO;
import com.ejiaoyi.common.entity.CandidateResults;

import java.util.List;

/**
 * <p>
 * 推荐候选人结果表 服务类
 * </p>
 *
 * @author liuguoqiang
 * @since 2020-09-23
 */
public interface ICandidateResultsService {

    /**
     * 个人推选是否结束
     * @param bidSectionId 标段ID
     * @param expertId 专家ID
     * @return
     */
    Boolean isPersonalSelectionEnd(Integer bidSectionId, Integer expertId);

    /**
     * 通过名次获取推选人
     * @param bidSectionId 标段ID
     * @param expertId 专家ID
     * @param rank 名次
     * @return
     */
    CandidateResults getCandidateByRanking(Integer bidSectionId, Integer expertId, Integer rank);

    /**
     * 通过条件获取推选人
     * @param candidateResults 候选人信息
     * @return
     */
    CandidateResults getCandidate(CandidateResults candidateResults);

    /**
     * 通过条件获取推选人
     * @param candidateResults 候选人信息
     * @return
     */
    List<CandidateResults> listCandidate(CandidateResults candidateResults);

    /**
     * 获取某个专家推选的所有候选人
     * @param expertId 专家id
     * @param bidSectionId 标段id
     * @return
     */
    List<CandidateResults> listCandidate(Integer expertId, Integer bidSectionId);

    /**
     * 添加候选人
     * @param candidateResults 候选人信息
     * @param bId 投标人id
     * @param why 原因
     * @return
     */
    Boolean addCandidateResult(CandidateResults candidateResults,Integer bId,String why);

    /**
     * 修改推选人
     * @param candidateResults 候选人信息
     * @return
     */
    Boolean updateCandidateById(CandidateResults candidateResults);

    /**
     * 初始化专家推荐的候选人
     * @param bidSectionId 标段id
     * @param expertId 专家id
     */
    void initCandidateResult(Integer bidSectionId, Integer expertId);

    /**
     * 批量更新当前标段 当前grades的enabled数据
     * @param bidSectionId 标段id
     * @param enabled 是否小组结束
     * @return
     */
    Integer updateAllCandidateResults(Integer bidSectionId, int enabled);

    /**
     * 通过标段id批量删除
     * @param bidSectionId 标段id
     * @return
     */
    Integer deleteByBidSectionId(Integer bidSectionId);

    /**
     * 统计投标人相应名次的票数
     * @param bidSectionId 标段主键
     * @param ranking 名次
     * @return
     */
    List<BidderRankingDTO> listBidderRanking(Integer bidSectionId, Integer ranking);
}
