package com.ejiaoyi.agency.controller;

import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.entity.BidSectionRelate;
import com.ejiaoyi.common.entity.Reg;
import com.ejiaoyi.common.entity.Site;
import com.ejiaoyi.common.enums.DMLType;
import com.ejiaoyi.common.service.IRegService;
import com.ejiaoyi.common.service.ISiteService;
import com.ejiaoyi.common.service.IBidSectionRelateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @Description:
 * @Auther: liuguoqiang
 * @Date: 2020/7/14 21:07
 */
@RestController
@RequestMapping("/bidSectionRelate")
public class BidSectionRelateController {
    @Autowired
    private IBidSectionRelateService bidSectionRelateService;
    @Autowired
    private ISiteService siteService;
    @Autowired
    private IRegService regService;

    /**
     * 选择场地页
     * @param id
     * @param regId
     * @return
     */
    @RequestMapping("/selectChangeSitePage")
    public ModelAndView selectChangeSitePage(Integer id,Integer regId){
        ModelAndView mav = new ModelAndView("/bidSectionRelate/selectSite");
        //判断是否已经选择过场地
        BidSectionRelate bidSectionRelate = bidSectionRelateService.getBidSectionRelateByBSId(id);
        //没选择过，设置标段信息主键ID
        if (bidSectionRelate==null){
            bidSectionRelate = new BidSectionRelate();
            bidSectionRelate.setBidSectionId(id);
        }
        //获取主场区域
        Reg homeReg = regService.getRegById(regId);
        //获取主场场地
        List<Site> homeSiteList = siteService.getEvalSiteListByRegId(regId);
        //获取客场区域列表
        List<Reg> awayRegList = regService.getRegList();

        mav.addObject("bsr",bidSectionRelate);
        mav.addObject("homeReg",homeReg);
        mav.addObject("homeSiteList",homeSiteList);
        mav.addObject("awayRegList",awayRegList);
        return mav;
    }

    /**
     * 获取通过regId客场评标场
     * @param regId
     * @return
     */
    @RequestMapping("/getAwaySiteByRegId")
    @UserLog(value = "'查询客场评标场：'+#regId", dmlType = DMLType.SELECT)
    public List<Site> getAwaySiteByRegId(Integer regId){
        return siteService.getEvalSiteListByRegId(regId);
    }

    /**
     * 添加
     * @param bidSectionRelate
     * @return
     */
    @RequestMapping("/addBidSectionRelate")
    @UserLog(value = "'添加场地：'+#bidSectionRelate", dmlType = DMLType.INSERT)
    public Boolean addBidSectionRelate(BidSectionRelate bidSectionRelate){
        return 1==bidSectionRelateService.addBidSectionRelate(bidSectionRelate);
    }

    /**
     * 修改
     * @param bidSectionRelate
     * @return
     */
    @RequestMapping("/updateBidSectionRelate")
    public Boolean updateBidSectionRelate(BidSectionRelate bidSectionRelate){
        return 1==bidSectionRelateService.updateBidSectionRelate(bidSectionRelate);
    }
}
