package com.ejiaoyi.expert.controller;

import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.entity.BidSection;
import com.ejiaoyi.common.entity.FreeBackApply;
import com.ejiaoyi.common.entity.TenderDoc;
import com.ejiaoyi.common.enums.BidProtype;
import com.ejiaoyi.common.enums.Enabled;
import com.ejiaoyi.common.enums.EvalProcess;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.expert.service.IEvalBackService;
import com.ejiaoyi.expert.support.AuthUser;
import com.ejiaoyi.expert.support.CurrentUserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 评审回退控制器
 *
 * @author Make
 * @since 2020/12/8
 */
@RestController
@RequestMapping("/evalBack")
public class EvalBackController {
    /**
     * 文件预览服务器地址
     */
    @Value("${file.view.address}")
    private String fileViewAddress;
    @Autowired
    private IEvalBackService evalBackService;
    @Autowired
    private IBidSectionService bidSectionService;
    @Autowired
    private ITenderDocService tenderDocService;
    @Autowired
    private ITenderProjectService tenderProjectService;
    @Autowired
    private IFreeBackApplyService freeBackApplyService;

    /**
     * 校验当前标段是否允许回退
     * @return
     */
    @RequestMapping("/validBack")
    public JsonData validBack(){
        return evalBackService.validBack();
    }

    /**
     * 跳转回退审核页面
     * @return
     */
    @RequestMapping("/loadBackStepPage")
    public ModelAndView selectBackStepPage(){
        ModelAndView mav = new ModelAndView("/evalBack/loadBackStepPage");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");

        if (BidProtype.EPC.getCode().equals(bidSection.getBidClassifyCode())){
            mav.addObject("qualification", evalBackService.isGroupCompletion(gradeIds, EvalProcess.QUALIFICATION.getCode()));
            mav.addObject("calcPriceScore", evalBackService.isGroupCompletion(gradeIds, EvalProcess.CALC_PRICE_SCORE.getCode()));
        }

        if (BidProtype.CONSTRUCTION.getCode().equals(bidSection.getBidClassifyCode()) && Enabled.YES.getCode().equals(tenderDoc.getMutualSecurityStatus())){
            String[] otherGradeIdS = tenderDoc.getOtherGradeId().split(",");
            mav.addObject("other", evalBackService.isGroupCompletion(otherGradeIdS, EvalProcess.OTHER.getCode()));
        }

        mav.addObject("preliminary", evalBackService.isGroupCompletion(gradeIds, EvalProcess.PRELIMINARY.getCode()));
        mav.addObject("detailed", evalBackService.isGroupCompletion(gradeIds, EvalProcess.DETAILED.getCode()));
        mav.addObject("bidSection", bidSection);
        mav.addObject("tenderDoc", tenderDoc);

        return mav;
    }

    /**
     * 新增文件审核申请
     * @param freeBackApply 新增内瓤
     * @return
     */
    @RequestMapping("/addFreeBackApply")
    public JsonData addFreeBackApply(FreeBackApply freeBackApply){
        return evalBackService.addFreeBackApply(freeBackApply);
    }

    /**
     * 获取回退审核消息
     * @return
     */
    @RequestMapping("/getBackPush")
    public JsonData getBackPush(){
        return evalBackService.getBackPush();
    }

    /**
     * 更新回退审核结构信息阅读情况
     * @return
     */
    @RequestMapping("/updateBackPush")
    public Integer updateBackPush(){
        return evalBackService.updateBackPush();
    }

    /**
     * 更新回退审核结构信息阅读情况
     * @return
     */
    @RequestMapping("/evalBackRecord")
    public ModelAndView evalBackRecord(){
        ModelAndView mav = new ModelAndView("/evalBack/evalBackRecordPage");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        List<FreeBackApply> freeBackApplies = freeBackApplyService.listAllFreeBackApply(bidSectionId);
        mav.addObject("freeBackApplies", freeBackApplies);
        mav.addObject("bidSection", bidSectionService.getBidSectionById(bidSectionId));
        mav.addObject("fileViewAddress", fileViewAddress);
        return mav;
    }

    /**
     * 预览评审回退申请表页面
     * @param freeBackApplyId 回退申请id
     * @return 打印承诺书
     */
    @RequestMapping("/previewBackFilePage")
    public ModelAndView previewBackFilePage(Integer freeBackApplyId){
        ModelAndView mav = new ModelAndView("/evalBack/previewBackFilePage");
        FreeBackApply freeBackApply = freeBackApplyService.getApplyById(freeBackApplyId);

        BidSection bidSection = bidSectionService.getBidSectionById(freeBackApply.getBidSectionId());
        LocalDateTime now = LocalDateTime.now();
        mav.addObject("now",now);
        mav.addObject("freeBackApply", freeBackApply);
        mav.addObject("bidSection", bidSection);
        mav.addObject("tenderProject",tenderProjectService.getTenderProjectById(bidSection.getTenderProjectId()));
        return mav;
    }

}
