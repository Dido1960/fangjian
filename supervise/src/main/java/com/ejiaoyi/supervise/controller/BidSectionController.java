package com.ejiaoyi.supervise.controller;


import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.constant.BidFileConstant;
import com.ejiaoyi.common.constant.ProjectFileTypeConstant;
import com.ejiaoyi.common.dto.DownBidderFileDTO;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.BidProtype;
import com.ejiaoyi.common.enums.DMLType;
import com.ejiaoyi.common.enums.Enabled;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.supervise.service.IGovService;
import com.ejiaoyi.supervise.support.AuthUser;
import com.ejiaoyi.supervise.support.CurrentUserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 监管项目标段信息 controller
 *
 * @author yyb
 * @since 2020-8-31
 */
@RestController
@RequestMapping("/gov/bidSection")
public class BidSectionController {
    @Autowired
    IBidSectionService bidSectionService;
    @Autowired
    IGovService govService;
    @Autowired
    private ITenderDocService tenderDocService;
    @Autowired
    private IFDFSService fdfsService;
    @Autowired
    private IClarifyAnswerService clarifyAnswerService;
    @Autowired
    private IQueryFileService queryFileService;
    @Autowired
    private IBidderService bidderService;
    @Autowired
    private IBidderFileInfoService bidderFileInfoService;

    @Autowired
    private IBidSectionRelateService bidSectionRelateService;

    /**
     * 从session中获取标段主键
     */
    public Integer getBidSectionIdBySession() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return (Integer) request.getSession().getAttribute("bidSectionId");
    }

    /**
     * 从session中获取投标人主键
     */
    public Integer getBidderIdBySession() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return (Integer) request.getSession().getAttribute("bidderId");
    }

    /**
     * 获取 标段分页信息
     *
     * @param bidSection
     * @return
     */
    @RequestMapping(value = "/listBidSection", produces = {"text/html;charset=utf-8"})
    public String listBidSection(BidSection bidSection) {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        if (user.getLeader() != null && user.getLeader() == 1) {
            bidSection.setLeader(user.getLeader());
            bidSection.setRegId(user.getRegId());
            bidSection.setManagerId(user.getUserId());
        } else {
            bidSection.setManagerId(user.getUserId());
        }
        return govService.listBidSection(bidSection);
    }

    /**
     * 获取 项目标段数目
     *
     * @return
     */
    @RequestMapping("/getProjectTotal")
    public Integer getProjectTotal(BidSection bidSection) {
        AuthUser user = CurrentUserHolder.getUser();
        bidSection.setRegId(user.getRegId());
        return govService.getProjectTotal(bidSection);
    }

    /**
     * 同意评标
     *
     * @param bidSectionId 标段id
     * @return
     */
    @RequestMapping(value = "/agreeEval")
    @UserLog(value = "'修改标段信息: bidSectionId='+#bidSectionId", dmlType = DMLType.UPDATE)
    public Boolean agreeEval(Integer bidSectionId) {
        return bidSectionService.agreeEval(bidSectionId);
    }


    /**
     * 文件下载页面
     *
     * @return
     */
    @RequestMapping("downFilesPage")
    public ModelAndView downFilesPage() {
        ModelAndView mav = new ModelAndView("/progressOfBid/projectInfo/downloadFiles");
        Integer bidSectionId = CurrentUserHolder.getUser().getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        BidSectionRelate bidSectionRelate = bidSectionRelateService.getBidSectionRelateByBSId(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        Fdfs tenderPdf = null;
        Fdfs bidEvaluationPdf = null;
        Fdfs openBidTablePdf = null;
        // 澄清文件(展示最新的)
        ClarifyAnswer clarifyAnswer = null;
        if (Enabled.YES.getCode().toString().equals(bidSectionRelate.getIsOldProject())){
            String tenderMarkPrefix = File.separator + ProjectFileTypeConstant.OLD_TENDER_DOC + File.separator + bidSectionId + File.separator;
            tenderPdf = fdfsService.getFdfsByMark(tenderMarkPrefix + BidFileConstant.OldFileName.TENDER_DOC);
            if (BidProtype.CONSTRUCTION.getCode().equals(bidSection.getBidClassifyCode())) {
                // 招标工程量清单pdf
                Fdfs quantityPdf = fdfsService.getFdfsByMark(tenderMarkPrefix + BidFileConstant.OldFileName.ENGINEER_QUANTITY_LIST_PDF);

                // 招标工程量清单xml
                Fdfs quantityXml = fdfsService.getFdfsByMark(tenderMarkPrefix + BidFileConstant.OldFileName.ENGINEER_QUANTITY_LIST_XML);
                mav.addObject("quantityPdf", quantityPdf);
                mav.addObject("quantityXml", quantityXml);
            }
        } else {
            if (Enabled.YES.getCode().toString().equals(bidSection.getPaperEval())) {
                tenderPdf = fdfsService.getFdfsByUpload(tenderDoc.getDocFileId());
            } else {
                // 招标文件Gef
                Fdfs tenderGef = fdfsService.getFdfsByUpload(tenderDoc.getDocFileId());
                mav.addObject("tenderGef", tenderGef);
                // 招标文件pdf
                String tenderPdfMark = File.separator + ProjectFileTypeConstant.TENDER_DOC + File.separator + tenderDoc.getDocFileId() + BidFileConstant.TENDER_DOC;
                tenderPdf = fdfsService.getFdfsByMark(tenderPdfMark);
            }

            if (BidProtype.CONSTRUCTION.getCode().equals(bidSection.getBidClassifyCode())) {
                // 招标工程量清单pdf
                String quantityPdfMark = File.separator + ProjectFileTypeConstant.TENDER_DOC + File.separator + tenderDoc.getDocFileId() + BidFileConstant.ENGINEER_QUANTITY_LIST_PDF;
                Fdfs quantityPdf = fdfsService.getFdfsByMark(quantityPdfMark);

                // 招标工程量清单xml
                String quantityXmlMark = File.separator + ProjectFileTypeConstant.TENDER_DOC + File.separator + tenderDoc.getDocFileId() + BidFileConstant.ENGINEER_QUANTITY_LIST_XML;
                Fdfs quantityXml = fdfsService.getFdfsByMark(quantityXmlMark);
                mav.addObject("quantityPdf", quantityPdf);
                mav.addObject("quantityXml", quantityXml);
            }

            List<ClarifyAnswer> clarifyAnswers = clarifyAnswerService.listClarifyAnswerBySectionId(bidSectionId);
            if (clarifyAnswers.size() >= 1) {
                clarifyAnswer = clarifyAnswers.get(0);
            }
            // 开标记录表
            String openBidTableMark = File.separator + ProjectFileTypeConstant.BID_OPEN_RECORD + File.separator + bidSectionId +
                    File.separator + ProjectFileTypeConstant.BID_OPEN_RECORD + ".pdf";
            openBidTablePdf = fdfsService.getFdfsByMark(openBidTableMark);
            // 评标报告
            String bidEvaluationMark = File.separator + ProjectFileTypeConstant.BID_OPEN_RECORD + File.separator + bidSectionId +
                    File.separator + ProjectFileTypeConstant.EVAL_REPORT + ".pdf";
            bidEvaluationPdf = fdfsService.getFdfsByMark(bidEvaluationMark);
        }

        // 下载的投标人文件
        List<DownBidderFileDTO> downBidderFileDTOs = queryFileService.listDownBidderFileDTO(bidSectionId);

        mav.addObject("tenderPdf", tenderPdf);
        mav.addObject("bidSection", bidSection);
        mav.addObject("clarifyAnswer", clarifyAnswer);
        mav.addObject("openBidTablePdf", openBidTablePdf);
        mav.addObject("bidEvaluationPdf", bidEvaluationPdf);
        mav.addObject("downBidderFileDTOs", downBidderFileDTOs);
        return mav;
    }

    /**
     * 文件预览页面
     *
     * @param url 文件url
     * @return
     */
    @RequestMapping("previewPDFPage")
    public ModelAndView previewPDFPage(String url) {
        ModelAndView mav = new ModelAndView("/viewPdf/showPDFViewByUrl");
        mav.addObject("pdfUrl", url);
        return mav;
    }

    /**
     * 文件预览页面
     *
     * @param id fdfs文件ID
     * @return
     */
    @RequestMapping("previewPDFPageById")
    public ModelAndView previewPDFPageById(Integer id) {
        ModelAndView mav = new ModelAndView("/viewPdf/showPDFViewByUrl");
        mav.addObject("pdfUrl", fdfsService.getFdfdById(id).getUrl());
        return mav;
    }

    /**
     * 投标人标书下载
     *
     * @return
     */
    @RequestMapping("bidderTenderDownloadPage")
    public ModelAndView bidderTenderDownloadPage(Integer bidderId) {
        Integer bidSectionId = CurrentUserHolder.getUser().getBidSectionId();
        ModelAndView mav = new ModelAndView("/progressOfBid/projectInfo/downloadFiles");
        DownBidderFileDTO bidderFileDTO = queryFileService.getBidderPdfFileByBidderId(bidSectionId, bidderId);
        // 构造一个list,使用文件下载页面进行展示数据
        List<DownBidderFileDTO> list = new ArrayList<>();
        list.add(bidderFileDTO);
        mav.addObject("bidder", bidderService.getBidderById(bidderId));
        mav.addObject("onlyBidderPdf", true);
        mav.addObject("downBidderFileDTOs", list);
        return mav;
    }

    /**
     * 获取投标人文件信息
     *
     * @return
     */
    @RequestMapping("getAllFileStatus")
    public Boolean getAllFileStatus(Integer bidderId) {
        BidderFileInfo bidderFileInfo = bidderFileInfoService.getBidderFileInfoByBidderId(bidderId);
        if (bidderFileInfo != null && !CommonUtil.isEmpty(bidderFileInfo.getAllFileStatus())){
            return bidderFileInfo.getAllFileStatus() == 1;
        }
        return false;
    }

    /**
     * 改变 是否点击 开启下载 状态
     * @param status 下载状态
     * @return
     */
    @RequestMapping("/changeStartDownLoad")
    public Boolean changeStartDownLoad(Integer status){
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        return bidSectionService.updateBidSectionById(BidSection.builder()
                .id(bidSectionId)
                .startDownLoad(status)
                .build()) == 1;
    }
}
