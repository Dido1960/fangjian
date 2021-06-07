package com.ejiaoyi.expert.controller;

import com.ejiaoyi.common.constant.CacheName;
import com.ejiaoyi.common.constant.EvalStatus;
import com.ejiaoyi.common.constant.ScoreType;
import com.ejiaoyi.common.dto.BidderResultDTO;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.dto.WebSocketMessage;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.ConDetailedMethod;
import com.ejiaoyi.common.enums.Enabled;
import com.ejiaoyi.common.enums.EvalProcess;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.RedissonUtil;
import com.ejiaoyi.expert.dto.ClearBidProcessDTO;
import com.ejiaoyi.expert.service.IBidEvalService;
import com.ejiaoyi.expert.service.IExpertService;
import com.ejiaoyi.expert.service.impl.ConBidEvalServiceImpl;
import com.ejiaoyi.expert.support.AuthUser;
import com.ejiaoyi.expert.support.CurrentUserHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 施工 专家评标控制器
 *
 * @author Make
 * @since 2020/9/7
 */
@RestController
@RequestMapping("/expert/conBidEval")
public class ConBidEvalController extends BaseController {

    @Autowired
    private IBidSectionService bidSectionService;

    @Autowired
    private ITenderDocService tenderDocService;

    @Autowired
    private IGradeService gradeService;
    @Autowired
    private IExpertUserService expertUserService;

    @Autowired
    private IBidEvalService bidEvalService;

    @Autowired
    private IExpertReviewSingleItemDeductService expertReviewSingleItemDeductService;

    @Autowired
    private IBidSectionRelateService bidSectionRelateService;

    @Autowired
    private IFDFSService fdfsService;

    @Autowired
    private IExpertService expertService;

    @Autowired
    private IExpertReviewService expertReviewService;

    @Autowired
    private ConBidEvalServiceImpl conBidEvalService;

    @Autowired
    private IExpertReviewMutualService expertReviewMutualService;

    @Autowired
    private IBidderService bidderService;

    /**
     * 详细评审 详细方法
     *
     * @param gradeId 评分标准id
     * @return 详细方法
     */
    @RequestMapping("/loadDetailedGradeDetailed")
    public ModelAndView loadDetailedGradeDetailed(Integer gradeId) {
        ModelAndView mav = new ModelAndView("/construction/detailed/loadDetailedGradeDetailed");
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");

        //筛选 参加详细评审的投标人 筛选
        List<Bidder> bidders = bidEvalService.listBidderPassPreliminary(bidSectionId);
        // 获取详细评审的评标办法
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());
        if (gradeId == null) {
            gradeId = grades.get(0).getId();
        }
        Grade currentGrade = gradeService.getGrade(gradeId, EvalProcess.DETAILED.getCode());
        //初始化打分项
        ExpertReview expertReview = expertReviewSingleItemDeductService.initReviewItemDeduct(bidSectionId, user.getUserId(), currentGrade, bidders);

        //判断对应投标人当前评审类型是否评审完毕
        bidders = bidEvalService.listBidderGradeIsEnd(bidders, currentGrade, ScoreType.DEDUCT);

        mav.addObject("expertReview", expertReview);
        mav.addObject("currentGrade", currentGrade);
        mav.addObject("tenderDoc", tenderDoc);
        mav.addObject("bidders", bidders);
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
        return bidEvalService.getConBidderData(gradeId, bidderId);
    }

    /**
     * 保存单项的扣分结果
     *
     * @param expertReviewSingleItemDeduct 数据
     * @return 保存单项的扣分结果
     */
    @RequestMapping("/saveExpertReviewSingleItemDeduct")
    public JsonData saveExpertReviewSingleItemDeduct(ExpertReviewSingleItemDeduct expertReviewSingleItemDeduct) {
        JsonData resultDate = new JsonData();
        Integer result = expertReviewSingleItemDeductService.updateExpertReviewSingleItemDeduct(expertReviewSingleItemDeduct);
        resultDate.setCode(result.toString());
        return resultDate;
    }

    /**
     * 判断当前投标人当前grade是否检查完成
     *
     * @param bidderId 投标人id
     * @param gradeId  gradeId
     * @return 判断当前投标人当前grade是否检查完成
     */
    @RequestMapping("/validBidderDeductCompletion")
    public Boolean validBidderDeductCompletion(Integer bidderId, Integer gradeId) {
        return bidEvalService.validBidderDeductCompletion(bidderId, gradeId);
    }

    /**
     * 一键不扣分
     *
     * @param gradeId gradeId
     * @return 更新数量
     */
    @RequestMapping("/oneKeyNoDeduct")
    public Integer oneKeyNoDeduct(Integer gradeId) {
        return bidEvalService.oneKeyNoDeduct(gradeId);
    }

    /**
     * 环节重评
     */
    @RequestMapping("/restartDetailedReview")
    public void restartDetailedReview() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        bidEvalService.callPersonReview(user.getBidSectionId(), EvalProcess.DETAILED.getCode());
    }

    /**
     * 检查个人扣分结束是否符合条件
     *
     * @return 检查个人扣分结束是否符合条件
     */
    @RequestMapping("/checkPersonalDeductEnd")
    public JsonData checkPersonalDeductEnd() {
        return bidEvalService.checkPersonalDeductEnd();
    }

    /**
     * 检查是否有扣分项
     *
     * @return 检查是否有扣分项
     */
    @RequestMapping("/checkDeduct")
    public Boolean checkDeduct() {
        return bidEvalService.checkDeduct();
    }

    /**
     * 扣分项的意见填写页面
     *
     * @return
     */
    @RequestMapping("/deductOpinionPage")
    public ModelAndView deductOpinionPage() {
        ModelAndView mav = new ModelAndView("/construction/detailed/deductOpinionPage");
        List<Bidder> bidders = bidEvalService.listDeductOpinion();
        mav.addObject("bidders", bidders);
        return mav;
    }

    /**
     * 批量更新扣分原因
     *
     * @param expertReviewSingleItemDeducts 数据列表
     * @return 批量更新扣分原因
     */
    @RequestMapping("/updateDeducts")
    public Boolean updateDeducts(@RequestBody List<ExpertReviewSingleItemDeduct> expertReviewSingleItemDeducts) {
        try {
            for (ExpertReviewSingleItemDeduct expertReviewSingleItemDeduct : expertReviewSingleItemDeducts) {
                expertReviewSingleItemDeductService.updateById(expertReviewSingleItemDeduct);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 详细评审 个人评审结束
     *
     * @return 个人评审结束
     */
    @RequestMapping("/personalDetailedReviewEnd")
    public Boolean personalDetailedReviewEnd() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        return bidEvalService.updateReviewEnd(user.getBidSectionId(), EvalProcess.DETAILED.getCode());
    }

    /**
     * 详细评审 小组结束检查
     *
     * @return 小组结束检查
     */
    @RequestMapping("/validDetailedGroupEnd")
    public JsonData validDetailedGroupEnd() {
        return bidEvalService.checkDeductGroupEnd();
    }

    /**
     * 详细评审 小组结束
     *
     * @return 小组结束
     */
    @RequestMapping("/endDetailedGroupReview")
    public Boolean endDetailedGroupReview() {
        return bidEvalService.endDeductGroupReview();
    }

    /**
     * 详细评审 评审结果总框架
     *
     * @return 专家评审汇总框架页面
     */
    @RequestMapping("/evalResultDetailedIndex")
    public ModelAndView evalResultDetailedIndex() {
        ModelAndView mav = new ModelAndView("/construction/detailed/result/evalResultDetailedIndex");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        mav.addObject("bidSection", bidSection);
        mav.addObject("expert", user);
        return mav;
    }

    /**
     * 详细评审 展示专家个人评审结果框架
     *
     * @return 详细评审 展示专家个人评审结果框架
     */
    @RequestMapping("/detailedPersonPage")
    public ModelAndView detailedPersonPage() {
        ModelAndView mav = new ModelAndView("/construction/detailed/result/detailedPerson");
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        // 获取详细评审的评标办法
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());
        mav.addObject("grades", grades);
        return mav;
    }

    /**
     * 详细评审 展示专家个人评审结果明细
     *
     * @param gradeId 评标办法id
     * @return
     */
    @RequestMapping("/detailedPersonGradePage")
    public ModelAndView showResultDetailedPage(Integer gradeId) {
        ModelAndView mav = new ModelAndView("/construction/detailed/result/detailedPersonGrade");
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        // 获取进入详细评审的投标人(包括专家打分完成情况)
        List<Bidder> bidders = bidEvalService.listBidderPassPreliminary(bidSectionId);
        Grade currentGrade = gradeService.getGrade(gradeId, EvalProcess.DETAILED.getCode());

        //初始化打分项
        ExpertReview expertReview = expertReviewSingleItemDeductService.initReviewItemDeduct(bidSectionId, user.getUserId(), currentGrade, bidders);

        mav.addObject("bidders", bidders);
        mav.addObject("currentGrade", currentGrade);
        mav.addObject("expertReview", expertReview);
        return mav;
    }

    /**
     * 获取当前专家的结果数据
     *
     * @param gradeId 评审类型
     */
    @RequestMapping("/getResultData")
    public Map<String, Object> getResultData(Integer gradeId) {
        Map<String, Object> result = new HashMap<>();

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;

        ExpertReviewSingleItemDeduct query = ExpertReviewSingleItemDeduct.builder()
                .expertId(user.getUserId())
                .gradeId(gradeId)
                .build();
        List<ExpertReviewSingleItemDeduct> deducts = expertReviewSingleItemDeductService.listExpertReviewSingleItemDeduct(query);
        result.put("data", deducts);

        return result;
    }

    /**
     * 评审结果 修改结果 窗口
     *
     * @param deductId deductId
     * @return
     */
    @RequestMapping("/updateDeductPersonResultPage")
    public ModelAndView updateDeductPersonResultPage(Integer deductId) {
        ModelAndView mav = new ModelAndView("/construction/detailed/result/updateDeductPersonResult");
        ExpertReviewSingleItemDeduct expertReviewSingleItemDeduct = expertReviewSingleItemDeductService.getDeductById(deductId);

        mav.addObject("deduct", expertReviewSingleItemDeduct);
        return mav;
    }

    /**
     * 详细评审 小组汇总基础页面
     *
     * @return
     */
    @RequestMapping("/detailedGroupResultBasePage")
    public ModelAndView detailedGroupResultBasePage() {
        ModelAndView mav = new ModelAndView("/construction/detailed/result/detailedGroupResultBase");
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        // 1、加载评审名称
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        // 获取详细评审的评标办法
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());

        mav.addObject("grades", grades);
        return mav;
    }


    /**
     * 详细评审 小组汇总 投标人对应页面
     *
     * @param gradeId 评审id
     * @return
     */
    @RequestMapping("/detailedGroupResultBidderPage")
    public ModelAndView showGroupTotalResultPage(Integer gradeId) {
        ModelAndView mav = new ModelAndView("/construction/detailed/result/detailedGroupResultBidder");
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        Grade grade = gradeService.getGradeById(gradeId);


        List<Bidder> bidders = bidEvalService.listBidderPassPreliminary(bidSectionId);
        //判断是否所有人都结束了个人评审
        int isAllExpertEnd = 1;
        if (!bidEvalService.validAllExpertEval(bidSectionId, gradeId)) {
            isAllExpertEnd = 0;
        }

        List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);        //获取已经完成个人评审的评标专家
        String endExpertIds = expertUserService.listExpertIdsForPersonEnd(bidSectionId, grade.getId());
        ExpertReview query = ExpertReview.builder()
                .gradeId(grade.getId())
                .bidSectionId(bidSectionId)
                .expertId(user.getUserId())
                .build();
        ExpertReview expertReview = expertReviewService.getExpertReview(query);

        //判断当前grade是否为安全质量事故扣分或建筑市场不良记录扣分
        Boolean isConGrade = grade.getName().equals(ConDetailedMethod.BAD_RECORD_MARKET.getName()) || grade.getName().equals(ConDetailedMethod.SAFETY_QUALITY_ACCIDENT.getName());

        mav.addObject("bidders", bidders);
        mav.addObject("expertUsers", expertUsers);
        mav.addObject("grade", grade);
        mav.addObject("isAllExpertEnd", isAllExpertEnd);
        mav.addObject("expertReview", expertReview);
        mav.addObject("isConGrade", isConGrade);
        mav.addObject("endExpertIds", endExpertIds);
        return mav;
    }

    /**
     * 获取每个投标人当前grade的最后结果
     *
     * @param gradeId gradeId
     * @return 获取每个投标人当前grade的最后结果
     */
    @RequestMapping("/getBiddersGradeResult")
    public List<BidderReviewResultDeduct> getBiddersGradeResult(Integer gradeId) {
        return bidEvalService.getConBiddersGradeResult(gradeId);
    }

    /**
     * 获取当前专家的结果数据
     *
     * @param bidderId       投标人ID
     * @param gradeId        gradeId
     * @param isAllExpertEnd 是否所有专家都个人评审结束
     */
    @RequestMapping("/getBidderDataForResult")
    public Map<String, Object> getBidderDataForResult(Integer bidderId, Integer gradeId, Integer isAllExpertEnd) {
        return bidEvalService.getConBidderDataForResult(bidderId, gradeId, isAllExpertEnd);
    }

    /**
     * 评审结果 综合排名页面
     *
     * @return 综合排名页面
     */
    @RequestMapping("/rankingSummaryPage")
    public ModelAndView rankingSummaryPage() {
        ModelAndView mav = new ModelAndView("/construction/result/rankingSummary");
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        List<EvalResultSg> list = bidEvalService.listConRankingBidder(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        mav.addObject("list", list);
        mav.addObject("tenderDoc", tenderDoc);
        return mav;
    }


    /**
     * 评标报告页面
     *
     * @return 评标报告页面
     */
    @RequestMapping("/evaluationReportPage")
    public ModelAndView evaluationReportPage() {
        ModelAndView mav = new ModelAndView("/construction/result/evaluationReport");
        AuthUser user = CurrentUserHolder.getUser();
        try {
            RedissonUtil.lock(CacheName.GENERATE_EVAL_REPORT_ + user.getBidSectionId(), 300);
            Integer bidSectionId = user.getBidSectionId();
            BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
            //生成评标报告
            if (!EvalStatus.FINISH.equals(bidSection.getEvalPdfGenerateStatus())){
                bidEvalService.generateReport(user);
                bidSectionService.updateBidSectionById(BidSection.builder().id(bidSectionId).evalPdfGenerateStatus(1).build());
            }
            // 设置当前专家的签名状态
            ExpertUser expertUser = expertUserService.getExpertUserById(user.getUserId());
            if (!CommonUtil.isEmpty(expertUser)) {
                user.setSignStatus(expertUser.getSignar());
            }
            List<ExpertUser> expertUserList = expertUserService.listExperts(bidSectionId);
            // 重新签名按钮
            boolean resignButton = true;
            for (ExpertUser signExperUser : expertUserList) {
                if (!EvalStatus.PROGRESSING.equals(signExperUser.getSignar())) {
                    resignButton = false;
                    break;
                }
            }
            String mark = "/evalReport/" + bidSectionId + "/evalReport.pdf";
            mav.addObject("resignButton", resignButton);
            mav.addObject("mark", mark);
            mav.addObject("user", user);
            mav.addObject("fdfs", fdfsService.getFdfsByMark(mark));
            mav.addObject("bidSection", bidSection);
            mav.addObject("bidSectionId", bidSectionId);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            RedissonUtil.unlock(CacheName.GENERATE_EVAL_REPORT_ + user.getBidSectionId());
        }
        return mav;
    }

    /**
     * 展示报告页面
     *
     * @param id fdfsId
     * @return
     */
    @RequestMapping("/reportShowPage")
    public ModelAndView reportShowPage(Integer id) {
        ModelAndView modelAndView = new ModelAndView("/reportPdf/pdfIframe");
        Fdfs fdfs = fdfsService.getFdfdById(id);
        modelAndView.addObject("fdfs", fdfs);
        return modelAndView;
    }

    /**
     * 展示报告页面
     *
     * @param fileId fdfsId
     * @return
     */
    @RequestMapping("/reportPdfPage")
    public ModelAndView reportPdfPage(Integer fileId) {
        AuthUser user = CurrentUserHolder.getUser();
        ModelAndView modelAndView = new ModelAndView("/reportPdf/reportShow");
        Fdfs fdfs = fdfsService.getFdfdById(fileId);
        modelAndView.addObject("fdfs", fdfs);
        modelAndView.addObject("user", user);
        return modelAndView;
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
            if(CommonUtil.isEmpty(expertUser.getSignar()) || expertUser.getSignar() != 1){
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
     * 计算报价得分页面
     *
     * @return 计算报价得分页面
     */
    @RequestMapping("/priceScoreReviewIndex")
    public ModelAndView priceScoreReviewIndex() {
        ModelAndView mav = new ModelAndView();
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        if (!expertService.isGroupCompletion(gradeIds, EvalProcess.DETAILED.getCode())) {
            mav.setViewName("redirect:/expert/startEval");
            this.setSystemActionMessage("当前环节还未开始！", WebSocketMessage.Dialog.MSG, WebSocketMessage.Type.ERROR);
            return mav;
        }
        BidSectionRelate bidSectionRelate = bidSectionRelateService.getBidSectionRelateByBSId(bidSectionId);
        mav.addObject("bidSectionRelate", bidSectionRelate);
        mav.addObject("expert", user);
        mav.addObject("bidSection", bidSection);
        mav.addObject("tenderDoc", tenderDoc);

        if (!Enabled.YES.getCode().equals(bidSection.getPriceRecordStatus())) {
            String priceScoreUid = bidSectionRelate.getPriceScoreUid();
            boolean calcResult = true;
            try {
                JsonData jsonData = conBidEvalService.validPriceScore(bidSectionId);
                calcResult = !Enabled.YES.getCode().toString().equals(jsonData.getCode());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (StringUtils.isEmpty(priceScoreUid) || calcResult) {
                ExpertUser leaderExpertUser = expertUserService.getChairmanByBidSectionId(bidSectionId);
                mav.addObject("leaderExpertUser", leaderExpertUser);
                mav.setViewName("/construction/calcPrice/waitCalcPriceScorePage");
                return mav;
            }
        }
        mav.setViewName("/construction/calcPrice/priceScoreReviewIndex");
        return mav;
    }

    /**
     * 计算报价得分
     */
    @RequestMapping("/calcPriceScore")
    public void calcPriceScore() {
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
//        conBidEvalService.calcPriceScore(bidSectionId);
    }

    /**
     * 校验报价得分计算情况
     *
     * @return 报价得分计算情况
     */
    @RequestMapping("/validPriceScore")
    public JsonData validPriceScore() {
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        return conBidEvalService.validPriceScore(bidSectionId);
    }

    /**
     * 删除报价得分计算服务序列号
     */
    @RequestMapping("/removePriceUid")
    public void removePriceUid() {
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        bidSectionRelateService.updateRelateBySectionId(BidSectionRelate.builder()
                .bidSectionId(bidSectionId)
                .calcPriceUid("")
                .priceScoreUid("")
                .build());
    }

    /**
     * 展示清标进度
     *
     * @return
     */
    @RequestMapping("/clearBidProcess")
    public ModelAndView clearBidProcess() {
        ModelAndView mav = new ModelAndView("/selectLeader/viewClearBidProcessPage");
        Integer bidSectionId = CurrentUserHolder.getUser().getBidSectionId();
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        mav.addObject("tenderDoc", tenderDoc);
        return mav;
    }

    /**
     * 获取清标进度数据
     *
     * @return 清标进度数据
     */
    @RequestMapping("/initClearBidProcessData")
    public ClearBidProcessDTO initClearBidProcessData() {
        Integer bidSectionId = CurrentUserHolder.getUser().getBidSectionId();
        return conBidEvalService.initClearBidProcessData(bidSectionId);
    }

    /**
     * 其他评审 基础页面 (互保共建)
     *
     * @return 其他评审 基础页面
     */
    @RequestMapping("/otherMutualReviewIndex")
    public ModelAndView otherMutualReviewIndex() {
        ModelAndView mav = new ModelAndView("/construction/other/otherMutualReviewIndex");
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] otherGradeIds = tenderDoc.getOtherGradeId().split(",");

        List<Grade> grades = gradeService.listGrade(otherGradeIds, EvalProcess.OTHER.getCode());
        Grade grade = grades.get(0);

        mav.addObject("grade", grade);
        mav.addObject("bidSection", bidSection);
        mav.addObject("expert", user);

        return mav;
    }

    /**
     * 其他评审内容页面 (互保共建)
     *
     * @param gradeId gradeId
     * @return 其他评审内容页面 (互保共建)
     */
    @RequestMapping("/otherMutualContentPage")
    public ModelAndView otherMutualContentPage(Integer gradeId) {
        ModelAndView mav = new ModelAndView("/construction/other/otherMutualContent");

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);

        //筛选 通过初步评审的投标人
        List<Bidder> bidders = bidEvalService.listBidderPassPreliminary(bidSectionId);

        Grade currentGrade = gradeService.getGrade(gradeId, EvalProcess.OTHER.getCode());

        //初始化打分项
        ExpertReview expertReview = expertReviewMutualService.initReviewMutual(bidSectionId, user.getUserId(), gradeId, bidders);

        //判断对应投标人当前评审类型是否评审完毕
        bidders = conBidEvalService.otherMutualGradeIsEnd(bidders, gradeId, user.getUserId());

        mav.addObject("expertReview", expertReview);
        mav.addObject("currentGrade", currentGrade);
        mav.addObject("tenderDoc", tenderDoc);
        mav.addObject("bidders", bidders);
        mav.addObject("expert", user);

        return mav;
    }

    /**
     * 获取投标人结果数据
     *
     * @param gradeId  当前gradeID
     * @param bidderId 投标人iD
     * @return 结果数据
     */
    @RequestMapping("/getOtherMutualBidderData")
    public List<ExpertReviewMutual> getOtherMutualBidderData(Integer gradeId, Integer bidderId) {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        List<ExpertReviewMutual> result = expertReviewMutualService.listExpertReviewMutual(ExpertReviewMutual.builder()
                .expertId(user.getUserId())
                .gradeId(gradeId)
                .bidderId(bidderId).build(), false);
        return result;
    }

    /**
     * 保存 结果
     *
     * @param expertReviewMutual 数据
     * @return 保存 结果
     */
    @RequestMapping("/saveMutualResult")
    public JsonData saveMutualResult(ExpertReviewMutual expertReviewMutual) {
        JsonData result = new JsonData();
        if (expertReviewMutualService.updateById(expertReviewMutual)) {
            result.setCode("1");
        } else {
            result.setCode("2");
        }
        return result;
    }

    /**
     * 判断专家 是否满足个人结束条件
     *
     * @param gradeId gradeId
     * @return 是否满足个人结束条件
     */
    @RequestMapping("/checkPersonalOtherMutualEnd")
    public JsonData checkPersonalOtherMutualEnd(Integer gradeId) {
        return conBidEvalService.checkPersonalOtherMutualEnd(gradeId);
    }

    /**
     * 互保共建 个人评审结束
     *
     * @return 个人评审结束
     */
    @RequestMapping("/personalOtherMutualEnd")
    public Boolean personalOtherMutualEnd() {
        return conBidEvalService.personalOtherMutualEnd();
    }

    /**
     * 互保共建 环节重评
     */
    @RequestMapping("/restartOtherMutualReview")
    public void restartOtherMutualReview() {
        conBidEvalService.restartOtherMutualReview();
    }

    /**
     * 互保共建 小组结束检查
     *
     * @return 小组结束检查
     */
    @RequestMapping("/validOtherMutualGroupEnd")
    public JsonData validOtherMutualGroupEnd() {
        return conBidEvalService.validOtherMutualGroupEnd();
    }

    /**
     * 互保共建 评审结果总框架
     *
     * @return 专家评审汇总框架页面
     */
    @RequestMapping("/evalResultMutualIndex")
    public ModelAndView evalResultMutualIndex() {
        ModelAndView mav = new ModelAndView("/construction/other/result/evalResultMutualIndex");
        AuthUser user = CurrentUserHolder.getUser();
        mav.addObject("expert", user);
        return mav;
    }

    /**
     * 互保共建 展示专家个人评审结果框架
     *
     * @return 互保共建 展示专家个人评审结果框架
     */
    @RequestMapping("/showPersonMutualResultPage")
    public ModelAndView showPersonReviewResultPage() {
        ModelAndView mav = new ModelAndView("/construction/other/result/showPersonMutualResult");

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getOtherGradeId().split(",");

        //筛选 通过初步评审的投标人
        List<Bidder> bidders = bidEvalService.listBidderPassPreliminary(bidSectionId);

        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.OTHER.getCode());
        Grade grade = grades.get(0);

        //是否个人评审结束
        ExpertReview expertReview = expertReviewMutualService.initReviewMutual(bidSectionId, user.getUserId(), grade.getId(), bidders);

        mav.addObject("bidders", bidders);
        mav.addObject("expertReview", expertReview);
        mav.addObject("grade", grade);
        return mav;
    }

    /**
     * 互保共建 获取专家个人打分结果
     *
     * @param gradeId gradeID
     * @return 专家ID
     */
    @RequestMapping("/getPersonMutualResultData")
    public List<ExpertReviewMutual> getPersonMutualResultData(Integer gradeId) {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;

        return expertReviewMutualService.listExpertReviewMutual(ExpertReviewMutual.builder()
                .expertId(user.getUserId())
                .gradeId(gradeId).build(), false);
    }

    /**
     * 互保共建 小组评审汇总
     *
     * @return 小组评审汇总
     */
    @RequestMapping("/showGroupMutualResultPage")
    public ModelAndView showGroupMutualResultPage() {
        ModelAndView mav = new ModelAndView("/construction/other/result/showGroupMutualResult");

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getOtherGradeId().split(",");

        //筛选 通过初步评审的投标人
        List<Bidder> bidders = bidEvalService.listBidderPassPreliminary(bidSectionId);

        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.OTHER.getCode());
        Grade grade = grades.get(0);

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
        if (!bidEvalService.validAllExpertEval(bidSectionId, grade.getId())) {
            isAllExpertEnd = 0;
        }

        mav.addObject("bidders", bidders);
        mav.addObject("expertUsers", expertUsers);
        mav.addObject("expertReview", expertReview);
        mav.addObject("grade", grade);
        mav.addObject("isAllExpertEnd", isAllExpertEnd);
        mav.addObject("grade", grade);
        mav.addObject("endExpertIds", endExpertIds);
        return mav;
    }

    /**
     * 互保共建 获取专家的结果数据
     *
     * @param bidderId       投标人ID
     * @param gradeId        gradeId
     * @param isAllExpertEnd 是否所有专家都个人评审结束
     */
    @RequestMapping("/getBidderMutualResultData")
    public Map<String, Object> getBidderMutualResultData(Integer bidderId, Integer gradeId, Integer isAllExpertEnd) {
        return conBidEvalService.getBidderMutualResultData(bidderId, gradeId, isAllExpertEnd);
    }

    /**
     * 互保共建 获取所有投标人的结果数据
     *
     * @param gradeId gradeId
     * @return 获取所有投标人的结果数据
     */
    @RequestMapping("/getGroupMutualResultData")
    public List<BidderResultDTO> getGroupMutualResultData(Integer gradeId) {
        return conBidEvalService.getGroupMutualResultData(gradeId);
    }

    /**
     * 修改单一结果页面
     * @param mutualId 结果ID
     * @return 修改单一结果页面
     */
    @RequestMapping("/updateMutualResultPage")
    public ModelAndView updateMutualResultPage(Integer mutualId){
        ModelAndView mav = new ModelAndView("/construction/other/result/updateMutualResult");
        ExpertReviewMutual mutual = expertReviewMutualService.getById(mutualId);
        Grade currentGrade = gradeService.getGrade(mutual.getGradeId(), EvalProcess.OTHER.getCode());
        Bidder bidder = bidderService.getBidderById(mutual.getBidderId());
        mav.addObject("mutual", mutual);
        mav.addObject("currentGrade", currentGrade);
        mav.addObject("bidder", bidder);
        return mav;
    }

    /**
     * 互保共建 小组结束
     *
     * @return 小组结束
     */
    @RequestMapping("/endGroupMutualResult")
    public Boolean endGroupMutualResult(Integer gradeId) {
        return conBidEvalService.endGroupMutualResult(gradeId);
    }
}
