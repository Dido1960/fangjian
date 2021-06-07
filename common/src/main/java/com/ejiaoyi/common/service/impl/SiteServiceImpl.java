package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.common.entity.Site;
import com.ejiaoyi.common.mapper.RegMapper;
import com.ejiaoyi.common.mapper.SiteMapper;
import com.ejiaoyi.common.service.ISiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @Description:
 * @Auther: liuguoqiang
 * @Date: 2020/7/13 14:25
 */
@Service
public class SiteServiceImpl extends BaseServiceImpl implements ISiteService {
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private WordbookServiceImpl wordbookService;
    @Autowired
    private RegMapper regMapper;
    
    /**
     * 查询分页数据
     * @param site
     * @return
     */
    @Override
    public String pagedSite(Site site) {
        Page page = this.getPageForLayUI();
        List<Site> list = siteMapper.pagedSite(page, site);
        for (Site s : list) {
            s.setTypeName(wordbookService.getValue("siteType",s.getType().toString()));
        }
        return this.initJsonForLayUI(list, (int) page.getTotal());
    }

    /**
     * 添加
     * @param site
     * @return
     */
    @Override
    public Integer addSite(Site site) {
        return siteMapper.insert(site);
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @Override
    public Integer deleteSite(Integer[] ids) {
        return siteMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 通过id查询Site
     * @param id
     * @return
     */
    @Override
    public Site getSiteById(Integer id) {
        Assert.notNull(id, "param id can not be null!");
        Site site = siteMapper.selectById(id);
        site.setRegName(regMapper.selectById(site.getRegId()).getRegName());
        site.setTypeName(wordbookService.getValue("siteType",site.getType().toString()));
        return site;
    }

    /**
     * 修改
     * @param site
     * @return
     */
    @Override
    public Integer updateSite(Site site) {
        return siteMapper.updateById(site);
    }

    @Override
    public List<Site> getSiteListByRegId(Integer regId) {
        QueryWrapper<Site> wrapper = new QueryWrapper<>();
        wrapper.eq("REG_ID",regId);
        List<Site> list = siteMapper.selectList(wrapper);
        return list;
    }

    @Override
    public List<Site> getEvalSiteListByRegId(Integer regId) {
        QueryWrapper<Site> wrapper = new QueryWrapper<>();
        wrapper.eq("REG_ID",regId);
        wrapper.eq("TYPE",2);
        List<Site> list = siteMapper.selectList(wrapper);
        return list;
    }
}
