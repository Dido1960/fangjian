package com.ejiaoyi.common.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 不见面开标大厅首页 前端控制器
 * </p>
 *
 * @author yyb
 * @since 2021-01-25
 */
@Controller
public class BidOpeningHallController {

    @Autowired
    private Environment env;

    /**
     * 房建项目引导页
     **/
    @RequestMapping("/pages")
    public ModelAndView bidConPages() {
        ModelAndView mav = new ModelAndView("/bidOpeningHall/bidConPages");
        mav.addObject("bidConAgency",env.getProperty("no-face.bid-con.agency"));
        mav.addObject("bidConBidder",env.getProperty("no-face.bid-con.bidder"));
        mav.addObject("bidConExpert",env.getProperty("no-face.bid-con.expert"));
        mav.addObject("bidConSupervise",env.getProperty("no-face.bid-con.supervise"));
        mav.addObject("bidConWorker",env.getProperty("no-face.bid-con.worker"));
        mav.addObject("admin",env.getProperty("no-face.bid-con.admin"));
        mav.addObject("active",env.getProperty("spring.profiles.active"));
        return mav;
    }

    /**
     * 不见面开标大厅首页
     **/
    @RequestMapping("/home.html")
    public ModelAndView hallIndexPage() {
        return new ModelAndView("/bidOpeningHall/index");
    }

    /**
     * 房建角色页面
     **/
    @RequestMapping("/bidConRolePage")
    public ModelAndView bidConRolePage() {
        ModelAndView mav = new ModelAndView("/bidOpeningHall/bidConRole");
        // 房建代理跳转地址
        mav.addObject("bidConAgency",env.getProperty("no-face.bid-con.agency"));
        // 房建投标人跳转地址
        mav.addObject("bidConBidder",env.getProperty("no-face.bid-con.bidder"));
        return mav;
    }

    /**
     * 政府采购角色选择页面
     **/
    @RequestMapping("/govRolePage")
    public ModelAndView govRolePage() {
        ModelAndView mav = new ModelAndView("/bidOpeningHall/govRole");
        // 政府采购代理跳转地址
        mav.addObject("govAgency",env.getProperty("no-face.gov.agency"));
        // 政府采购供应商跳转地址
        mav.addObject("govSupplier",env.getProperty("no-face.gov.supplier"));
        // 政府采购文件制作跳转地址
        mav.addObject("govFileMake",env.getProperty("no-face.gov.file-make"));
        return mav;
    }

    /**
     * 交通角色页面
     **/
    @RequestMapping("/trafficRolePage")
    public ModelAndView trafficRolePage() {
        ModelAndView mav = new ModelAndView("/bidOpeningHall/bidConRole");
        // 代理跳转地址
        mav.addObject("bidConAgency",env.getProperty("no-face.traffic.agency"));
        // 投标人跳转地址
        mav.addObject("bidConBidder",env.getProperty("no-face.traffic.bidder"));
        return mav;
    }

    /**
     * 水利角色页面
     **/
    @RequestMapping("/waterRolePage")
    public ModelAndView waterRolePage() {
        ModelAndView mav = new ModelAndView("/bidOpeningHall/bidConRole");
        // 代理跳转地址
        mav.addObject("bidConAgency",env.getProperty("no-face.water.agency"));
        // 投标人跳转地址
        mav.addObject("bidConBidder",env.getProperty("no-face.water.bidder"));
        return mav;
    }


    /**
     * 纸质标角色页面
     **/
    @RequestMapping("/paperRolePage")
    public ModelAndView paperRolePage() {
        ModelAndView mav = new ModelAndView("/bidOpeningHall/bidConRole");
        // 代理跳转地址
        mav.addObject("bidConAgency",env.getProperty("no-face.paper.agency"));
        // 投标人跳转地址
        mav.addObject("bidConBidder",env.getProperty("no-face.paper.bidder"));
        return mav;
    }

    /**
     * 无范本角色页面
     **/
    @RequestMapping("/noTemplateRolePage")
    public ModelAndView noTemplateRolePage() {
        ModelAndView mav = new ModelAndView("/bidOpeningHall/bidConRole");
        // 代理跳转地址
        mav.addObject("bidConAgency",env.getProperty("no-face.no-template.agency"));
        // 投标人跳转地址
        mav.addObject("bidConBidder",env.getProperty("no-face.no-template.bidder"));
        return mav;
    }
}
