package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.entity.QuoteScoreResult;
import com.ejiaoyi.common.entity.QuoteScoreResultAppendix;
import com.ejiaoyi.common.mapper.QuoteScoreResultAppendixMapper;
import com.ejiaoyi.common.service.IQuoteScoreResultAppendixService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 投标人报价得分结果附录 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-12-05
 */
@Service
public class QuoteScoreResultAppendixServiceImpl extends ServiceImpl<QuoteScoreResultAppendixMapper, QuoteScoreResultAppendix> implements IQuoteScoreResultAppendixService {
    @Autowired
    private QuoteScoreResultAppendixMapper quoteScoreResultAppendixMapper;

    @Override
    public List<QuoteScoreResultAppendix> listQuoteScoreResultAppendix(Integer bidSectionId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null!");
        QueryWrapper<QuoteScoreResultAppendix> wrapper = new QueryWrapper<>();
        wrapper.eq("BID_SECTION_ID", bidSectionId);
        return quoteScoreResultAppendixMapper.selectList(wrapper);
    }

    @Override
    public QuoteScoreResultAppendix getQuoteScoreResultAppendix(Integer bidderId) {
        Assert.notNull(bidderId, "param bidderId can not be null!");
        QueryWrapper<QuoteScoreResultAppendix> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BIDDER_ID", bidderId);
        return quoteScoreResultAppendixMapper.selectOne(queryWrapper);
    }

    @Override
    public Integer deleteByBsId(Integer bidSectionId) {
        QueryWrapper<QuoteScoreResultAppendix> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", bidSectionId);
        return quoteScoreResultAppendixMapper.delete(queryWrapper);
    }
}
