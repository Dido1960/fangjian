package com.ejiaoyi.expert.controller;

import cn.hutool.core.date.DateUtil;
import com.ejiaoyi.common.constant.CacheName;
import com.ejiaoyi.common.constant.EvalStatus;
import com.ejiaoyi.common.constant.ScoreType;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.dto.PushReportDTO;
import com.ejiaoyi.common.dto.WebSocketMessage;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.*;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.RedisUtil;
import com.ejiaoyi.common.util.RedissonUtil;
import com.ejiaoyi.common.util.ThreadUtlis;
import com.ejiaoyi.expert.dto.ExpertUserMsgDTO;
import com.ejiaoyi.expert.service.IBidEvalService;
import com.ejiaoyi.expert.service.IExpertService;
import com.ejiaoyi.expert.service.impl.PushEvalResultServiceImpl;
import com.ejiaoyi.expert.support.AuthUser;
import com.ejiaoyi.expert.support.CurrentUserHolder;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 评标公共 控制器
 *
 * @author Make
 * @since 2020/9/7
 */
@RestController
@RequestMapping("/expert/evalPlan")
public class EvalPlanController extends BaseController {

    @Autowired
    private IBidSectionService bidSectionService;

    @Autowired
    private ITenderDocService tenderDocService;

    @Autowired
    private IGradeService gradeService;

    @Autowired
    private IExpertService expertService;

    @Autowired
    private IBidderService bidderService;

    @Autowired
    private IBidEvalService bidEvalService;

    @Autowired
    private IExpertReviewService expertReviewService;

    @Autowired
    private IExpertReviewSingleItemService expertReviewSingleItemService;

    @Autowired
    private IExpertUserService expertUserService;

    @Autowired
    private IBidSectionRelateService bidSectionRelateService;

    @Autowired
    private PushEvalResultServiceImpl pushEvalResultService;

    @Autowired
    private IFDFSService fdfsService;

    @Autowired
    ISignatureService signatureService;

    /**
     * 跳转初步评审index页面
     *
     * @return
     */
    @RequestMapping("/preliminaryReview")
    public ModelAndView preliminaryReview() {
        ModelAndView mav = new ModelAndView();
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");

        // 初步评审完成情况
        boolean firstPreliminary = (bidSection.getBidOpenStatus() != null && Status.END.getCode().equals(bidSection.getBidOpenStatus()));
        if (!firstPreliminary) {
            mav.setViewName("redirect:/expert/startEval");
            this.setSystemActionMessage("当前环节还未开始！", WebSocketMessage.Dialog.MSG, WebSocketMessage.Type.ERROR);
            return mav;
        }
        if (BidProtype.EPC.getCode().equals(bidSection.getBidClassifyCode()) && !expertService.isGroupCompletion(gradeIds, EvalProcess.QUALIFICATION.getCode())){
            //判断为施工总承包并且，资格审查没有结束！
            mav.setViewName("redirect:/expert/startEval");
            this.setSystemActionMessage("当前环节还未开始！", WebSocketMessage.Dialog.MSG, WebSocketMessage.Type.ERROR);
            return mav;
        }

        // 获取初步评审的评标办法
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.PRELIMINARY.getCode());

        mav.setViewName("/evalPlan/preliminary/preliminaryReviewIndex");
        mav.addObject("grades", grades);
        mav.addObject("expert", user);
        mav.addObject("bidSection", bidSection);
        return mav;
    }

    /**
     * 跳转初步评审内容页面
     *
     * @param gradeId      评审id
     * @return
     */
    @RequestMapping("/loadGradeDetailed")
    public ModelAndView loadGradeDetailed(Integer gradeId) {
        ModelAndView mav = new ModelAndView("/evalPlan/preliminary/loadGradeDetailedPage");

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");

        // 获取初步评审的评标办法
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.PRELIMINARY.getCode());
        if (gradeId == null) {
            gradeId = grades.get(0).getId();
        }

        Grade grade = gradeService.getGrade(gradeId, EvalProcess.PRELIMINARY.getCode());
        List<Bidder> bidders;
        if (BidProtype.EPC.getCode().equals(bidSection.getBidClassifyCode())){
            //施工总承包 获取通过资格审查的投标人
            bidders = bidderService.listBidderPassQualifyReview(bidSectionId);
        }else {
            //获取通过开标的投标人
            bidders = bidderService.listPassBidOpenBidder(bidSectionId);
        }

        ExpertReview expertReview = expertReviewSingleItemService.initExpertSingleItem(bidSectionId, user.getUserId(), grade, bidders);

        //判断对应投标人当前评审类型是否评审完毕
        bidders = bidEvalService.listBidderGradeIsEnd(bidders, grade, ScoreType.QUALIFIED);

        mav.addObject("expertReview", expertReview);
        mav.addObject("currentGrade", grade);
        mav.addObject("tenderDoc", tenderDoc);
        mav.addObject("bidders", bidders);
        mav.addObject("bidSection", bidSection);
        mav.addObject("expert", user);
        return mav;
    }

    /**
     * 获取投标人数据
     *
     * @param gradeId  gradeID
     * @param bidderId 投标人ID
     * @return 获取投标人数据
     */
    @RequestMapping("/getBidderData")
    public Map<String, Object> getBidderData(Integer gradeId, Integer bidderId) {
        return bidEvalService.getPreBidderData(gradeId, bidderId);
    }

    /**
     * 保存专家评分项
     *
     * @param expertReviewSingleItem 已评分的项目
     * @return 保存专家评分项
     */
    @RequestMapping("/saveExpertReviewSingleItem")
    public JsonData saveExpertReviewSingleItem(ExpertReviewSingleItem expertReviewSingleItem) {
        JsonData data = new JsonData();
        try {
            Integer id = bidEvalService.saveExpertReviewSingleItem(expertReviewSingleItem);
            data.setCode(ExecuteCode.SUCCESS.getCode().toString());
            data.setData(id);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            data.setCode(ExecuteCode.FAIL.getCode().toString());
            return data;
        }
    }

    /**
     * 校验专家是否对某投标人评审完成
     *
     * @param bidderId 投标人id
     * @param gradeId  评分标准id
     * @return
     */
    @RequestMapping("/validBidderReviewComplete")
    public boolean validBidderReviewComplete(Integer bidderId, Integer gradeId) {
        return bidEvalService.validBidderReviewComplete(bidderId, gradeId);
    }

    /**
     * 一键合格
     *
     * @param gradeId 评分标准主键id
     * @return 一键合格
     */
    @RequestMapping("/passAllQualified")
    public void passAllQualified(Integer gradeId) {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        bidEvalService.passAllQualified(gradeId, user.getUserId());
    }

    /**
     * 个人评审结束校验（是否符合个人评审条件）
     *
     * @return 个人评审结束校验
     */
    @RequestMapping("/validQualifiedEnd")
    public JsonData validQualifiedEnd() {
        return bidEvalService.validQualifiedEnd();
    }

    /**
     * 校验是否有不合格项评审
     *
     * @return
     */
    @RequestMapping("/validUnqualified")
    public boolean validUnqualified() {
        List<ExpertReviewSingleItem> items = bidEvalService.listUnqualifiedItem();
        return CollectionUtils.isEmpty(items);
    }

    /**
     * 个人评审结束
     *
     * @return 个人评审结束
     */
    @RequestMapping("/personalReviewEnd")
    public boolean personalReviewEnd() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        return bidEvalService.updateReviewEnd(bidSectionId, EvalProcess.PRELIMINARY.getCode());
    }

    /**
     * 跳转到不合格项意见填写页面
     *
     * @return 跳转到不合格项意见填写页面
     */
    @RequestMapping("/jumpOpinionPage")
    public ModelAndView jumpOpinionPage() {
        ModelAndView mav = new ModelAndView("/evalPlan/preliminary/opinionPage");

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        List<Bidder> bidders = bidEvalService.listExpertOpinion(bidSectionId, EvalProcess.PRELIMINARY.getCode());
        mav.addObject("bidders", bidders);
        return mav;
    }

    /**
     * 保存不合格项的评审意见
     *
     * @param expertReviewSingleItems 不合格评审意见
     * @return 保存不合格项的评审意见
     */
    @RequestMapping("/updateReviewItems")
    public boolean updateReviewItems(@RequestBody List<ExpertReviewSingleItem> expertReviewSingleItems) {
        try {
            for (ExpertReviewSingleItem expertReviewSingleItem : expertReviewSingleItems) {
                expertReviewSingleItemService.update(expertReviewSingleItem);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 环节重评
     */
    @RequestMapping("/callPersonReview")
    public void callPersonReview() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        bidEvalService.callPersonReview(user.getBidSectionId(), EvalProcess.PRELIMINARY.getCode());
    }

    /**
     * 验证评标委员会评审结束的相关条件
     *
     * @return 验证评标委员会评审结束的相关条件
     */
    @RequestMapping("/validGroupReview")
    public JsonData validGroupReview() {
        return bidEvalService.checkPreGroupEnd();
    }

    /**
     * 评标委员会评审结束
     *
     * @return 评标委员会评审结束
     */
    @RequestMapping("/endGroupReview")
    public boolean endGroupReview() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        try {
            bidEvalService.updateGroupReviewEndStatus(bidSectionId, EvalProcess.PRELIMINARY.getCode());
            return true;
        } catch (Exception e) {
            e.getStackTrace();
            return false;
        }
    }

    /**
     * 专家评审汇总框架页面 （合格制）
     *
     * @return 专家评审汇总框架页面
     */
    @RequestMapping("/qualifiedEvalResultPage")
    public ModelAndView qualifiedEvalResultPage() {
        return new ModelAndView("/evalPlan/preliminary/result/evaluationResultPage", "expert", CurrentUserHolder.getUser());
    }

    /**
     * 展示专家个人评审结果框架 （合格制）
     * @return
     */
    @RequestMapping("/showPersonReviewResultPage")
    public ModelAndView showPersonReviewResultPage() {
        ModelAndView mav = new ModelAndView("/evalPlan/preliminary/result/showPersonReviewResultPage");

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");

        List<Bidder> bidders;
        if (BidProtype.EPC.getCode().equals(bidSection.getBidClassifyCode())){
            //施工总承包 获取通过资格审查的投标人
            bidders = bidderService.listBidderPassQualifyReview(bidSectionId);
        }else {
            //获取通过开标的投标人
            bidders = bidderService.listPassBidOpenBidder(bidSectionId);
        }
        // 获取初步评审的评标办法
        Grade grade = gradeService.listGrade(gradeIds, EvalProcess.PRELIMINARY.getCode()).get(0);
        //是否个人评审结束
        ExpertReview expertReview = expertReviewSingleItemService.initExpertSingleItem(bidSectionId, user.getUserId(), grade, bidders);

        mav.addObject("bidders", bidders);
        mav.addObject("expertReview", expertReview);
        mav.addObject("grade", grade);
        return mav;
    }

    /**
     * 获取合格制 专家个人评审结果数据
     * @param gradeId gradeId
     * @return 专家个人评审结果数据
     */
    @RequestMapping("/getResultData")
    public Map<String, Object> getResultData(Integer gradeId) {
        Map<String, Object> result = new HashMap<>();

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;

        ExpertReviewSingleItem query = ExpertReviewSingleItem.builder()
                .expertId(user.getUserId())
                .gradeId(gradeId)
                .build();
        List<ExpertReviewSingleItem> list = expertReviewSingleItemService.listExpertReviewSingleItem(query);
        result.put("data", list);

        return result;
    }


    /**
     * 获取当前专家的结果数据
     * @param bidderId 投标人ID
     * @param gradeId gradeId
     * @param isAllExpertEnd 是否所有专家都个人评审结束
     */
    @RequestMapping("/getBidderDataForResult")
    public Map<String, Object> getBidderDataForResult(Integer bidderId, Integer gradeId, Integer isAllExpertEnd){
        return bidEvalService.getPreBidderDataForResult(bidderId, gradeId, isAllExpertEnd);
    }

    /**
     * 跳转到修改专家个人意见页面 (合格制)
     *
     * @return
     */
    @RequestMapping("/updatePersonResult")
    public ModelAndView updatePersonResultPage(Integer singId) {
        ModelAndView mav = new ModelAndView("/evalPlan/preliminary/result/updatePersonResultPage");
        mav.addObject("single", expertReviewSingleItemService.getSingleById(singId));
        return mav;
    }

    /**
     * 小组评审汇总框架页面 （合格制）
     * @return 小组评审汇总框架页面
     */
    @RequestMapping("/showGroupReviewResultPage")
    public ModelAndView showGroupReviewResultPage() {
        ModelAndView mav = new ModelAndView("/evalPlan/preliminary/result/showGroupReviewResultPage");

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");

        List<Bidder> bidders;
        if (BidProtype.EPC.getCode().equals(bidSection.getBidClassifyCode())){
            //施工总承包 获取通过资格审查的投标人
            bidders = bidderService.listBidderPassQualifyReview(bidSectionId);
        }else {
            //获取通过开标的投标人
            bidders = bidderService.listPassBidOpenBidder(bidSectionId);
        }
        // 获取初步评审的评标办法
        Grade grade = gradeService.listGrade(gradeIds, EvalProcess.PRELIMINARY.getCode()).get(0);
        //评标专家
        List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);
        //获取已经完成个人评审的评标专家
        String endExpertIds = expertUserService.listExpertIdsForPersonEnd(bidSectionId, grade.getId());

        ExpertReview query = ExpertReview.builder()
                .gradeId(grade.getId())
                .bidSectionId(bidSectionId)
                .expertId(user.getUserId())
                .build();
        ExpertReview expertReview = expertReviewService.getExpertReview(query);

        //判断是否所有人都结束了个人评审
        int isAllExpertEnd = 1;
        if (!bidEvalService.validAllExpertEval(bidSectionId, grade.getId())){
            isAllExpertEnd = 0;
        }

        mav.addObject("bidders", bidders);
        mav.addObject("expertUsers", expertUsers);
        mav.addObject("expertReview", expertReview);
        mav.addObject("grade", grade);
        mav.addObject("isAllExpertEnd", isAllExpertEnd);
        mav.addObject("endExpertIds", endExpertIds);
        return mav;
    }

    /**
     * 获取初步评审投标人小组结果数据
     * @param gradeId gradeId
     * @return
     */
    @RequestMapping("/getBiddersGradeResult")
    public List<BidderReviewResult> getBiddersGradeResult(Integer gradeId){
        return bidEvalService.getPreBiddersGradeResult(gradeId);
    }

    /**
     * 跳转详细评审index页面
     *
     * @return
     */
    @RequestMapping("/detailedReview")
    public ModelAndView detailedReview() {
        ModelAndView mav = new ModelAndView();
        AuthUser user = CurrentUserHolder.getUser();
        BidSection bidSection = bidSectionService.getBidSectionById(user.getBidSectionId());
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(user.getBidSectionId());
        String[] gradeIds = tenderDoc.getGradeId().split(",");

        // 获取详细评审的评标办法
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());

        // 初步评审完成情况
        if (!expertService.isGroupCompletion(gradeIds, EvalProcess.PRELIMINARY.getCode())) {
            mav.setViewName("redirect:/expert/startEval");
            this.setSystemActionMessage("当前环节还未开始！", WebSocketMessage.Dialog.MSG, WebSocketMessage.Type.ERROR);
            return mav;
        }
        //详细评审框架首页判断
        BidProtype bidProtype = BidProtype.getBidProtypeByCode(bidSection.getBidClassifyCode());
        switch (bidProtype) {
            case CONSTRUCTION:
                // 施工详细评审页面
                mav.setViewName("/construction/detailed/detailedReviewIndex");
                break;
            case SUPERVISION:
                // 监理详细评审页面
                List<ReviewType> reviewTypes = ReviewType.listSupervisionType();
                mav.addObject("reviewTypes", reviewTypes);
                mav.setViewName("/supervisor/detailed/detailedReviewIndex");
                break;
            case QUALIFICATION:
                // 资格预审详细评审页面
                mav.setViewName("/qualification/detailed/detailedReviewIndex");
                break;
            case INVESTIGATION:
            case ELEVATOR:
            case DESIGN:
                // 进入勘察、设计、电梯详细评审页面
                mav.setViewName("/otherEvalPlan/detailed/detailedReviewIndex");
                break;
            case EPC:
                // 施工总承包
                mav.setViewName("/epc/detailed/detailedReviewIndex");
                break;
            default:
        }

        mav.addObject("grades", grades);
        mav.addObject("expert", user);
        mav.addObject("bidSection", bidSection);
        mav.addObject("evalProcess", EvalProcess.DETAILED.getCode());
        return mav;
    }

    /**
     * 评标结果基础页面
     *
     * @return
     */
    @RequestMapping("/endBasePage")
    public ModelAndView endBasePage() {
        ModelAndView mav = new ModelAndView();
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        mav.addObject("bidSection", bidSection);
        mav.addObject("expert", user);
        if (Enabled.YES.getCode().equals(bidSection.getCancelStatus())) {
            mav.setViewName("/evalPlan/cancelResult/cancelEndBase");
            return mav;
        }

        //评标结果框架首页判断
        BidProtype bidProtype = BidProtype.getBidProtypeByCode(bidSection.getBidClassifyCode());
        switch (bidProtype) {
            case CONSTRUCTION:
                // 施工评审结果页面
                mav.setViewName("/construction/result/endBase");
                break;
            case SUPERVISION:
                // 监理评审结果页面
                mav.setViewName("/supervisor/result/endBase");
                break;
            case QUALIFICATION:
                // 资格预审评审结果页面
                mav.setViewName("/qualification/result/endBase");
                break;
            case INVESTIGATION:
            case ELEVATOR:
            case DESIGN:
                // 进入勘察、设计、电梯评审结果页面
                mav.setViewName("/otherEvalPlan/result/endBase");
                break;
            case EPC:
                mav.setViewName("/epc/result/endBase");
            default:
                break;
        }
        return mav;
    }

    /**
     * 检查评标报告是否已经生成
     *
     * @return 检查评标报告是否已经生成
     */
    @RequestMapping("/checkReport")
    public JsonData checkReport() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        BidSection bidSection = bidSectionService.getBidSectionById(user.getBidSectionId());
        JsonData jsonData = new JsonData();
        if (!CommonUtil.isEmpty(bidSection.getEvalPdfGenerateStatus()) && bidSection.getEvalPdfGenerateStatus() == 1 ) {
            // 评标报告已经生成
            jsonData.setCode("1");
        } else {
            jsonData.setCode("2");
        }
        return jsonData;
    }



    /**
     * 评标报告页面
     *
     * @return 评标报告页面
     */
    @RequestMapping("/evaluationReportPage")
    public ModelAndView evaluationReportPage() {
        ModelAndView mav = new ModelAndView("/evalPlan/cancelResult/evaluationReport");
        AuthUser user = CurrentUserHolder.getUser();

        try {
            RedissonUtil.lock(CacheName.GENERATE_EVAL_REPORT_ + user.getBidSectionId(), 300, TimeUnit.SECONDS);
            Integer bidSectionId = user.getBidSectionId();
            BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
            // 设置当前专家的签名状态
            ExpertUser expertUser = expertUserService.getExpertUserById(user.getUserId());
            if (!CommonUtil.isEmpty(expertUser)) {
                user.setSignStatus(expertUser.getSignar());
            }
            List<ExpertUser> expertUserList = expertUserService.listExperts(bidSectionId);
            // 重新签名按钮
            boolean resignButton = true;
            for (ExpertUser signExperUser : expertUserList) {
                if (CommonUtil.isEmpty(signExperUser) || !EvalStatus.PROGRESSING.equals(signExperUser.getSignar())) {
                    resignButton = false;
                    break;
                }
            }
            //生成评标报告
            if (!EvalStatus.FINISH.equals(bidSection.getEvalPdfGenerateStatus())){
                bidEvalService.generateReport(user);
                bidSectionService.updateBidSectionById(BidSection.builder().id(bidSectionId).evalPdfGenerateStatus(1).build());
            }
            String mark = "/evalReport/" + bidSectionId + "/evalReport.pdf";
            mav.addObject("resignButton", resignButton);
            mav.addObject("mark", mark);
            mav.addObject("fdfs", fdfsService.getFdfsByMark(mark));
            mav.addObject("user", user);
            mav.addObject("bidSection", bidSection);
            mav.addObject("bidSectionId", bidSectionId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedissonUtil.unlock(CacheName.GENERATE_EVAL_REPORT_ + user.getBidSectionId());
        }
        return mav;
    }

    /**
     * 重新生成报告
     * @param delExertSigarInfo 是否删除签名信息
     *
     * @return
     */
    @RequestMapping("/reReport")
    public JsonData reReport(boolean delExertSigarInfo) {
        AuthUser user = CurrentUserHolder.getUser();
        JsonData jsonData = new JsonData();
        // 获取标段信息
        BidSection bidSection = bidSectionService.getBidSectionById(user.getBidSectionId());
        // 修改评标报告生成状态
        bidSectionService.updateBidSectionById(BidSection.builder()
                .id(bidSection.getId())
                .evalPdfGenerateStatus(0)
                .build());
        if (delExertSigarInfo){
            // 重新生成报告时，清除签名信息
            signatureService.updateExpertSigarStatus(user.getBidSectionId());
        }
        return jsonData;
    }

    /**
     * 评标结束页面
     *
     * @return 评标结束页面
     */
    @RequestMapping("/evaluationEndPage")
    public ModelAndView evaluationEndPage() {
        ModelAndView mav = new ModelAndView("/construction/result/evaluationEnd");
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        List<ExpertUser> expertUsers = expertUserService.listExperts(bidSectionId);
        //所有专家 签名状态 1签名完成
        Integer checkSignar = 1;
        for (ExpertUser expertUser : expertUsers) {
            if(expertUser.getSignar() != 1){
                checkSignar = 0;
                break;
            }
        }
        mav.addObject("checkSignar", checkSignar);
        mav.addObject("bidSection", bidSection);
        mav.addObject("expert", user);
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
     * 跳转报价得分计算index页面
     *
     * @return
     */
    @RequestMapping("/priceScoreReview")
    public ModelAndView priceScoreReview() {
        ModelAndView mav = new ModelAndView();
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");

        // 详细评审完成情况
        if (!expertService.isGroupCompletion(gradeIds, EvalProcess.DETAILED.getCode())) {
            mav.setViewName("redirect:/expert/startEval");
            this.setSystemActionMessage("当前环节还未开始！", WebSocketMessage.Dialog.MSG, WebSocketMessage.Type.ERROR);
            return mav;
        }
        if (BidProtype.CONSTRUCTION.getCode().equals(bidSection.getBidClassifyCode())) {
            mav.setViewName("redirect:/expert/conBidEval/priceScoreReviewIndex");
        } else if(BidProtype.EPC.getCode().equals(bidSection.getBidClassifyCode())){
            mav.setViewName("redirect:/expert/epcBidEval/priceScoreReviewIndex");
        }
        return mav;
    }

    /**
     * 跳转 其他评审 index页面
     * @return 其他评审 index页面
     */
    @RequestMapping("/otherReview")
    public ModelAndView otherReview() {
        ModelAndView mav = new ModelAndView();
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);

        // 详细评审完成情况
        if (!(!CommonUtil.isEmpty(bidSection.getPriceRecordStatus())&& ExecuteCode.SUCCESS.getCode().equals(bidSection.getPriceRecordStatus()))) {
            mav.setViewName("redirect:/expert/startEval");
            this.setSystemActionMessage("当前环节还未开始！", WebSocketMessage.Dialog.MSG, WebSocketMessage.Type.ERROR);
            return mav;
        }
        if (BidProtype.CONSTRUCTION.getCode().equals(bidSection.getBidClassifyCode()) && Enabled.YES.getCode().equals(tenderDoc.getMutualSecurityStatus())) {
            mav.setViewName("redirect:/expert/conBidEval/otherMutualReviewIndex");
        } else{
            mav.setViewName("redirect:/expert/startEval");
        }
        return mav;
    }

    /**
     * 修改标段
     * @param bidSection 修改标段
     * @return 修改状态
     */
    @RequestMapping("/updateBidSection")
    public boolean updateBidSection(BidSection bidSection) {
        return bidSectionService.updateBidSectionById(bidSection) > 0;
    }

    /**
     * 判断当前标段是否有关联标段
     */
    @RequestMapping("/isHaveRelateBid")
    public Boolean isHaveRelateBid(){
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        BidSectionRelate bidSectionRelate = bidSectionRelateService.getBidSectionRelateByBSId(user.getBidSectionId());
        return !CommonUtil.isEmpty(bidSectionRelate) && !CommonUtil.isEmpty(bidSectionRelate.getPreRelatedId());
    }


    /**
     * 项目关联 页面
     */
    @RequestMapping("/bidRelatePage")
    public ModelAndView bidRelatePage(){
        ModelAndView mav = new ModelAndView("/include/bidRelate");

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        BidSectionRelate bidSectionRelate = bidSectionRelateService.getBidSectionRelateByBSId(bidSectionId);
        Integer bidSectionRelateId = bidSectionRelate.getPreRelatedId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionRelateId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionRelateId);
        String bidType = BidProtype.getBidProtypeByCode(bidSection.getBidClassifyCode()).getChineseName();

        List<Bidder> bidders = bidderService.listPassBidOpenBidder(bidSectionId);
        List<Bidder> relateBidders = new ArrayList<>();
        for (Bidder bidder : bidders) {
            Bidder relateBidder = bidderService.getBidderForRelate(bidder.getBidderOrgCode(), bidSectionRelateId);
            if (!CommonUtil.isEmpty(relateBidder)){
                relateBidders.add(relateBidder);
            }
        }

        mav.addObject("bidSection", bidSection);
        mav.addObject("tenderDoc", tenderDoc);
        mav.addObject("bidType", bidType);
        mav.addObject("bidders", relateBidders);
        return mav;
    }

    /**
     * 获取组长消息
     * @return
     */
    @RequestMapping("/getExpertUserMsg")
    public ExpertUserMsgDTO getExpertUserMsg(){
        return bidEvalService.getExpertUserMsg();
    }

    /**
     * 删除已读消息
     */
    @RequestMapping("/deleteExpertUserMsg")
    public void deleteExpertUserMsg(){
        bidEvalService.deleteExpertUserMsg();
    }
}
