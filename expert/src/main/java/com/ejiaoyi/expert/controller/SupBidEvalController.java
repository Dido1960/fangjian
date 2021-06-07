package com.ejiaoyi.expert.controller;

import com.ejiaoyi.common.constant.CacheName;
import com.ejiaoyi.common.constant.EvalStatus;
import com.ejiaoyi.common.dto.BidderResultDTO;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.EvalProcess;
import com.ejiaoyi.common.enums.ReviewType;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.RedissonUtil;
import com.ejiaoyi.expert.service.IBidEvalService;
import com.ejiaoyi.expert.support.AuthUser;
import com.ejiaoyi.expert.support.CurrentUserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 监理 专家评标控制器
 *
 * @author Make
 * @since 2020/9/7
 */
@RestController
@RequestMapping("/expert/supBidEval")
public class SupBidEvalController extends BaseController {

    @Autowired
    private IBidSectionService bidSectionService;

    @Autowired
    private ITenderDocService tenderDocService;

    @Autowired
    private IGradeService gradeService;

    @Autowired
    private IExpertUserService expertUserService;

    @Autowired
    private IBidderService bidderService;

    @Autowired
    private IBidEvalService bidEvalService;

    @Autowired
    private IExpertReviewService expertReviewService;

    @Autowired
    private IExpertReviewSingleItemScoreService expertReviewSingleItemScoreService;

    @Autowired
    private IExpertReviewSingleItemDeductScoreService expertReviewSingleItemDeductScoreService;

    @Autowired
    private IFDFSService fdfsService;

    @Autowired
    ISignatureService signatureService;

    /**
     * 详细评审加载投标人
     *
     * @param reviewType 评审类型
     * @return 详细评审加载投标人
     */
    @RequestMapping("/loadDetailedGradeDetailedPage")
    public ModelAndView loadDetailedGradeDetailedPage(Integer reviewType) {
        ModelAndView mav = new ModelAndView("/supervisor/detailed/loadDetailedGradeDetailed");

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        //筛选 参加详细评审的投标人 筛选
        List<Bidder> bidders = bidEvalService.listBidderPassPreliminary(bidSectionId);
        List<Grade> grades = gradeService.listGradeByReviewType(gradeIds, EvalProcess.DETAILED.getCode(), reviewType);
        //此处保证reviewType 下的所有expertReview 保持一致 如果其中一个没有则表示所有数据都未被初始化，则初始化数据
        //初始化打分项
        ExpertReview expertReview;
        //判断当前加载的类型是否为违章行为
        if (ReviewType.VIOLATION.getCode().equals(reviewType)) {
            expertReview = expertReviewSingleItemDeductScoreService.initReviewItemDeductScore(bidSectionId, user.getUserId(), grades, bidders);
        } else {
            expertReview = expertReviewSingleItemScoreService.initReviewItemScore(bidSectionId, user.getUserId(), grades, bidders);
        }
        //判断对应投标人对当前评审类型的评审方法是否评审完成当前评审类型是否评审完毕
        bidders = bidEvalService.listBidderGradesIsEnd(bidders, grades, reviewType);

        //是否为违章行为
        boolean isViolation = ReviewType.VIOLATION.getCode().equals(reviewType);

        mav.addObject("bidders", bidders);
        mav.addObject("grades", grades);
        mav.addObject("expertReview", expertReview);
        mav.addObject("expert", user);
        mav.addObject("reviewType", reviewType);
        mav.addObject("tenderDoc", tenderDoc);
        mav.addObject("isViolation", isViolation);

        return mav;
    }

    @RequestMapping("/getBidderData")
    public Map<String, Object> getBidderData(Integer bidderId, Integer reviewType) {
        return bidEvalService.getSupBidderData(bidderId, reviewType);
    }

    /**
     * 保存单项打分数据
     *
     * @param id         单项ID
     * @param score      大分分数
     * @param reviewType 评审类型
     * @return 保存结果
     */
    @RequestMapping("/saveDetailedResult")
    public JsonData saveDetailedResult(Integer id, String score, Integer reviewType) {
        return bidEvalService.saveSupDetailedResult(id, score, reviewType);
    }

    /**
     * 检查当前投标人当前评审类型是否打分完成
     *
     * @param bidderId   投标人Id
     * @param reviewType 评审类型
     * @return 检查当前投标人当前评审类型是否打分完成
     */
    @RequestMapping("/validBidderScoreCompletion")
    public Boolean validBidderScoreCompletion(Integer bidderId, Integer reviewType) {
        return bidEvalService.validSupBidderCompletion(bidderId, reviewType);
    }

    /**
     * 检查是否可以个人评审结束
     *
     * @return 检查是否可以个人评审结束
     */
    @RequestMapping("/checkSupPersonalEnd")
    public JsonData checkSupPersonalEnd() {
        return bidEvalService.checkSupPersonalEnd();
    }

    /**
     * 个人评审结束
     *
     * @return 个人评审结束
     */
    @RequestMapping("/supPersonalEnd")
    public Boolean supPersonalEnd() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        return bidEvalService.updateReviewEnd(user.getBidSectionId(), EvalProcess.DETAILED.getCode());
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
     * 一键不扣分
     */
    @RequestMapping("/oneKeyNoDeduct")
    public void oneKeyNoDeduct() {
        bidEvalService.supOneKeyNoDeduct();
    }

    /**
     * 小组评审结束检查
     *
     * @return 小组评审结束检查
     */
    @RequestMapping("/checkSupGroupEnd")
    public JsonData checkSupGroupEnd() {
        return bidEvalService.checkSupGroupEnd();
    }

    /**
     * 小组评审结束
     *
     * @return 小组评审结束
     */
    @RequestMapping("/endSupGroupReview")
    public Boolean endDetailedGroupReview() {
        return bidEvalService.endSupGroupReview();
    }

    /**
     * 详细评审 评审结果总框架
     *
     * @return 专家评审汇总框架页面
     */
    @RequestMapping("/evalResultDetailedIndex")
    public ModelAndView evalResultDetailedIndex() {
        ModelAndView mav = new ModelAndView("/supervisor/detailed/result/evalResultDetailedIndex");
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
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
        ModelAndView mav = new ModelAndView("/supervisor/detailed/result/detailedPerson");
        //监理 评审类型 并计算每个type的总分
        List<ReviewType> reviewTypes = bidEvalService.listSupReviewType();
        mav.addObject("reviewTypes", reviewTypes);
        return mav;
    }

    /**
     * 详细评审 展示专家个人评审结果明细
     *
     * @param reviewType 评审类型
     * @return
     */
    @RequestMapping("/detailedPersonGradePage")
    public ModelAndView detailedPersonGradePage(Integer reviewType) {
        ModelAndView mav = new ModelAndView("/supervisor/detailed/result/detailedPersonGrade");
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");

        // 获取进入详细评审的投标人(包括专家打分完成情况)
        List<Bidder> bidders = bidEvalService.listBidderPassPreliminary(bidSectionId);

        // 获取详细评审当前评审类型的评标办法
        List<Grade> grades = gradeService.listGradeByReviewType(gradeIds, EvalProcess.DETAILED.getCode(), reviewType);

        //是否个人评审结束
        ExpertReview expertReview;
        //判断当前加载的类型是否为违章行为
        if (ReviewType.VIOLATION.getCode().equals(reviewType)) {
            expertReview = expertReviewSingleItemDeductScoreService.initReviewItemDeductScore(bidSectionId, user.getUserId(), grades, bidders);
        } else {
            expertReview = expertReviewSingleItemScoreService.initReviewItemScore(bidSectionId, user.getUserId(), grades, bidders);
        }
        mav.addObject("bidders", bidders);
        mav.addObject("grades", grades);
        mav.addObject("expertReview", expertReview);
        mav.addObject("reviewType", reviewType);
        return mav;
    }

    /**
     * 获取当前专家的结果数据
     *
     * @param reviewType 评审类型
     */
    @RequestMapping("/getResultData")
    public Map<String, Object> getResultData(Integer reviewType) {
        Map<String, Object> result = new HashMap<>();

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");

        if (ReviewType.VIOLATION.getCode().equals(reviewType)) {
            //违章行为
            List<ExpertReviewSingleItemDeductScore> deductScoreList = expertReviewSingleItemDeductScoreService.listDeductScoreBySth(gradeIds, null, user.getUserId(), null, reviewType);
            result.put("data", deductScoreList);
        } else {
            List<ExpertReviewSingleItemScore> scoreList = expertReviewSingleItemScoreService.listScoreBySth(gradeIds, null, user.getUserId(), null, EvalProcess.DETAILED.getCode(), reviewType);
            result.put("data", scoreList);
        }
        return result;
    }

    /**
     * 详细评审 小组汇总基础页面
     *
     * @return 小组汇总基础页面
     */
    @RequestMapping("/detailedGroupResultBasePage")
    public ModelAndView detailedGroupResultBasePage() {
        ModelAndView mav = new ModelAndView("/supervisor/detailed/result/detailedGroupResultBase");
        // 监理 评审类型
        List<ReviewType> reviewTypes = bidEvalService.listSupReviewType();
        mav.addObject("reviewTypes", reviewTypes);
        return mav;
    }

    /**
     * 小组汇总 详细结果页面
     * @param reviewType 评审类型
     * @return 详细结果页面
     */
    @RequestMapping("/groupResultPage")
    public ModelAndView groupResultPage(Integer reviewType) {
        ModelAndView mav = new ModelAndView("/supervisor/detailed/result/detailedGroupResult");

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");

        // 获取进入详细评审的投标人(包括专家打分完成情况)
        List<Bidder> bidders = bidEvalService.listBidderPassPreliminary(bidSectionId);
        List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);
        // 获取详细评审当前评审类型的评标办法
        List<Grade> grades = gradeService.listGradeByReviewType(gradeIds, EvalProcess.DETAILED.getCode(), reviewType);
        //获取已经完成个人评审的评标专家
        String endExpertIds = expertUserService.listExpertIdsForPersonEnd(bidSectionId, grades.get(0).getId());

        //判断是否所有人已经结束个人评审
        Integer isAllExpertEnd = 1;
        for (int i = 0; i < grades.size(); i++) {
            Grade grade = grades.get(i);
            // 检查所有专家是否结束个人评审
            if (!bidEvalService.validAllExpertEval(bidSectionId, grade.getId())) {
                isAllExpertEnd = 0;
                break;
            }
        }

        ExpertReview query = ExpertReview.builder()
                .gradeId(grades.get(0).getId())
                .bidSectionId(bidSectionId)
                .expertId(user.getUserId())
                .build();
        ExpertReview expertReview = expertReviewService.getExpertReview(query);

        mav.addObject("bidders", bidders);
        mav.addObject("expertUsers", expertUsers);
        mav.addObject("grades", grades);
        mav.addObject("reviewType", reviewType);
        mav.addObject("isAllExpertEnd", isAllExpertEnd);
        mav.addObject("expertReview", expertReview);
        mav.addObject("endExpertIds", endExpertIds);
        return mav;
    }

    /**
     * 获取每个投标人当前reviewType的最后结果
     * @param reviewType reviewType
     * @return 获取每个投标人当前reviewType的最后结果
     */
    @RequestMapping("/getBiddersReviewResult")
    public List<BidderResultDTO> getBiddersReviewResult(Integer reviewType){
        return bidEvalService.getSupBiddersReviewResult(reviewType);
    }

    /**
     * 获取当前投标人的结果数据
     * @param bidderId 投标人ID
     * @param reviewType 评审类型
     * @param isAllExpertEnd 是否所有专家都个人评审结束
     */
    @RequestMapping("/getBidderDataForResult")
    public Map<String, Object> getBidderDataForResult(Integer bidderId, Integer reviewType, Integer isAllExpertEnd){
        return bidEvalService.getSupBidderDataForResult(bidderId, reviewType, isAllExpertEnd);
    }

    /**
     * 评审结果 综合排名页面
     *
     * @return 综合排名页面
     */
    @RequestMapping("/rankingSummaryPage")
    public ModelAndView rankingSummaryPage() {
        ModelAndView mav = new ModelAndView("/supervisor/result/rankingSummary");
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        List<EvalResultJl> list = bidEvalService.listSupRankingBidder(bidSectionId);
        mav.addObject("list", list);
        return mav;
    }

    /**
     * 评标报告页面
     *
     * @return 评标报告页面
     */
    @RequestMapping("/evaluationReportPage")
    public ModelAndView evaluationReportPage() {
        ModelAndView mav = new ModelAndView("/supervisor/result/evaluationReport");
        AuthUser user = CurrentUserHolder.getUser();
        try {

            RedissonUtil.lock(CacheName.GENERATE_EVAL_REPORT_ + user.getBidSectionId(), 300, TimeUnit.SECONDS);
            Integer bidSectionId = user.getBidSectionId();
            BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
            //生成评标报告
            if (!EvalStatus.FINISH.equals(bidSection.getEvalPdfGenerateStatus())) {
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
     *
     * @return
     */
    @RequestMapping("/reReport")
    public JsonData reReport() {
        AuthUser user = CurrentUserHolder.getUser();
        JsonData jsonData = new JsonData();
        // 获取标段信息
        BidSection bidSection = bidSectionService.getBidSectionById(user.getBidSectionId());
        // 修改评标报告生成状态
        bidSectionService.updateBidSectionById(BidSection.builder()
                .id(bidSection.getId())
                .evalPdfGenerateStatus(0)
                .build());

        // 重新生成报告时，清除签名信息
        signatureService.updateExpertSigarStatus(user.getBidSectionId());
        return jsonData;
    }

    /**
     * 评标结束页面
     *
     * @return 评标结束页面
     */
    @RequestMapping("/evaluationEndPage")
    public ModelAndView evaluationEndPage() {
        ModelAndView mav = new ModelAndView("/supervisor/result/evaluationEnd");
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

}
