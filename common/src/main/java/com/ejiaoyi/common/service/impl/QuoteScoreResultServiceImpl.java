package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.entity.QuoteScoreResult;
import com.ejiaoyi.common.mapper.QuoteScoreResultMapper;
import com.ejiaoyi.common.service.IQuoteScoreResultService;
import com.ejiaoyi.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 投标人报价得分结果 服务实现类
 * </p>
 *
 * @author liuguoqiang
 * @since 2020-12-01
 */
@Service
public class QuoteScoreResultServiceImpl extends BaseServiceImpl implements IQuoteScoreResultService {
    @Autowired
    private QuoteScoreResultMapper quoteScoreResultMapper;

    @Override
    public Integer addOrUpdateQuoteScore(QuoteScoreResult quoteScoreResult) {
        if (CommonUtil.isEmpty(quoteScoreResult.getId())) {
            QueryWrapper<QuoteScoreResult> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("BIDDER_ID", quoteScoreResult.getBidderId());
            QuoteScoreResult tempItem = quoteScoreResultMapper.selectOne(queryWrapper);
            if (null == tempItem) {
                quoteScoreResultMapper.insert(quoteScoreResult);
            } else {
                quoteScoreResult.setId(tempItem.getId());
                quoteScoreResultMapper.updateById(quoteScoreResult);
            }
        } else {
            quoteScoreResultMapper.updateById(quoteScoreResult);
        }
        return quoteScoreResult.getId();
    }

    @Override
    public List<QuoteScoreResult> listQuoteScoreResult(QuoteScoreResult quoteScoreResult) {
        QueryWrapper<QuoteScoreResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(null != quoteScoreResult.getId(), "ID", quoteScoreResult.getId());
        queryWrapper.eq(null != quoteScoreResult.getBidderId(), "BIDDER_ID", quoteScoreResult.getBidderId());
        queryWrapper.eq(null != quoteScoreResult.getBidSectionId(), "BID_SECTION_ID", quoteScoreResult.getBidSectionId());
        return quoteScoreResultMapper.selectList(queryWrapper);
    }

    @Override
    public List<QuoteScoreResult> listQuoteScoreResult(Integer bidSectionId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null!");
        QueryWrapper<QuoteScoreResult> wrapper = new QueryWrapper<>();
        wrapper.eq("BID_SECTION_ID", bidSectionId);
        return quoteScoreResultMapper.selectList(wrapper);
    }

    @Override
    public QuoteScoreResult getQuoteScoreResultByBidderId(Integer bidderId) {
        Assert.notNull(bidderId, "param bidderId can not be null!");
        QueryWrapper<QuoteScoreResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BIDDER_ID", bidderId);
        return quoteScoreResultMapper.selectOne(queryWrapper);
    }

    @Override
    public Integer deleteByBsId(Integer bidSectionId) {
        QueryWrapper<QuoteScoreResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", bidSectionId);
        return quoteScoreResultMapper.delete(queryWrapper);
    }
}
