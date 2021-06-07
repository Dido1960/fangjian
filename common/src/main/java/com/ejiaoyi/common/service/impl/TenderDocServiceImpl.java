package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ejiaoyi.common.constant.CacheName;
import com.ejiaoyi.common.entity.TenderDoc;
import com.ejiaoyi.common.mapper.TenderDocMapper;
import com.ejiaoyi.common.service.ITenderDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 招标文件信息 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-07-13
 */
@Service
public class TenderDocServiceImpl extends ServiceImpl<TenderDocMapper, TenderDoc> implements ITenderDocService {

    @Autowired
    private TenderDocMapper tenderDocMapper;

    @Override
    @Cacheable(value= CacheName.TENDER_DOC_CACHE, key = "#id", unless = "#result==null")
    public TenderDoc getTenderDocById(Integer id) {
        Assert.notNull(id, "param id can not be null!");
        return tenderDocMapper.selectById(id);
    }

    @Override
    @Cacheable(value= CacheName.TENDER_DOC_CACHE, key = "'bidSectionId_'+#bidSectionId", unless = "#result==null")
    public TenderDoc getTenderDocBySectionId(Integer bidSectionId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null!");
        QueryWrapper<TenderDoc> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", bidSectionId);
        queryWrapper.eq("DELETE_FLAG", 0);
        queryWrapper.orderByDesc("VERSION");
        return tenderDocMapper.selectOne(queryWrapper);
    }

    @Override
    @CacheEvict(value= CacheName.TENDER_DOC_CACHE,allEntries = true)
    public boolean updateTenderDoc(TenderDoc tenderDoc) {
        QueryWrapper<TenderDoc> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", tenderDoc.getBidSectionId());
        return tenderDocMapper.update(tenderDoc, queryWrapper) == 1;
    }

    @Override
    @CacheEvict(value= CacheName.TENDER_DOC_CACHE,allEntries = true)
    public boolean updateTenderDocById(TenderDoc tenderDoc) {
        return tenderDocMapper.updateById(tenderDoc) == 1;
    }
}
