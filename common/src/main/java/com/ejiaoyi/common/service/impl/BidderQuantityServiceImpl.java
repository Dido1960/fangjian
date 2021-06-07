package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ejiaoyi.common.entity.BidderQuantity;
import com.ejiaoyi.common.mapper.BidderQuantityMapper;
import com.ejiaoyi.common.service.IBidderQuantityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 投标人工程量清单服务信息 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-12-15
 */
@Service
public class BidderQuantityServiceImpl extends ServiceImpl<BidderQuantityMapper, BidderQuantity> implements IBidderQuantityService {
    @Autowired
    private BidderQuantityMapper bidderQuantityMapper;

    @Override
    public BidderQuantity initBidderQuantity(BidderQuantity bidderQuantity) {
        Assert.notNull(bidderQuantity, "param bidXmlUid can not be null");
        Assert.notNull(bidderQuantity.getBidXmlUid(), "param bidXmlUid can not be null");
        QueryWrapper<BidderQuantity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(null != bidderQuantity.getId(), "ID", bidderQuantity.getId());
        queryWrapper.eq(null != bidderQuantity.getTenderXmlUid(), "TENDER_XML_UID", bidderQuantity.getTenderXmlUid());
        queryWrapper.eq(null != bidderQuantity.getBidXmlUid(), "BID_XML_UID", bidderQuantity.getBidXmlUid());
        BidderQuantity old = bidderQuantityMapper.selectOne(queryWrapper);
        if (old == null) {
            bidderQuantityMapper.insert(bidderQuantity);
            return bidderQuantity;
        } else {
            return old;
        }
    }

    @Override
    public BidderQuantity getBidderQuantityByBidXmlUid(String bidXmlUid) {
        Assert.notNull(bidXmlUid, "param bidXmlUid can not be null");
        QueryWrapper<BidderQuantity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_XML_UID", bidXmlUid);
        return bidderQuantityMapper.selectOne(queryWrapper);
    }

    @Override
    public List<BidderQuantity> listBidderQuantityNeedDoArithmeticAnalysis() {
        return bidderQuantityMapper.listBidderQuantityNeedDoArithmeticAnalysis();
    }

    @Override
    public List<BidderQuantity> listBidderQuantityNeedDoPriceAnalysis() {
        return bidderQuantityMapper.listBidderQuantityNeedDoPriceAnalysis();
    }

    @Override
    public List<BidderQuantity> listBidderQuantityNeedDoRuleAnalysis() {
        return bidderQuantityMapper.listBidderQuantityNeedDoRuleAnalysis();
    }

    @Override
    public List<BidderQuantity> listBidderQuantityNeedDoStructureAnalysis() {
        return bidderQuantityMapper.listBidderQuantityNeedDoStructureAnalysis();
    }
}
