package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.Site;

import java.util.List;

/**
 * @Description:
 * @Auther: liuguoqiang
 * @Date: 2020/7/13 14:25
 */
public interface ISiteService {
    /**
     * 查询分页数据
     * @param site
     * @return
     */
    String pagedSite(Site site);

    /**
     * 添加
     * @param site
     * @return
     */
    Integer addSite(Site site);

    /**
     * 删除
     * @param ids
     * @return
     */
    Integer deleteSite(Integer[] ids);

    /**
     * 通过id查询Site
     * @param id
     * @return
     */
    Site getSiteById(Integer id);

    /**
     * 修改
     * @param site
     * @return
     */
    Integer updateSite(Site site);

    /**
     * 通过地区id获取
     * @param regId
     * @return
     */
    List<Site> getSiteListByRegId(Integer regId);

    /**
     * 通过地区获取评标场地
     * @param regId
     * @return
     */
    List<Site> getEvalSiteListByRegId(Integer regId);
}
