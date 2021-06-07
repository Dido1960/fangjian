package com.ejiaoyi.expert.controller;

import com.ejiaoyi.common.constant.ProjectFileTypeConstant;
import com.ejiaoyi.common.constant.TenderFileConstant;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.Status;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.service.impl.BidderServiceImpl;
import com.ejiaoyi.expert.support.AuthUser;
import com.ejiaoyi.expert.support.CurrentUserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.List;

/**
 * 纸质标控制器
 *
 * @author Make
 * @since 2021/1/23
 */
@RestController
@RequestMapping("/paper")
public class PaperEvalController extends BaseController {

    @Autowired
    private IBidSectionService bidSectionService;

    @Autowired
    private ITenderDocService tenderDocService;

    @Autowired
    private IFDFSService fdfsService;

    @Autowired
    private BidderServiceImpl bidderService;

    /**
     * 跳转纸质标评标页面
     *
     * @return
     */
    @RequestMapping("/paperEvalPage")
    public ModelAndView paperEvalPage() {
        ModelAndView mav = new ModelAndView("/paper/paperEvalPage");
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        if (Status.NOT_START.getCode().equals(bidSection.getEvalStatus())){
            bidSectionService.updateBidSectionById(BidSection.builder()
                    .id(bidSectionId)
                    .evalStatus(Status.PROCESSING.getCode()).build());
            bidSection.setEvalStatus(Status.PROCESSING.getCode());
        }
        List<Bidder> bidders = bidderService.listPassBidOpenBidder(bidSectionId);

        mav.addObject("bidderOne", bidders.get(0));
        mav.addObject("bidSection", bidSection);
        mav.addObject("docFileId", tenderDocService.getTenderDocBySectionId(user.getBidSectionId()).getDocFileId());
        mav.addObject("bidders", bidders);
        mav.addObject("expert", user);
        return mav;
    }

    /**
     * 招标pdf展示
     * @param fileId 招标文件上传id
     * @return
     */
    @RequestMapping("showTenderPdf")
    public ModelAndView showTenderPdf(Integer fileId) {
        ModelAndView modelAndView = new ModelAndView("/paper/paperIframe");
        Fdfs fdfs = fdfsService.getFdfsByUpload(fileId);
        modelAndView.addObject("isTender",1);
        modelAndView.addObject("mark", fdfs.getMark());
        return modelAndView;
    }

    /**
     * 投标pdf展示
     * @param fileId 解密的投标文件id
     * @return
     */
    @RequestMapping("showBidderPdf")
    public ModelAndView showBidderPdf(Integer fileId) {
        ModelAndView modelAndView = new ModelAndView("/paper/paperIframe");
        String mark = File.separator + ProjectFileTypeConstant.BIDDER_FILE + File.separator + fileId + TenderFileConstant.PAPER_BID_FILE;
        modelAndView.addObject("isTender",0);
        modelAndView.addObject("mark", mark);
        return modelAndView;
    }

    /**
     * 结束评标
     */
    @RequestMapping("/endEval")
    public JsonData endEval(){
        JsonData result = new JsonData();

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);

        if (Status.END.getCode().equals(bidSection.getEvalStatus())){
            result.setCode("1");
            result.setMsg("评标已结束!");
        }else {
             if (bidSectionService.updateBidSectionById(BidSection.builder()
                     .id(bidSectionId)
                     .evalStatus(Status.END.getCode()).build()) == 1){
                 result.setCode("1");
                 result.setMsg("已结束评标!");
             }else {
                 result.setCode("2");
                 result.setMsg("结束评标失败!");
             }
        }
        return result;
    }
}
