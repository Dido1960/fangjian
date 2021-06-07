package com.ejiaoyi.admin.controller;

import com.ejiaoyi.admin.support.AuthUser;
import com.ejiaoyi.admin.support.CurrentUserHolder;
import com.ejiaoyi.common.service.ISiteService;
import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.entity.Site;
import com.ejiaoyi.common.enums.DMLType;
import com.ejiaoyi.common.service.IRegService;
import com.ejiaoyi.common.service.IWordbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description:  招标场地配置
 * @Auther: liuguoqiang
 * @Date: 2020/7/13 14:22
 */

@RestController
@RequestMapping("/site")
public class SiteController {
    @Autowired
    private ISiteService siteService;
    @Autowired
    private IRegService regService;
    @Autowired
    private IWordbookService wordbookService;


    /**
     * 场地框架页
     * @return
     */
    @RequestMapping("/frameSitePage")
    public ModelAndView frameSitePage() {
        return new ModelAndView("/site/frameSite");
    }

    /**
     * 区域树页
     * @return
     */
    @RequestMapping("/treeSitePage")
    public ModelAndView treeSitePage(){
        ModelAndView mav = new ModelAndView("/site/treeSite");
        AuthUser user = CurrentUserHolder.getUser();
        mav.addObject("regId", user.getRegId());
        return mav;
    }

    /**
     * 场地表页
     * @param site
     * @return
     */
    @RequestMapping("/siteTablePage")
    public ModelAndView siteTablePage(Site site){
        ModelAndView mav = new ModelAndView("/site/siteTable");
        mav.addObject("site",site);
        mav.addObject("typeList",wordbookService.listWordbookByTopKey("siteType"));
        return mav;
    }


    /**
     * 分页查询场地
     * @param site 部门对象
     * @return
     */
    @RequestMapping("/pagedSite")
    public String pagedSite(Site site) {
        return siteService.pagedSite(site);
    }

    /**
     * 添加页
     * @param site
     * @return
     */
    @RequestMapping("/addSitePage")
    public ModelAndView addSitePage(Site site){
        ModelAndView mav = new ModelAndView("/site/addSite");
        mav.addObject("site",site);
        return mav;
    }

    /**
     * 添加
     * @param site
     * @return
     */
    @RequestMapping("/addSite")
    @UserLog(value = "'添加场地信息: site='+#site.toString()", dmlType = DMLType.INSERT)
    public Boolean addSite(Site site){
        return 1==siteService.addSite(site);
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @RequestMapping("/deleteSite")
    @UserLog(value = "'删除场地信息: ids='+#ids", dmlType = DMLType.DELETE)
    public Boolean deleteSite(Integer[] ids){
        if (ids!=null){
            return ids.length == siteService.deleteSite(ids);
        }
        return false;
    }

    /**
     * 修改页
     * @param id
     * @return
     */
    @RequestMapping("/updateSitePage")
    public ModelAndView updateSitePage(Integer id){
        ModelAndView mav = new ModelAndView("/site/updateSite");
        mav.addObject("site",siteService.getSiteById(id));
        return mav;
    }

    /**
     * 修改
     * @param site
     * @return
     */
    @RequestMapping("/updateSite")
    @UserLog(value = "'修改场地信息：site='+#site.toString()",dmlType = DMLType.UPDATE)
    public Boolean updateSite(Site site){
        return 1==siteService.updateSite(site);
    }

    @RequestMapping("/showSitePage")
    public ModelAndView showSitePage(Integer id){
        ModelAndView mav = new ModelAndView("/site/showSite");
        mav.addObject("site",siteService.getSiteById(id));
        return mav;
    }
}
