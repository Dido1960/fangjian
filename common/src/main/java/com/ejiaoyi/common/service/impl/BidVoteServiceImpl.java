package com.ejiaoyi.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.entity.BidVote;
import com.ejiaoyi.common.entity.ExpertReview;
import com.ejiaoyi.common.mapper.BidVoteMapper;
import com.ejiaoyi.common.service.IBidVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 标段信息 服务实现类
 *
 * @author yyb
 * @since 2020-8-25
 */
@Service
public class BidVoteServiceImpl extends BaseServiceImpl<BidVote> implements IBidVoteService {

    @Autowired
    private BidVoteMapper bidVoteMapper;


    @Override
    public BidVote getBidVote(BidVote bidVote) {
        QueryWrapper<BidVote> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_APPLY_ID", bidVote.getBidApplyId());
        queryWrapper.eq("VOTE_ROUND", bidVote.getVoteRound());
        queryWrapper.eq("BID_EXPERT_ID", bidVote.getBidExpertId());

        return bidVoteMapper.selectOne(queryWrapper);
    }

    @Override
    public List<BidVote> getBidVoteList(Integer bidApplyId, Integer voteRound) {
        QueryWrapper<BidVote> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_APPLY_ID", bidApplyId);
        queryWrapper.eq("VOTE_ROUND", voteRound);
        return bidVoteMapper.selectList(queryWrapper);
    }

    @Override
    public Boolean insertBidVote(BidVote bidVote) {
        return bidVoteMapper.insert(bidVote) > 0;
    }

    @Override
    public Boolean updateBidVoteById(BidVote bidVote) {
        return bidVoteMapper.updateById(bidVote) > 0;
    }

    @Override
    public Integer deleteByBidApplyId(Integer bidApplyId) {
        QueryWrapper<BidVote> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_APPLY_ID", bidApplyId);
        return bidVoteMapper.delete(queryWrapper);
    }
}
