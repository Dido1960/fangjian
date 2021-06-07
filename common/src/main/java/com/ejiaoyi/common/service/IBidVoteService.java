package com.ejiaoyi.common.service;


import com.ejiaoyi.common.entity.BidVote;

import java.util.List;

/**
 * 投票信息服务类接口
 *
 * @author yyb
 * @since 2020-08-26
 */
public interface IBidVoteService {

    /**
     * 获取 得票情况（某标段/某轮次/某专家）
     *
     * @param bidVote 投票信息
     * @return 记录
     */
    BidVote getBidVote(BidVote bidVote);

    /**
     * 获取所有专家的 得票信息（某标段/某轮次）
     *
     * @param bidApplyId
     * @param voteRound
     * @return
     */
    List<BidVote> getBidVoteList(Integer bidApplyId,Integer voteRound);

    /**
     * 插入一条 得票情况
     *
     * @param bidVote 投票信息
     * @return 是否成功
     */
    Boolean insertBidVote(BidVote bidVote);

    /**
     * 更新 得票情况
     *
     * @param bidVote 投票信息
     * @return 是否成功
     */
    Boolean updateBidVoteById(BidVote bidVote);

    /**
     * 删除所有的专家组长推选的投标记录
     * @param bidApplyId 评标申请记录id
     * @return
     */
    Integer deleteByBidApplyId(Integer bidApplyId);
}
