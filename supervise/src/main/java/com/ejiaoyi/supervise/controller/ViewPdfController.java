package com.ejiaoyi.supervise.controller;


import com.ejiaoyi.common.constant.BidFileConstant;
import com.ejiaoyi.common.constant.ProjectFileTypeConstant;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.Enabled;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.supervise.support.AuthUser;
import com.ejiaoyi.supervise.support.CurrentUserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.List;


/**
 * Pdf加载控制器
 *
 * @author Make
 * @since 2020/9/29
 */
@RestController
@RequestMapping("gov/viewPdf")
public class ViewPdfController extends BaseController {
    /**
     * 文件预览服务器地址
     */
    @Value("${file.view.address}")
    private String fileViewAddress;
    @Autowired
    private IBidSectionService bidSectionService;
    @Autowired
    private IBidderService bidderService;
    @Autowired
    private ITenderDocService tenderDocService;
    @Autowired
    private IFDFSService fdfsService;
    @Autowired
    private IClarifyAnswerService clarifyAnswerService;
    @Autowired
    private IBidSectionRelateService bidSectionRelateService;

    /**
     * 招标文件
     */
    private final String TENDER_DOC = "1";
    /**
     * 投标文件
     */
    private final String BIDDER_FILE = "2";

    /**
     * 跳转评标查阅招投标文件页面
     *
     * @param bidSectionId 标段id
     * @param bidderId     投标人id
     * @return
     */
    @RequestMapping("/loadViewEvalPdf")
    public ModelAndView loadViewEvalPdf(Integer bidSectionId, Integer bidderId) {
        ModelAndView modelAndView = new ModelAndView("/viewPdf/viewEvalPdf");
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        Bidder bidder = bidderService.getBidderById(bidderId);
        modelAndView.addObject("bidSection", bidSection);
        modelAndView.addObject("bidder", bidder);
        modelAndView.addObject("tenderDoc", tenderDoc);
        return modelAndView;
    }

    /**
     * 通过fdfs的mark获取文件地址
     *
     * @param fileId   文件id
     * @param mark     文件标记
     * @param fileType 文件类型
     * @return
     */
    @RequestMapping("/getEvalFileUrl")
    public String getEvalFileUrl(Integer fileId, String mark, String fileType) {
        String path;
        if (TENDER_DOC.equals(fileType)) {
            path = File.separator + ProjectFileTypeConstant.TENDER_DOC + File.separator + fileId + mark;
        } else if (BIDDER_FILE.equals(fileType)) {
            path = File.separator + ProjectFileTypeConstant.BIDDER_FILE + File.separator + fileId + mark;
        } else {
            return null;
        }
        return fdfsService.getUrlByMark(path);
    }


    /**
     * 查看澄清文件
     * @return
     */
    @RequestMapping("/clarifyFilePage")
    public ModelAndView viewAllBidderPdf() {
        ModelAndView mav = new ModelAndView("/viewPdf/viewClarifyPdf");
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        List<ClarifyAnswer> clarifyAnswers = clarifyAnswerService.listClarifyAnswerBySectionId(bidSectionId);
        mav.addObject("clarifyAnswers", clarifyAnswers);
        return mav;
    }

    /**
     * 文件对比
     * @return
     */
    @RequestMapping("bidFileComparedPage")
    public ModelAndView bidFileComparedPage(){
        ModelAndView mav = new ModelAndView("/viewPdf/bidFileCompared");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        List<Bidder> list = bidderService.listPassBidOpenBidder(bidSectionId);

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        mav.addObject("list",list);
        mav.addObject("bidSection",bidSection);
        mav.addObject("bidFile", BidFileConstant.TENDER_DOC);
        mav.addObject("tenderDoc", tenderDoc);
        return mav;
    }

    /**
     * 招投标文件对比页面
     *
     * @return
     */
    @RequestMapping("/bidViewPage")
    public ModelAndView bidViewPage() {
        ModelAndView mav = new ModelAndView("/viewPdf/bidView");
        AuthUser user = CurrentUserHolder.getUser();
        BidSection bidSection = bidSectionService.getBidSectionById(user.getBidSectionId());
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(user.getBidSectionId());
        if (Enabled.YES.getCode().toString().equals(bidSection.getPaperEval())){
            mav.addObject("tenderMark",fdfsService.getFdfsByUpload(tenderDoc.getDocFileId()).getMark());
        }
        mav.addObject("bidSection",bidSection);
        mav.addObject("bidders", bidderService.listPassBidOpenBidder(user.getBidSectionId()));
        mav.addObject("tenderDoc",tenderDoc);
        return mav;
    }

    /**
     * 标书查看
     *
     * @return
     */
    @RequestMapping("/bidSeePage")
    public ModelAndView bidSeePage() {
        ModelAndView mav = new ModelAndView("/viewPdf/bidSee");
        AuthUser user = CurrentUserHolder.getUser();
        mav.addObject("bidSection",bidSectionService.getBidSectionById(user.getBidSectionId()));
        mav.addObject("bidders", bidderService.listPassBidOpenBidder(user.getBidSectionId()));
        mav.addObject("tenderDoc",tenderDocService.getTenderDocBySectionId(user.getBidSectionId()));

        return mav;
    }

    /**
     * 查看清单
     * @return
     */
    @RequestMapping("/checkListPage")
    public ModelAndView checkList() {
        ModelAndView mav = new ModelAndView("/viewPdf/viewListPage");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        List<Bidder> bidders = bidderService.listPassBidOpenBidder(bidSectionId);

        mav.addObject("bidders", bidders);
        mav.addObject("fileViewAddress", fileViewAddress);
        mav.addObject("tenderDoc", tenderDoc);
        return mav;
    }

    /**
     * 显示评标报告
     * @return
     */
    @RequestMapping("/showEvaluationReportPage")
    public ModelAndView showEvaluationReport() {
        ModelAndView mav = new ModelAndView("/viewPdf/evaluationReport");
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        String mark = "/evalReport/" + bidSectionId + "/evalReport.pdf";
        mav.addObject("mark", mark);
        mav.addObject("fdfs", fdfsService.getFdfsByMark(mark));
        mav.addObject("user", user);
        mav.addObject("bidSectionId", bidSectionId);
        return mav;
    }


}
