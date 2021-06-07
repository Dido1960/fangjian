package com.ejiaoyi.admin.controller;

import cn.hutool.core.date.DateUtil;
import com.ejiaoyi.admin.service.IProjectInfoService;
import com.ejiaoyi.admin.service.IPushEvalResultService;
import com.ejiaoyi.admin.service.impl.OldProjectServiceImpl;
import com.ejiaoyi.admin.support.AuthUser;
import com.ejiaoyi.admin.support.CurrentUserHolder;
import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.constant.BidFileConstant;
import com.ejiaoyi.common.constant.CacheName;
import com.ejiaoyi.common.constant.EvalStatus;
import com.ejiaoyi.common.constant.ProjectFileTypeConstant;
import com.ejiaoyi.common.dto.DownBidderFileDTO;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.dto.ProjectInfoTemp;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.BidProtype;
import com.ejiaoyi.common.enums.DMLType;
import com.ejiaoyi.common.enums.Enabled;
import com.ejiaoyi.common.enums.TimeFormatter;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.service.impl.BidSectionServiceImpl;
import com.ejiaoyi.common.service.impl.OperationMaintenanceTrackRecordServiceImpl;
import com.ejiaoyi.common.service.impl.TenderDocServiceImpl;
import com.ejiaoyi.common.util.DateTimeUtil;
import com.ejiaoyi.common.util.RedisUtil;
import com.ejiaoyi.common.util.ThreadUtlis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 项目基本信息 控制层类
 * </p>
 *
 * @author Mike
 * @since 2021-03-03
 */
@RestController
@RequestMapping("/projectInfo")
public class ProjectInfoController {
    @Autowired
    private BidSectionServiceImpl bidSectionService;
    @Autowired
    private OperationMaintenanceTrackRecordServiceImpl operationMaintenanceTrackRecordService;
    @Autowired
    private OldProjectServiceImpl oldProjectService;
    @Autowired
    private TenderDocServiceImpl tenderDocService;
    @Autowired
    private IRegService regService;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private IBidderService bidderService;
    @Autowired
    private IBidSectionRelateService bidSectionRelateService;
    @Autowired
    private IFDFSService fdfsService;
    @Autowired
    private IClarifyAnswerService clarifyAnswerService;
    @Autowired
    private IQueryFileService queryFileService;
    @Autowired
    private IPushEvalResultService pushEvalResultService;

    /**
     * 设置用户操作的bidsetionId
     *
     * @param bidSectionId
     * @return
     */
    @PostMapping("/setBidSectionId")
    public void setBidSectionId(Integer bidSectionId) {
        CurrentUserHolder.getUser().setBidSectionId(bidSectionId);
    }

    /**
     * 项目信息框架页
     * @return
     */
    @RequestMapping("/frameProjectInfoPage")
    public ModelAndView frameProjectInfoPage() {
        return new ModelAndView("/projectInfo/ordinary/frameProjectInfoPage");
    }

    /**
     * 项目信息区划树页
     * @return
     */
    @RequestMapping("/treeProjectInfoPage")
    public ModelAndView treeProjectInfoPage(){
        ModelAndView mav = new ModelAndView("/projectInfo/ordinary/treeProjectInfoPage");
        AuthUser user = CurrentUserHolder.getUser();
        mav.addObject("regId", user.getRegId());
        return mav;
    }

    /**
     * 项目信息表页
     * @param reg
     * @return
     */
    @RequestMapping("/projectInfoTablePage")
    public ModelAndView projectInfoTablePage(Reg reg){
        ModelAndView mav = new ModelAndView("/projectInfo/ordinary/projectInfoTable");
        mav.addObject("reg",reg);
        return mav;
    }


    /**
     * 项目信息框架页
     * @return
     */
    @RequestMapping("/frameSuperProjectInfoPage")
    public ModelAndView frameSuperProjectInfoPage() {
        return new ModelAndView("/projectInfo/super/frameProjectInfoPage");
    }

    /**
     * 项目信息区划树页
     * @return
     */
    @RequestMapping("/treeSuperProjectInfoPage")
    public ModelAndView treeSuperProjectInfoPage(){
        ModelAndView mav = new ModelAndView("/projectInfo/super/treeProjectInfoPage");
        AuthUser user = CurrentUserHolder.getUser();
        mav.addObject("regId", user.getRegId());
        return mav;
    }

    /**
     * 项目信息表页
     * @param reg
     * @return
     */
    @RequestMapping("/superProjectInfoTablePage")
    public ModelAndView superProjectInfoTablePage(Reg reg){
        ModelAndView mav = new ModelAndView("/projectInfo/super/bidderOrFileInfoTable");
        mav.addObject("reg",reg);
        return mav;
    }

    /**
     * 分页项目信息
     * @param bidSection 标段信息
     * @return
     */
    @RequestMapping("/pagedProjectInfo")
    public String pagedProjectInfo(BidSection bidSection) {
        return bidSectionService.pagedBidSectionInfo(bidSection);
    }

    /**
     * 添加页
     * @param operationMaintenanceTrackRecord 跟标记录
     * @return
     */
    @RequestMapping("/operationMaintenanceInfo")
    public ModelAndView operationMaintenanceInfo(OperationMaintenanceTrackRecord operationMaintenanceTrackRecord){
        ModelAndView mav = new ModelAndView("/projectInfo/ordinary/operationMaintenanceInfo");
        OperationMaintenanceTrackRecord record = operationMaintenanceTrackRecordService.getOperationMaintenanceBySectionId(operationMaintenanceTrackRecord.getBidSectionId());
        if (record != null) {
            mav.addObject("record",record);
        } else {
            AuthUser user = CurrentUserHolder.getUser();
            operationMaintenanceTrackRecord.setOperationMaintenanceId(user.getUserId());
            operationMaintenanceTrackRecord.setOperationMaintenanceName(user.getName());
            mav.addObject("record",operationMaintenanceTrackRecord);
        }

        return mav;
    }

    /**
     * 修改或新增
     * @param operationMaintenanceTrackRecord 跟标记录
     * @return
     */
    @RequestMapping("/saveOrUpdateOperationMaintenanceInfo")
    @UserLog(value = "'修改或新增运维跟踪记录: operationMaintenanceTrackRecord='+#operationMaintenanceTrackRecord.toString()", dmlType = DMLType.UPDATE)
    public Boolean saveOrUpdateOperationMaintenanceInfo(OperationMaintenanceTrackRecord operationMaintenanceTrackRecord){
        if (operationMaintenanceTrackRecord.getId() != null) {
            return operationMaintenanceTrackRecordService.updateById(operationMaintenanceTrackRecord);
        } else {
            return operationMaintenanceTrackRecordService.save(operationMaintenanceTrackRecord);
        }
    }

    /**
     * 跳转新增老系统项目页面
     * @return
     */
    @RequestMapping("/addOldProjectPage")
    public ModelAndView addOldProjectPage(Integer regId){
        return new ModelAndView("/projectInfo/oldSystem/addOldProjectPage", "regId", regId);
    }

    /**
     * 新增老系统项目
     * @param fileArchive 上传压缩包文件
     * @param regId 区划id
     * @return
     */
    @RequestMapping("/addOldProject")
    @UserLog(value = "'新增老系统项目: bidFileId='+#bidFileId", dmlType = DMLType.INSERT)
    public JsonData addOldProject(String fileArchive, Integer regId){
        return oldProjectService.addOldProject(fileArchive, regId);
    }


    /**
     * 修改页
     * @param id
     * @return
     */
    @RequestMapping("/updateProjectInfoPage")
    public ModelAndView updateProjectInfoPage(Integer id){
        ModelAndView mav = new ModelAndView("/projectInfo/ordinary/updateProjectInfoPage");
        BidSection bidSection = bidSectionService.getBidSectionById(id);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(id);
        mav.addObject("regs", regService.listReg(null));
        mav.addObject("bidSection", bidSection);
        mav.addObject("tenderDoc", tenderDoc);
        return mav;
    }

    /**
     * 修改
     * @param projectInfoTemp 修改的项目信息
     * @return
     */
    @RequestMapping("/updateProjectInfo")
    @UserLog(value = "'修改的项目信息: projectInfoTemp='+#projectInfoTemp.toString()", dmlType = DMLType.UPDATE)
    public Boolean updateProjectInfo(ProjectInfoTemp projectInfoTemp){
        return  projectInfoService.updateProjectInfo(projectInfoTemp);
    }

    /**
     * 分页查询改标段投标人信息
     * @param id 标段id
     * @return
     */
    @RequestMapping("/pageBidderBySectionId")
    public String pageBidderBySectionId(Integer id) {
        return bidderService.pageBidderBySectionId(id);
    }

    /**
     * 文件下载页面
     *
     * @return
     */
    @RequestMapping("downFilesPage")
    public ModelAndView downFilesPage() {
        ModelAndView mav = new ModelAndView("/projectInfo/super/downloadFiles");
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

        RedisUtil.delete(CacheName.DOWN_BIDDER_FILE + "::" + bidSectionId);
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
     * 结束评标
     *
     * @return 结束评标
     */
    @RequestMapping("/endEvaluation")
    public JsonData endEvaluation() {
        JsonData jsonData = new JsonData();
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = BidSection.builder()
                .id(bidSectionId)
                .evalEndTime(DateUtil.formatLocalDateTime(LocalDateTime.now()))
                .evalStatus(EvalStatus.FINISH)
                .build();
        if (bidSectionService.updateBidSectionById(bidSection) == 1) {
            ThreadUtlis.run(() -> {
                try {
                    pushEvalResultService.pushEvalResultForJQ(bidSectionId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            jsonData.setCode("1");
            jsonData.setMsg("结束评标成功！");
            jsonData.setData(bidSection.getEvalEndTime());
        } else {
            jsonData.setCode("2");
            jsonData.setMsg("结束评标失败！");
        }
        return jsonData;
    }

    /**
     * 推送开标结果
     *
     * @param bidSectionId 标段id
     * @return 是否成功
     */
    @RequestMapping("/pushOpenResult")
    public boolean pushOpenResult(Integer bidSectionId) {
        boolean pushStatus = false;
        try {
            pushEvalResultService.pushOpenResultForJQ(bidSectionId);
            pushStatus = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pushStatus;
    }

    /**
     * 推送评标结果
     *
     * @param bidSectionId 标段id
     * @return 是否成功
     */
    @RequestMapping("/pushEvalResult")
    public boolean pushEvalResult(Integer bidSectionId) {
        boolean pushStatus = false;
        try {
            pushEvalResultService.pushEvalResultForJQ(bidSectionId);
            pushStatus = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pushStatus;
    }
}
