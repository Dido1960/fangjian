package com.ejiaoyi.expert.controller;

import com.ejiaoyi.common.constant.CacheName;
import com.ejiaoyi.common.constant.EvalStatus;
import com.ejiaoyi.common.constant.RankingConstant;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.dto.WebSocketMessage;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.EvalProcess;
import com.ejiaoyi.common.enums.ExecuteCode;
import com.ejiaoyi.common.enums.Status;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.RedisUtil;
import com.ejiaoyi.common.util.RedissonUtil;
import com.ejiaoyi.expert.service.IBidEvalService;
import com.ejiaoyi.expert.service.IOtherBidEvalService;
import com.ejiaoyi.expert.support.AuthUser;
import com.ejiaoyi.expert.support.CurrentUserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 勘察设计电梯 专家评标控制器
 *
 * @author Make
 * @since 2020/9/7
 */
@RestController
@RequestMapping("/expert/otherBidEval")
public class OtherBidEvalController extends BaseController {

    @Autowired
    private IBidSectionService bidSectionService;

    @Autowired
    private ICandidateResultsService candidateResultsService;

    @Autowired
    private IExpertUserService expertUserService;

    @Autowired
    private IBidderService bidderService;

    @Autowired
    private IBidEvalService bidEvalService;

    @Autowired
    private IExpertReviewService expertReviewService;

    @Autowired
    private ITenderDocService tenderDocService;

    @Autowired
    private IGradeService gradeService;

    @Autowired
    private IOtherBidEvalService otherBidEvalService;

    @Autowired
    private IFDFSService fdfsService;

    @Autowired
    private ICandidateSuccessService candidateSuccessService;

    @Autowired
    ISignatureService signatureService;

    /**
     * 详细评审项内容 推荐中标候选人页面
     *
     * @return
     */
    @RequestMapping("/loadDetailedGradePage")
    public ModelAndView loadDetailedGradePage(Integer gradeId) {
        ModelAndView mav = new ModelAndView("/otherEvalPlan/detailed/candidateSelect");
        // 获取当前操作的标段以及专家
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());
        if (gradeId == null) {
            gradeId = grades.get(0).getId();
        }
        Grade currentGrade = bidEvalService.getGradeDetailItem(bidSectionId, gradeId, EvalProcess.DETAILED.getCode());

        // 个人推选是否结束
        Boolean isPersonalSelectionEnd = candidateResultsService.isPersonalSelectionEnd(bidSectionId, user.getUserId());
        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);

        // 初始化候选人
        candidateResultsService.initCandidateResult(bidSectionId, user.getUserId());

        mav.addObject("expert", user);
        mav.addObject("isPersonalSelectionEnd", isPersonalSelectionEnd);
        mav.addObject("bidSection", bidSection);
        mav.addObject("currentGrade", currentGrade);
        mav.addObject("grades", grades);
        mav.addObject("bidders", bidders);
        mav.addObject("evalProcess", EvalProcess.DETAILED.getCode());
        mav.addObject("tenderDoc", tenderDoc);

        return mav;
    }

    /**
     * 获取投标人数据（评审点）
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
     * 添加推选候选人信息
     *
     * @param candidateResults 推选人信息
     * @param bId              投标人id
     * @param why              原因
     * @return
     */
    @RequestMapping("/addCandidate")
    public Boolean addCandidate(CandidateResults candidateResults, Integer bId, String why) {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        candidateResults.setExpertId(user.getUserId());
        candidateResults.setBidSectionId(user.getBidSectionId());
        return candidateResultsService.addCandidateResult(candidateResults, bId, why);
    }

    /**
     * 专家修改候选人信息（意见）
     *
     * @param candidateResults 推选人信息
     * @return
     */
    @RequestMapping("/updateCandidate")
    public Boolean updateCandidate(CandidateResults candidateResults) {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        candidateResults.setExpertId(user.getUserId());
        candidateResults.setBidSectionId(user.getBidSectionId());
        return candidateResultsService.updateCandidateById(candidateResults);
    }


    /**
     * 校验候选人（专家个人推选）是否重复
     *
     * @return
     */
    @RequestMapping("/validRecommend")
    public Boolean validRecommend() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        return otherBidEvalService.validRecommend(user.getUserId(), user.getBidSectionId());
    }

    /**
     * 校验中标候选人（专家组长推选）是否重复
     *
     * @return
     */
    @RequestMapping("/validLeaderRecommend")
    public JsonData validLeaderRecommend() {
        JsonData data = new JsonData();
        data.setCode(ExecuteCode.FAIL.getCode().toString());
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        String errorInfo = otherBidEvalService.validLeaderRecommend(user.getBidSectionId());
        data.setMsg(errorInfo);
        if (CommonUtil.isEmpty(errorInfo)) {
            data.setCode(ExecuteCode.SUCCESS.getCode().toString());
        }
        return data;
    }


    /**
     * 组长推选评标委员会 投票无结果 的候选人 页面
     *
     * @param candidateSuccess 推选人信息
     * @return
     */
    @RequestMapping("/addCandidateSuccess")
    public Boolean addCandidateSuccess(CandidateSuccess candidateSuccess) {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        candidateSuccess.setBidSectionId(user.getBidSectionId());
        return candidateSuccessService.addCandidateSuccess(candidateSuccess);
    }

    /**
     * 个人评审结束
     *
     * @param bidSectionId 标段id
     * @return
     */
    @RequestMapping("/personalReviewEnd")
    public JsonData personalReviewEnd(Integer bidSectionId) {
        JsonData data = new JsonData();
        data.setCode(ExecuteCode.FAIL.getCode().toString());
        AuthUser user = CurrentUserHolder.getUser();
        Integer expertId = user.getUserId();

        if (bidEvalService.isFreeBackApplying(bidSectionId)) {
            data.setCode("2");
            data.setMsg("评审回退尚未审核，操作失败!");
            return data;
        }

        // 获取专家推选的前三名投标人
        CandidateResults firstCandidate = candidateResultsService.getCandidateByRanking(bidSectionId, user.getUserId(), RankingConstant.FIRST_PLACE);

        List<CandidateResults> candidateResults = candidateResultsService.listCandidate(expertId, bidSectionId);
        boolean validResult = candidateResults.size() == 0;
        for (CandidateResults candidateResult : candidateResults) {
            if (CommonUtil.isEmpty(candidateResult.getBidderId())) {
                validResult = true;
            }
        }
        if (validResult) {
            data.setMsg("请完善推荐候选人");
            return data;
        }
        if (!otherBidEvalService.validRecommend(expertId, bidSectionId)) {
            data.setMsg("选候人推选存在重复！");
            return data;
        }

        firstCandidate.setIsEnd(1);
        if (candidateResultsService.updateCandidateById(firstCandidate)) {
            data.setCode(ExecuteCode.SUCCESS.getCode().toString());
        }
        return data;
    }

    /**
     * 推荐汇总表页面
     *
     * @return
     */
    @RequestMapping("/recommendSummaryPage")
    public ModelAndView recommendSummaryPage() {
        ModelAndView mav = new ModelAndView("/otherEvalPlan/detailed/recommendSummary");
        // 获取当前操作的标段以及专家
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);
        //获取专家的推选情况
        for (ExpertUser expertUser : expertUsers) {
            expertUser.setCandidateResults(candidateResultsService.listCandidate(CandidateResults.builder()
                    .bidSectionId(bidSectionId)
                    .expertId(expertUser.getId())
                    .build()));
        }
        mav.addObject("expertUsers", expertUsers);
        return mav;
    }

    /**
     * 推荐汇总表页面
     *
     * @return
     */
    @RequestMapping("/summaryVotesPage")
    public ModelAndView summaryVotesPage() {
        ModelAndView mav = new ModelAndView("/otherEvalPlan/detailed/summaryVotes");
        // 获取当前操作的标段以及专家
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        // 个人评审结果数据
        List<CandidateResults> candidates = candidateResultsService.listCandidate(user.getUserId(), bidSectionId);
        // 评审结果票数统计
        List<Bidder> bidders = otherBidEvalService.getBidderVoteNum(bidSectionId);
        // 中标候选人
        List<Bidder> successBidder = new ArrayList<>();
        List<CandidateSuccess> candidateSuccesses = candidateSuccessService.listCandidateSuccess(bidSectionId);
        if (candidateSuccesses.size() > 0) {
            for (CandidateSuccess cs : candidateSuccesses) {
                Integer bidderId = cs.getBidderId();
                if (!CommonUtil.isEmpty(bidderId)) {
                    Bidder bidder = bidderService.getBidderById(bidderId).setCandidateSuccess(cs);
                    successBidder.add(bidder);
                }
            }
        }
        mav.addObject("candidates", candidates);
        mav.addObject("bidders", bidders);
        mav.addObject("successBidder", successBidder);
        return mav;
    }

    /**
     * 推荐候选人弹窗页面
     *
     * @return
     */
    @RequestMapping("/recommendCandidatePage")
    public ModelAndView recommendCandidatePage() {
        ModelAndView mav = new ModelAndView("/otherEvalPlan/detailed/recommendCandidate");
        // 获取当前操作的标段以及专家
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);
        CandidateResults recommendOne = candidateResultsService.getCandidateByRanking(bidSectionId, user.getUserId(), RankingConstant.FIRST_PLACE);
        CandidateResults recommendTwo = candidateResultsService.getCandidateByRanking(bidSectionId, user.getUserId(), RankingConstant.SECOND_PLACE);
        CandidateResults recommendThree = candidateResultsService.getCandidateByRanking(bidSectionId, user.getUserId(), RankingConstant.THIRD_PLACE);

        mav.addObject("bidders", bidders);
        mav.addObject("recommendOne", recommendOne);
        mav.addObject("recommendTwo", recommendTwo);
        mav.addObject("recommendThree", recommendThree);

        return mav;
    }

    /**
     * 组长最终决定投票无结果的候选人弹窗页面
     *
     * @return
     */
    @RequestMapping("/leaderCandidatePage")
    public ModelAndView leaderCandidatePage() {
        ModelAndView mav = new ModelAndView("/otherEvalPlan/detailed/leaderCandidate");
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        // 评审结果票数统计
        List<Bidder> bidders = otherBidEvalService.getBidderVoteNum(bidSectionId);

        CandidateSuccess firstCs = candidateSuccessService.getCandidateSuccess(bidSectionId, RankingConstant.FIRST_PLACE);
        CandidateSuccess secondCs = candidateSuccessService.getCandidateSuccess(bidSectionId, RankingConstant.SECOND_PLACE);
        CandidateSuccess thirdCs = candidateSuccessService.getCandidateSuccess(bidSectionId, RankingConstant.THIRD_PLACE);

        mav.addObject("bidders", bidders);
        mav.addObject("firstBidder", firstCs);
        mav.addObject("secondBidder", secondCs);
        mav.addObject("thirdBidder", thirdCs);
        mav.addObject("bidSectionId", bidSectionId);

        return mav;
    }

    /**
     * 验证评标委员会评审结束的相关条件
     * 个人评审是否结束
     *
     * @param bidSectionId 标段id
     * @param evalProcess  评审环节
     * @return
     */
    @RequestMapping("/validGroupReview")
    public Map<String, Object> validGroupReview(Integer bidSectionId, Integer evalProcess) {
        return otherBidEvalService.validGroupReview(bidSectionId, evalProcess);
    }

    /**
     * 评审结果 中标候选人页面
     *
     * @return 中标候选人名单页面
     */
    @RequestMapping("/successCandidatePage")
    public ModelAndView successCandidatePage() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        ModelAndView mav = new ModelAndView("/otherEvalPlan/result/successCandidate");
        Integer bidSectionId = user.getBidSectionId();
        CandidateSuccess firstCs = candidateSuccessService.getCandidateSuccess(bidSectionId, RankingConstant.FIRST_PLACE);
        CandidateSuccess secondCs = candidateSuccessService.getCandidateSuccess(bidSectionId, RankingConstant.SECOND_PLACE);
        CandidateSuccess thirdCs = candidateSuccessService.getCandidateSuccess(bidSectionId, RankingConstant.THIRD_PLACE);

        Bidder firstBidder = bidderService.getBidderById(firstCs.getBidderId()).setCandidateSuccess(firstCs);
        Bidder secondBidder = bidderService.getBidderById(secondCs.getBidderId()).setCandidateSuccess(secondCs);
        Bidder thirdBidder = thirdCs != null ? bidderService.getBidderById(thirdCs.getBidderId()).setCandidateSuccess(thirdCs) : null;

        mav.addObject("firstBidder", firstBidder);
        mav.addObject("secondBidder", secondBidder);
        mav.addObject("thirdBidder", thirdBidder);
        return mav;
    }

    /**
     * 评标报告页面
     *
     * @return 评标报告页面
     */
    @RequestMapping("/evaluationReportPage")
    public ModelAndView evaluationReportPage() {
        ModelAndView mav = new ModelAndView("/otherEvalPlan/result/evaluationReport");
        AuthUser user = CurrentUserHolder.getUser();
        try {
            RedissonUtil.lock(CacheName.GENERATE_EVAL_REPORT_ + user.getBidSectionId(), 300, TimeUnit.SECONDS);
            // 获取签名状态
            user.setSignStatus(expertUserService.getExpertUserById(user.getUserId()).getSignar());

            Integer bidSectionId = user.getBidSectionId();
            BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
            //生成评标报告
            if (!EvalStatus.PROGRESSING.equals(bidSection.getEvalPdfGenerateStatus())) {
                // 重新生成报告
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
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        ModelAndView mav = new ModelAndView("/otherEvalPlan/result/evaluationEnd");
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
     * 评标委员会评审结束
     *
     * @return
     */
    @RequestMapping("/endGroupReview")
    public boolean endGroupReview(Integer bidSectionId, Integer evalProcess) {
        try {
            bidEvalService.updateGroupReviewEndStatus(bidSectionId, evalProcess);
            return true;
        } catch (Exception e) {
            e.getStackTrace();
            return false;
        }
    }
}
