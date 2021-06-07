package com.ejiaoyi.common.service.impl;

import cn.hutool.cache.CacheUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.common.constant.CacheName;
import com.ejiaoyi.common.dto.statistical.BidTypeDetailDTO;
import com.ejiaoyi.common.dto.statistical.RegDetailDTO;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.mapper.BidSectionMapper;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标段信息 服务实现类
 * </p>
 *
 * @author fengjunhong
 * @since 2020-7-13
 */
@Service
public class BidSectionServiceImpl extends BaseServiceImpl implements IBidSectionService {

    @Autowired
    private BidSectionMapper bidSectionMapper;

    @Autowired
    private BidSectionRelateServiceImpl bidSectionRelateService;

    @Autowired
    private IBidderService bidderService;

    @Autowired
    private IRegService regService;


    @Override
    @Cacheable(value = CacheName.BID_SECTION_CACHE, key = "#id", unless = "#result==null")
    public BidSection getBidSectionById(Integer id) {
        Assert.notNull(id, "param id can not be null!");
        return bidSectionMapper.selectById(id);
    }

    @Override
    public List<BidSection> listBidSection(BidSection bidSection) {
        // 获取分页对象
        Page page = this.getPageForLayUI();
        return bidSectionMapper.listBidSection(page, bidSection, null);
    }

    @Override
    public List<BidSection> listBidSectionNoPage(BidSection bidSection) {
        List<BidSection> list = bidSectionMapper.listBidSectionByToDay(bidSection);
        //开标场地
        for (BidSection section : list) {
            Reg reg = regService.getRegById(section.getRegId());
            if (!CommonUtil.isEmpty(reg)) {
                section.setBidOpenPlace(reg.getRegName());
            }
        }
        return list;
    }

    @Override
    public List<BidSection> listThreeBidSection() {
        List<BidSection> list = bidSectionMapper.listThreeBidSection();
        //开标场地
        for (BidSection section : list) {
            Reg reg = regService.getRegById(section.getRegId());
            if (!CommonUtil.isEmpty(reg)) {
                section.setBidOpenPlace(reg.getRegName());
            }
        }
        return list;
    }

    @Override
    public Map<String, Object> mapBidSection(BidSection bidSection) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 当前标段类型，总条数
        int count = bidSectionMapper.listBidSection(bidSection, null).size();
        // 分页数据
        List<BidSection> list = this.listBidSection(bidSection);
        map.put("count", count);
        map.put("list", list);
        return map;
    }


    @Override
    @CacheEvict(value = CacheName.BID_SECTION_CACHE, allEntries = true)
    public int updateBidSectionById(BidSection bidSection) {
        Assert.notNull(bidSection, "param bidSection can not be null!");
        Assert.notNull(bidSection.getId(), "param id can not be null!");
        // 更新复会时间
        return bidSectionMapper.updateById(bidSection);
    }


    @Override
    public String listJoinBidSection(BidSection bidSection, String currentUserOrgCode) {
        // 获取分页对象
        Page page = this.getPageForLayUI();
        List<BidSection> bidSections = new ArrayList<>();

        if (bidSection.getBidOpenOnline() != 1) {
            //现场开标
            //查詢投标人所有参标标段
            List<Bidder> bidders = bidderService.getBidder(currentUserOrgCode, null);
            if (bidders.size() > 0) {
                List<Integer> bidSectionIds = new ArrayList<>();
                for (Bidder bidder : bidders) {
                    bidSectionIds.add(bidder.getBidSectionId());
                }
                bidSections = bidSectionMapper.listBidSection(page, bidSection, bidSectionIds);
            }
        } else {
            //网上开标
            bidSections = bidSectionMapper.listBidSection(page, bidSection, null);
            for (BidSection section : bidSections) {
                //获取当前bidder
                List<Bidder> bidders = bidderService.getBidder(currentUserOrgCode, section.getId());
                if (!CommonUtil.isEmpty(currentUserOrgCode) && bidders.size() > 0) {
                    section.setIsJoinBid("1");
                    section.setBidderId(bidders.get(0).getId());
                } else {
                    section.setIsJoinBid("0");
                }
            }
        }
        return this.initJsonForLayUI(bidSections, (int) page.getTotal());
    }

    @Override
    public Integer getProjectTotal(BidSection bidSection, String currentUserOrgCode) {
        //网上开标
        if (bidSection.getBidOpenOnline() == 1) {
            return bidSectionMapper.listBidSection(bidSection, null).size();
        } else {
            List<Bidder> bidders = bidderService.getBidder(currentUserOrgCode, null);
            if (bidders.size() == 0) {
                return bidders.size();
            }
            List<Integer> bidSectionIds = new ArrayList<>();
            for (Bidder bidder : bidders) {
                bidSectionIds.add(bidder.getBidSectionId());
            }
            return bidSectionMapper.listBidSection(bidSection, bidSectionIds).size();
        }
    }

    @Override
    public String getBidSection(BidSection bidSection) {
        Page page = this.getPageForLayUI();
        List<BidSection> list = bidSectionMapper.listBidSection(page, bidSection, null);
        return this.initJsonForLayUI(list, (int) page.getTotal());
    }

    @Override
    @CacheEvict(value = CacheName.BID_SECTION_CACHE, key = "#bidSectionId")
    public Boolean agreeEval(Integer bidSectionId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null!");
        BidSection bidSection = BidSection.builder()
                .id(bidSectionId)
                .evalReviewStatus("2")
                .build();

        return bidSectionMapper.updateById(bidSection) > 0;
    }

    @Override
    public Integer countBidSection(BidSection bidSection) {
        return bidSectionMapper.countBidSection(bidSection);
    }

    @Override
    public Integer save(BidSection bidSection) {
        bidSectionMapper.insert(bidSection);
        return bidSection.getId();
    }

    @Override
    @Cacheable(value = CacheName.BID_SECTION_CACHE, key = "#bidSectionCode+'_'+#regId", unless = "#result==null")
    public BidSection getBidSectionByCode(String bidSectionCode, String bidClassifyCode, Integer regId) {
        QueryWrapper<BidSection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(bidSectionCode), "BID_SECTION_CODE", bidSectionCode);
        queryWrapper.eq(StringUtils.isNotEmpty(bidClassifyCode), "BID_CLASSIFY_CODE", bidClassifyCode);
        queryWrapper.eq(regId != null, "REG_ID", regId);
        return bidSectionMapper.selectOne(queryWrapper);
    }

    @Override
    public List<BidSection> listBidSectionBySth(BidSection bidSection) {
        return bidSectionMapper.selectList(new QueryWrapper<>(bidSection));
    }

    @Override
    public String pagedBidSectionInfo(BidSection bidSection) {
        Page page = this.getPageForLayUI();
        List<BidSection> list = bidSectionMapper.pagedBidSectionInfo(page,bidSection);
        for (BidSection section : list) {
            BidSectionRelate bidSectionRelate = bidSectionRelateService.getBidSectionRelateByBSId(section.getId());
            if (bidSectionRelate != null) {
                section.setBidSectionRelate(bidSectionRelate);
            }
        }
        return this.initJsonForLayUI(list,(int)page.getTotal());
    }

    @Override
    public List<BidSection> listStatisticalBidSection(BidSection bidSection) {
        return bidSectionMapper.listStatisticalBidSection(bidSection);
    }

    @Override
    public List<RegDetailDTO> statisticalBidSectionByReg(BidSection bidSection) {
        return bidSectionMapper.statisticalBidSectionByReg(bidSection);
    }

    @Override
    public List<BidTypeDetailDTO> statisticalBidSectionByBidType(BidSection bidSection) {
        return bidSectionMapper.statisticalBidSectionByBidType(bidSection);
    }
}
