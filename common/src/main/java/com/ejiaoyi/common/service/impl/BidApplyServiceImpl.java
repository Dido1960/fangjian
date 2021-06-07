package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.constant.CacheName;
import com.ejiaoyi.common.entity.BidApply;
import com.ejiaoyi.common.mapper.BidApplyMapper;
import com.ejiaoyi.common.service.IBidApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * 标段信息 服务实现类
 *
 * @author yyb
 * @since 2020-8-25
 */
@Service
public class BidApplyServiceImpl extends BaseServiceImpl<BidApply> implements IBidApplyService {

    @Autowired
    private BidApplyMapper bidApplyMapper;

    @Override
    //@Cacheable(value = CacheName.BID_APPLY_CACHE, key = "#bidSectionId", unless = "#result==null")
    public BidApply getBidApplyByBidSectionId(Integer bidSectionId) {
        QueryWrapper<BidApply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", bidSectionId);
        return bidApplyMapper.selectOne(queryWrapper);
    }

    @Override
    public Boolean insert(Integer bidSectionId) {
        BidApply bidApply = BidApply.builder()
                .bidSectionId(bidSectionId)
                .voteCount(1)
                .build();
        return bidApplyMapper.insert(bidApply) > 0;
    }

    @Override
    /*@CacheEvict(value = CacheName.BID_APPLY_CACHE,allEntries = true)*/
    public Boolean updateBidApplyById(BidApply bidApply) {
        return bidApplyMapper.updateById(bidApply) > 0;
    }

    @Override
    /*@CacheEvict(value = CacheName.BID_APPLY_CACHE,allEntries = true)*/
    public void updateClearChairManId(Integer id) {
        Assert.notNull(id, "param id can not be null!");
        bidApplyMapper.updateClearChairManId(id);
    }

    @Override
    public Integer saveBidApply(BidApply bidApply) {
        bidApplyMapper.insert(bidApply);
        return bidApply.getId();
    }

    @Override
    public List<BidApply> listBidApply(Integer bidSectionId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null!");
        QueryWrapper<BidApply> wrapper = new QueryWrapper<>();
        wrapper.eq("BID_SECTION_ID", bidSectionId);
        return bidApplyMapper.selectList(wrapper);
    }
}
