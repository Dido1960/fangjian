package com.ejiaoyi.expert.controller;

import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.service.IBidderQuantityScoreService;
import com.ejiaoyi.common.service.impl.*;
import com.ejiaoyi.expert.support.AuthUser;
import com.ejiaoyi.expert.support.CurrentUserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * 清标控制器
 *
 * @author Make
 * @since 2020/12/16
 */
@RestController
@RequestMapping("/clearBid")
public class ClearBidController extends BaseController {

    @Autowired
    private BidSectionServiceImpl bidSectionService;

    @Autowired
    private BidSectionRelateServiceImpl bidSectionRelateService;

    @Autowired
    private TenderDocServiceImpl tenderDocService;

    @Autowired
    private QuantityResultServiceImpl quantityResultService;

    @Autowired
    private BidderServiceImpl bidderService;

    @Autowired
    private IBidderQuantityScoreService bidderQuantityScoreService;

    /**
     * 跳转清标主页面
     *
     * @return
     */
    @RequestMapping("/clearBidIndex")
    public ModelAndView clearBidIndex() {
        ModelAndView modelAndView = new ModelAndView("/construction/quantity/clearBidIndex");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        BidSectionRelate bidSectionRelate = bidSectionRelateService.getBidSectionRelateByBSId(bidSectionId);

        modelAndView.addObject("bidSection", bidSection);
        modelAndView.addObject("tenderDoc", tenderDoc);
        modelAndView.addObject("expert", user);
        modelAndView.addObject("menu", false);
        modelAndView.addObject("bidSectionRelate", bidSectionRelate);
        return modelAndView;
    }

    /**
     * 跳转清标分析结果主页面
     *
     * @return
     */
    @RequestMapping("/clearBidResultIndex")
    public ModelAndView clearBidResultIndex() {
        ModelAndView modelAndView = new ModelAndView("/construction/quantity/clearBidResult/clearBidResultIndex");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        BidSectionRelate bidSectionRelate = bidSectionRelateService.getBidSectionRelateByBSId(bidSectionId);

        modelAndView.addObject("tenderDoc", tenderDoc);
        modelAndView.addObject("bidSection", bidSection);
        modelAndView.addObject("expert", user);
        modelAndView.addObject("menu", false);
        modelAndView.addObject("bidSectionRelate", bidSectionRelate);
        return modelAndView;
    }

    /**
     * 跳转经济标分析结果主页面
     *
     * @return
     */
    @RequestMapping("/economicAnalysisIndex")
    public ModelAndView economicAnalysisIndex() {
        ModelAndView modelAndView = new ModelAndView("/construction/quantity/economicAnalysis/economicAnalysisIndex");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        BidSectionRelate bidSectionRelate = bidSectionRelateService.getBidSectionRelateByBSId(bidSectionId);

        modelAndView.addObject("tenderDoc", tenderDoc);
        modelAndView.addObject("bidSection", bidSection);
        modelAndView.addObject("expert", user);
        modelAndView.addObject("menu", false);
        modelAndView.addObject("bidSectionRelate", bidSectionRelate);
        return modelAndView;
    }

    /**
     * 跳转控制价分析页面
     *
     * @return
     */
    @RequestMapping("/controlPriceAnalysisPage")
    public ModelAndView controlPriceAnalysisPage() {
        ModelAndView modelAndView = new ModelAndView("/construction/quantity/clearBidResult/controlPriceAnalysisPage");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        List<Bidder> bidders = bidderService.listDecrySuccessBidder(bidSectionId, false);

        modelAndView.addObject("bidSection", bidSection);
        modelAndView.addObject("tenderDoc", tenderDoc);
        modelAndView.addObject("bidders", bidders);
        modelAndView.addObject("expert", user);
        modelAndView.addObject("menu", false);
        return modelAndView;
    }

    /**
     * 跳转清标分析结果页面
     *
     * @return
     */
    @RequestMapping("/resultPage")
    public ModelAndView resultPage() {
        ModelAndView modelAndView = new ModelAndView("/construction/quantity/clearBidResult/resultPage");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        BidSectionRelate bidSectionRelate = bidSectionRelateService.getBidSectionRelateByBSId(bidSectionId);

        modelAndView.addObject("bidSectionRelate", bidSectionRelate);
        modelAndView.addObject("tenderDoc", tenderDoc);
        return modelAndView;
    }



}
