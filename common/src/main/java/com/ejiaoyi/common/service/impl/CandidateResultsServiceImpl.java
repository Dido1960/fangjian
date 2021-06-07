package com.ejiaoyi.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ejiaoyi.common.constant.CacheName;
import com.ejiaoyi.common.dto.BidderRankingDTO;
import com.ejiaoyi.common.entity.Bidder;
import com.ejiaoyi.common.entity.CandidateResults;
import com.ejiaoyi.common.entity.ExpertReview;
import com.ejiaoyi.common.enums.TimeFormatter;
import com.ejiaoyi.common.mapper.CandidateResultsMapper;
import com.ejiaoyi.common.service.IBidderService;
import com.ejiaoyi.common.service.ICandidateResultsService;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 推荐候选人结果表 服务实现类
 * </p>
 *
 * @author liuguoqiang
 * @since 2020-09-23
 */
@Service
public class CandidateResultsServiceImpl extends BaseServiceImpl<CandidateResults> implements ICandidateResultsService {
    @Autowired
    private CandidateResultsMapper candidateResultsMapper;
    @Autowired
    private IBidderService bidderService;

    @Override
    public Boolean isPersonalSelectionEnd(Integer bidSectionId, Integer expertId) {
        QueryWrapper<CandidateResults> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", bidSectionId);
        queryWrapper.eq("EXPERT_ID", expertId);
        queryWrapper.eq("IS_END", 1);
        List<CandidateResults> list = candidateResultsMapper.selectList(queryWrapper);
        return !CommonUtil.isEmpty(list);
    }

    @Override
    public CandidateResults getCandidateByRanking(Integer bidSectionId, Integer expertId, Integer ranking) {
        QueryWrapper<CandidateResults> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", bidSectionId);
        queryWrapper.eq("EXPERT_ID", expertId);
        queryWrapper.eq("RANKING", ranking);
        return candidateResultsMapper.selectOne(queryWrapper);
    }

    @Override
    public CandidateResults getCandidate(CandidateResults candidateResults) {
        QueryWrapper<CandidateResults> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", candidateResults.getBidSectionId());
        queryWrapper.eq("EXPERT_ID", candidateResults.getExpertId());
        queryWrapper.eq("RANKING", candidateResults.getRanking());
        queryWrapper.eq(null != candidateResults.getBidderId(), "BIDDER_ID", candidateResults.getBidderId());
        return candidateResultsMapper.selectOne(queryWrapper);
    }

    @Override
    public List<CandidateResults> listCandidate(CandidateResults candidateResults) {
        QueryWrapper<CandidateResults> wrapper = new QueryWrapper<>();
        wrapper.eq(null != candidateResults.getBidSectionId(), "BID_SECTION_ID", candidateResults.getBidSectionId());
        wrapper.eq(null != candidateResults.getExpertId(), "EXPERT_ID", candidateResults.getExpertId());
        wrapper.eq(null != candidateResults.getBidderId(), "BIDDER_ID", candidateResults.getBidderId());
        wrapper.eq(null != candidateResults.getRanking(), "RANKING", candidateResults.getRanking());
        wrapper.orderByAsc("RANKING");
        List<CandidateResults> candidates = candidateResultsMapper.selectList(wrapper);
        for (CandidateResults candidate : candidates) {
            if (candidate.getBidderId()!=null){
                candidate.setBidderName(bidderService.getBidderById(candidate.getBidderId()).getBidderName());
            }
        }
        return candidates;
    }

    @Override
    public List<CandidateResults> listCandidate(Integer expertId, Integer bidSectionId) {
        QueryWrapper<CandidateResults> wrapper = new QueryWrapper<>();
        wrapper.eq("EXPERT_ID", expertId);
        wrapper.eq(null != bidSectionId, "BID_SECTION_ID", bidSectionId);
        wrapper.orderByAsc("RANKING");
        List<CandidateResults> candidates = candidateResultsMapper.selectList(wrapper);
        for (CandidateResults candidate : candidates) {
            if (candidate.getBidderId()!=null){
                candidate.setBidderName(bidderService.getBidderById(candidate.getBidderId()).getBidderName());
            }
        }
        return candidates;
    }

    @Override
    public Boolean addCandidateResult(CandidateResults candidateResults, Integer bId, String why) {
        List<CandidateResults> candidates = this.listCandidate(candidateResults);
        candidateResults.setBidderId(bId);
        candidateResults.setReason(why);
        if (candidates.size() > 0) {
            candidateResults.setId(candidates.get(0).getId());
            return candidateResultsMapper.updateById(candidateResults) > 0;
        }
        return candidateResultsMapper.insert(candidateResults) > 0;
    }

    @Override
    public Boolean updateCandidateById(CandidateResults candidateResults) {
        return candidateResultsMapper.updateById(candidateResults) > 0;
    }

    @Override
    public void initCandidateResult(Integer bidSectionId, Integer expertId) {
        QueryWrapper<CandidateResults> wrapper = new QueryWrapper<>();
        wrapper.eq("BID_SECTION_ID", bidSectionId);
        wrapper.eq("EXPERT_ID", expertId);
        List<CandidateResults> candidateResults = candidateResultsMapper.selectList(wrapper);
        // 判断是否已经初始化过
        if (candidateResults.size() == 0) {
            List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);
            int initCount = bidders.size() < 3 ? 2 : 3;
            for (int i = 0; i < initCount; i++) {
                candidateResultsMapper.insert(CandidateResults.builder()
                        .insertTime(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS))
                        .bidSectionId(bidSectionId)
                        .expertId(expertId)
                        .ranking(i + 1)
                        .build());
            }
        }
    }

    @Override
    public Integer updateAllCandidateResults(Integer bidSectionId, int enabled) {
        UpdateWrapper<CandidateResults> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("BID_SECTION_ID", bidSectionId);
        updateWrapper.set("IS_END", enabled);
        return candidateResultsMapper.update(null, updateWrapper);
    }

    @Override
    public Integer deleteByBidSectionId(Integer bidSectionId) {
        QueryWrapper<CandidateResults> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", bidSectionId);
        return candidateResultsMapper.delete(queryWrapper);
    }

    @Override
    public List<BidderRankingDTO> listBidderRanking(Integer bidSectionId, Integer ranking) {
        return candidateResultsMapper.listBidderRanking(bidSectionId, ranking);
    }

}
