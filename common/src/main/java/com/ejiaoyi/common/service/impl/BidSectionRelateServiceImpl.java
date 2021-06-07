package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ejiaoyi.common.entity.BidSectionRelate;
import com.ejiaoyi.common.mapper.BidSectionRelateMapper;
import com.ejiaoyi.common.service.IBidSectionRelateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Auther: liuguoqiang
 * @Date: 2020/7/15 09:52
 */
@Service
public class BidSectionRelateServiceImpl extends ServiceImpl<BidSectionRelateMapper, BidSectionRelate> implements IBidSectionRelateService {
    @Autowired
    private BidSectionRelateMapper bidSectionRelateMapper;

    @Override
    public BidSectionRelate getBidSectionRelateByBSId(Integer bSId) {
        Assert.notNull(bSId, "param bidSectionId can not be null!");
        QueryWrapper<BidSectionRelate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", bSId);
        queryWrapper.eq("DELETE_FLAG", 0);
        return bidSectionRelateMapper.selectOne(queryWrapper);
    }

    @Override
    public Integer addBidSectionRelate(BidSectionRelate bidSectionRelate) {
        return bidSectionRelateMapper.insert(bidSectionRelate);
    }

    @Override
    public Integer updateBidSectionRelate(BidSectionRelate bidSectionRelate) {
        return bidSectionRelateMapper.updateById(bidSectionRelate);
    }

    @Override
    public Boolean updateRelateBySectionId(BidSectionRelate bidSectionRelate) {
        Assert.notNull(bidSectionRelate.getBidSectionId(), "param bidSectionId can not be null!");
        QueryWrapper<BidSectionRelate> wrapper = new QueryWrapper<>();
        wrapper.eq("BID_SECTION_ID", bidSectionRelate.getBidSectionId());
        wrapper.eq("DELETE_FLAG", 0);
        return bidSectionRelateMapper.update(bidSectionRelate, wrapper) > 0;
    }

    @Override
    public void updateClearReportId(Integer id) {
        Assert.notNull(id, "param id can not be null!");
        bidSectionRelateMapper.updateClearReportId(id);
    }
}
