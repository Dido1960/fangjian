package com.ejiaoyi.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.entity.CandidateResults;
import com.ejiaoyi.common.entity.CandidateSuccess;
import com.ejiaoyi.common.mapper.CandidateSuccessMapper;
import com.ejiaoyi.common.service.ICandidateSuccessService;
import com.ejiaoyi.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 推荐候选人结果表 服务实现类
 * </p>
 *
 * @author yyb
 * @since 2020-11-20
 */
@Service
public class CandidateSuccessServiceImpl extends BaseServiceImpl<CandidateSuccess> implements ICandidateSuccessService {

    @Autowired
    private CandidateSuccessMapper candidateSuccessMapper;


    @Override
    public CandidateSuccess getCandidateSuccess(Integer bidSectionId, Integer ranking) {
        QueryWrapper<CandidateSuccess> wrapper = new QueryWrapper<>();
        wrapper.eq("BID_SECTION_ID", bidSectionId);
        wrapper.eq("RANKING", ranking);
        return candidateSuccessMapper.selectOne(wrapper);
    }

    @Override
    public CandidateSuccess getCandidate(Integer bidSectionId, Integer bidderId) {
        QueryWrapper<CandidateSuccess> wrapper = new QueryWrapper<>();
        wrapper.eq("BID_SECTION_ID", bidSectionId);
        wrapper.eq("BIDDER_ID", bidderId);
        return candidateSuccessMapper.selectOne(wrapper);
    }

    @Override
    public Boolean addCandidateSuccess(CandidateSuccess candidateSuccess) {
        CandidateSuccess cs = getCandidateSuccess(candidateSuccess.getBidSectionId(), candidateSuccess.getRanking());
        if (cs != null) {
            return candidateSuccessMapper.updateById(CandidateSuccess.builder()
                    .id(cs.getId())
                    .bidderId(candidateSuccess.getBidderId())
                    .reason(candidateSuccess.getReason())
                    .build()) > 0;
        }
        candidateSuccessMapper.insert(candidateSuccess);
        return true;
    }

    @Override
    public List<CandidateSuccess> listCandidateSuccess(Integer bidSectionId) {
        if (!CommonUtil.isEmpty(bidSectionId)) {
            QueryWrapper<CandidateSuccess> wrapper = new QueryWrapper<>();
            wrapper.eq("BID_SECTION_ID", bidSectionId);
            wrapper.orderByAsc("RANKING");
            return candidateSuccessMapper.selectList(wrapper);
        }
        return null;
    }

    @Override
    public Integer deleteByBidSectionId(Integer bidSectionId) {
        QueryWrapper<CandidateSuccess> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", bidSectionId);
        return candidateSuccessMapper.delete(queryWrapper);
    }


}
