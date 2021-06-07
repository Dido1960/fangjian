package com.ejiaoyi.supervise.controller;

import com.ejiaoyi.common.annotation.RedissonLock;
import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.constant.BackStatus;
import com.ejiaoyi.common.constant.CacheName;
import com.ejiaoyi.common.constant.EvalStatus;
import com.ejiaoyi.common.dto.BidderResultDTO;
import com.ejiaoyi.common.dto.WebSocketMessage;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.*;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.RedisUtil;
import com.ejiaoyi.common.util.RedissonUtil;
import com.ejiaoyi.supervise.service.IBidEvalService;
import com.ejiaoyi.supervise.support.AuthUser;
import com.ejiaoyi.supervise.support.CurrentUserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 评标进度控制器
 *
 * @author yyb
 * @date 2020-9-1 15:14
 */
@RestController
@RequestMapping("/gov/bidEval")
public class BidEvalController extends BaseController {

    @Autowired
    private IExpertUserService expertUserService;
    @Autowired
    private IBidSectionService bidSectionService;
    @Autowired
    private IBidEvalService bidEvalService;
    @Autowired
    private IProjectPauseService projectPauseService;
    @Autowired
    private ITenderProjectService tenderProjectService;
    @Autowired
    private IBidderService bidderService;
    @Autowired
    private IGradeService gradeService;
    @Autowired
    private IFreeBackApplyService freeBackApplyService;
    @Autowired
    private ITenderDocService tenderDocService;
    @Autowired
    private ICalcScoreParamService calcScoreParamService;
    @Autowired
    private IEvalResultSgService evalResultSgService;
    @Autowired
    private IEvalResultJlService evalResultJlService;
    @Autowired
    private IEvalResultEpcService evalResultEpcService;
    @Autowired
    private IBidSectionRelateService bidSectionRelateService;
    @Autowired
    private IBackPushStatusService backPushStatusService;
    @Autowired
    private IReevalLogService reevalLogService;
    @Autowired
    private IClearBidV3Service clearBidV3Service;

    @RequestMapping("/bidEvalBase")
    public ModelAndView bidEvalBase() {
        ModelAndView mav = new ModelAndView("/progressOfBid/bidEvaluation/bidEvalBase");

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        BidProtype bidProtype = BidProtype.getBidProtypeByCode(bidSection.getBidClassifyCode());
        boolean isOtherEval = false;
        if (BidProtype.CONSTRUCTION.equals(bidProtype)) {
            TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
            if (Enabled.YES.getCode().equals(tenderDoc.getMutualSecurityStatus())) {
                isOtherEval = true;
            }
        }
        List<EvalProcessGov> processList = EvalProcessGov.listEvalProcessGovByBp(bidProtype, isOtherEval);
        //判断流程进行情况
        Map<String, Integer> processStatus = bidEvalService.listProcessStatus(bidSectionId);

        mav.addObject("bidSection", bidSection);
        mav.addObject("processList", processList);
        mav.addObject("processStatus", processStatus);
        return mav;
    }

    @RequestMapping("linkInProgressPage")
    public ModelAndView linkInProgressPage() {
        return new ModelAndView("/progressOfBid/bidEvaluation/linkInProgress");
    }

    /**
     * 跳转评标组长推选页面
     *
     * @return 跳转评标组长推选页面
     */
    @RequestMapping("/selectLeaderPage")
    public ModelAndView selectLeaderPage() {
        ModelAndView mav = new ModelAndView("/progressOfBid/bidEvaluation/selectLeader");

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        List<ExpertUser> experts = expertUserService.getExpertsByBidSectionId(bidSectionId);
        ExpertUser chairman = expertUserService.getChairmanByBidSectionId(bidSectionId);
        mav.addObject("experts", experts);
        mav.addObject("chairman", chairman);
        return mav;
    }

    /**
     * 跳转资格审查页面
     *
     * @return 跳转资格审查页面
     */
    @RequestMapping("/qualificationPage")
    public ModelAndView qualificationPage() {
        ModelAndView mav = new ModelAndView("/progressOfBid/bidEvaluation/qualification");

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");

        // 获取进入开标的投标人
        List<Bidder> bidders = bidderService.listPassBidOpenBidder(bidSectionId);
        // 获取资格审查的评标办法
        Grade grade = gradeService.listGrade(gradeIds, EvalProcess.QUALIFICATION.getCode()).get(0);
        //评标专家
        List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);

        mav.addObject("bidders", bidders);
        mav.addObject("expertUsers", expertUsers);
        mav.addObject("grade", grade);
        mav.addObject("grade", grade);
        return mav;
    }

    /**
     * 获取资格审查 的 投标人小组结果汇总数据
     *
     * @param gradeId gradeId
     * @return 投标人小组结果数据
     */
    @RequestMapping("/getQuaBiddersGradeResult")
    public List<BidderReviewResult> getQuaBiddersGradeResult(Integer gradeId) {
        return bidEvalService.getQualifyBiddersGradeResult(gradeId);
    }

    /**
     * 获取合格制 的 小组投标人结果详细数据
     *
     * @param bidderId 投标人ID
     * @param gradeId  gradeId
     * @return 投标人小组结果数据
     */
    @RequestMapping("/getBidderQualifiedData")
    public Map<String, Object> getBidderQualifiedData(Integer bidderId, Integer gradeId) {
        return bidEvalService.getBidderQualifiedData(bidderId, gradeId);
    }

    /**
     * 跳转初步评审页面
     *
     * @return 跳转初步评审页面
     */
    @RequestMapping("/preliminaryPage")
    public ModelAndView preliminaryPage() {
        ModelAndView mav = new ModelAndView("/progressOfBid/bidEvaluation/preliminary");

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");

        List<Bidder> bidders;
        if (BidProtype.EPC.getCode().equals(bidSection.getBidClassifyCode())) {
            //施工总承包 获取通过资格审查的投标人
            bidders = bidderService.listBidderPassQualifyReview(bidSectionId);
        } else {
            //获取通过开标的投标人
            bidders = bidderService.listPassBidOpenBidder(bidSectionId);
        }
        // 获取初步评审的评标办法
        Grade grade = gradeService.listGrade(gradeIds, EvalProcess.PRELIMINARY.getCode()).get(0);
        //评标专家
        List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);

        mav.addObject("bidders", bidders);
        mav.addObject("expertUsers", expertUsers);
        mav.addObject("grade", grade);
        return mav;
    }

    /**
     * 初步评审 投标人小组结果数据
     *
     * @param gradeId gradeId
     * @return 投标人小组结果数据
     */
    @RequestMapping("/getPreBiddersGradeResult")
    public List<BidderReviewResult> getPreBiddersGradeResult(Integer gradeId) {
        return bidEvalService.getPreBiddersGradeResult(gradeId);
    }

    /**
     * 跳转详细评审页面
     *
     * @return 跳转初步评审页面
     */
    @RequestMapping("/detailedPage")
    public ModelAndView detailedPage() {
        ModelAndView mav = new ModelAndView();

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        BidProtype bidProtype = BidProtype.getBidProtypeByCode(bidSection.getBidClassifyCode());

        switch (bidProtype) {
            case CONSTRUCTION:
                // 施工详细评审页面
                mav.setViewName("/progressOfBid/bidEvaluation/detailed/conDetailed");
                break;
            case SUPERVISION:
                // 监理详细评审页面
                mav.setViewName("/progressOfBid/bidEvaluation/detailed/supDetailed");
                List<ReviewType> reviewTypes = ReviewType.listSupervisionType();
                mav.addObject("reviewTypes", reviewTypes);
                break;
            case QUALIFICATION:
                // 资格预审详细评审页面
                mav.setViewName("/progressOfBid/bidEvaluation/detailed/quaDetailed");
                // 获取进入详细评审的投标人(包括专家打分完成情况)
                List<Bidder> quaBidders = bidderService.listDetailedBidder(bidSectionId);
                //评标专家
                List<ExpertUser> quaExpertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);
                mav.addObject("bidders", quaBidders);
                mav.addObject("expertUsers", quaExpertUsers);
                break;
            case INVESTIGATION:
            case ELEVATOR:
            case DESIGN:
                // 进入勘察、设计、电梯详细评审页面
                mav.setViewName("/progressOfBid/bidEvaluation/detailed/candidatesDetailed");
                List<Bidder> canBidders = bidEvalService.listCanBidderVotes(bidSectionId);
                mav.addObject("bidders", canBidders);
                break;
            case EPC:
                // 施工总承包
                mav.setViewName("/progressOfBid/bidEvaluation/detailed/epcDetailed");
                // 获取进入详细评审的投标人
                List<Bidder> epcBidders = bidderService.listDetailedBidder(bidSectionId);
                List<ExpertUser> epcExpertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);
                mav.addObject("bidders", epcBidders);
                mav.addObject("expertUsers", epcExpertUsers);
                break;
            default:
        }

        // 获取详细评审当前评审类型的评标办法
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());
        mav.addObject("grades", grades);
        return mav;
    }

    /**
     * 施工 详细评审 小组汇总 grade对应页面
     *
     * @param gradeId 评审id
     * @return 小组汇总 grade对应页面
     */
    @RequestMapping("/conGradeDetailedPage")
    public ModelAndView conGradeDetailedPage(Integer gradeId) {
        ModelAndView mav = new ModelAndView("/progressOfBid/bidEvaluation/detailed/conGradeDetailed");

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        Grade grade = gradeService.getGradeById(gradeId);
        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);
        List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);

        //判断当前grade是否为安全质量事故扣分或建筑市场不良记录扣分
        Boolean isConGrade = grade.getName().equals(ConDetailedMethod.BAD_RECORD_MARKET.getName()) || grade.getName().equals(ConDetailedMethod.SAFETY_QUALITY_ACCIDENT.getName());

        mav.addObject("bidders", bidders);
        mav.addObject("expertUsers", expertUsers);
        mav.addObject("grade", grade);
        mav.addObject("isConGrade", isConGrade);
        return mav;
    }

    /**
     * 施工 详细评审 获取每个投标人当前grade的最后结果
     *
     * @param gradeId gradeId
     * @return 获取每个投标人当前grade的最后结果
     */
    @RequestMapping("/getConDetailedGroupBiddersResult")
    public List<BidderReviewResultDeduct> getConDetailedGroupBiddersResult(Integer gradeId) {
        return bidEvalService.getConDetailedGroupBiddersResult(gradeId);
    }

    /**
     * 施工 详细评审 获取当前专家的结果数据
     *
     * @param bidderId 投标人ID
     * @param gradeId  gradeId
     */
    @RequestMapping("/getBidderDataForResult")
    public Map<String, Object> getConDetailedGroupBidderResult(Integer bidderId, Integer gradeId) {
        return bidEvalService.getConDetailedGroupBidderResult(bidderId, gradeId);
    }

    /**
     * 监理 详细评审 小组汇总 详细结果页面
     *
     * @param reviewType 评审类型
     * @return 详细结果页面
     */
    @RequestMapping("/supReviewDetailedPage")
    public ModelAndView supReviewDetailedPage(Integer reviewType) {
        ModelAndView mav = new ModelAndView("/progressOfBid/bidEvaluation/detailed/supReviewDetailed");

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");

        // 获取进入详细评审的投标人(包括专家打分完成情况)
        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);
        List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);
        // 获取详细评审当前评审类型的评标办法
        List<Grade> grades = gradeService.listGradeByReviewType(gradeIds, EvalProcess.DETAILED.getCode(), reviewType);

        mav.addObject("bidders", bidders);
        mav.addObject("expertUsers", expertUsers);
        mav.addObject("grades", grades);
        mav.addObject("reviewType", reviewType);
        return mav;
    }

    /**
     * 监理 详细评审  获取每个投标人当前reviewType的最后结果
     *
     * @param reviewType reviewType
     * @return 获取每个投标人当前reviewType的最后结果
     */
    @RequestMapping("/getSupDetailedGroupBiddersResult")
    public List<BidderResultDTO> getSupDetailedGroupBiddersResult(Integer reviewType) {
        return bidEvalService.getSupDetailedGroupBiddersResult(reviewType);
    }


    /**
     * 监理 详细评审  获取当前投标人的结果数据
     *
     * @param bidderId   投标人ID
     * @param reviewType 评审类型
     */
    @RequestMapping("/getSupDetailedGroupBidderResult")
    public Map<String, Object> getSupDetailedGroupBidderResult(Integer bidderId, Integer reviewType) {
        return bidEvalService.getSupDetailedGroupBidderResult(bidderId, reviewType);
    }

    /**
     * EPC 详细评审 小组汇总 所有投标人 结果数据
     *
     * @return 小组汇总 所有投标人 结果数据
     */
    @RequestMapping("/getEpcDetailedGroupBiddersResult")
    public List<BidderResultDTO> getEpcDetailedGroupBiddersResult() {
        return bidEvalService.getEpcDetailedGroupBiddersResult();
    }

    /**
     * EPC 详细评审 小组汇总 当前投标人 结果数据
     *
     * @param bidderId 投标人ID
     * @return 小组汇总 当前投标人 结果数据
     */
    @RequestMapping("/getEpcDetailedGroupBidderResult")
    public Map<String, Object> getEpcDetailedGroupBidderResult(Integer bidderId) {
        return bidEvalService.getEpcDetailedGroupBidderResult(bidderId);
    }

    /**
     * 跳转投标报价页面
     *
     * @return 跳转投标报价页面
     */
    @RequestMapping("/calcPriceScorePage")
    public ModelAndView calcPriceScorePage() {
        ModelAndView mav = new ModelAndView();

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        BidProtype bidProtype = BidProtype.getBidProtypeByCode(bidSection.getBidClassifyCode());

        switch (bidProtype) {
            case CONSTRUCTION:
                // 施工
                BidSectionRelate bidSectionRelate = bidSectionRelateService.getBidSectionRelateByBSId(bidSectionId);
                mav.addObject("bidSection", bidSection);
                mav.addObject("bidSectionRelate", bidSectionRelate);

                List<EvalResultSg> list = bidEvalService.listConRankingBidder(bidSectionId);
                TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
                mav.addObject("list", list);
                mav.addObject("tenderDoc", tenderDoc);

                mav.setViewName("/progressOfBid/bidEvaluation/calcPriceScore/conCalcPriceScore");
                break;
            case EPC:
                // 施工总承包
                mav.setViewName("/progressOfBid/bidEvaluation/calcPriceScore/epcCalcPriceScore");
                List<Bidder> epcBidders;
                Integer isUpdate = Enabled.NO.getCode();
                if (!Enabled.YES.getCode().equals(bidSection.getUpdateScoreStatus())) {
                    epcBidders = bidEvalService.listEpcBidderQuoteScore(bidSectionId);
                } else {
                    isUpdate = Enabled.YES.getCode();
                    epcBidders = bidEvalService.listEpcBidderQuoteAppendixScore(bidSectionId);
                }
                CalcScoreParam calcScoreParam = calcScoreParamService.getCalcScoreParamBySectionId(bidSectionId);
                mav.addObject("isUpdate", isUpdate);
                mav.addObject("bidders", epcBidders);
                mav.addObject("calcScoreParam", calcScoreParam);
                break;
            default:
        }
        return mav;
    }

    /**
     * 跳转评审结果页面
     *
     * @return 跳转评审结果页面
     */
    @RequestMapping("/resultPage")
    public ModelAndView resultPage() {
        ModelAndView mav = new ModelAndView();

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        BidProtype bidProtype = BidProtype.getBidProtypeByCode(bidSection.getBidClassifyCode());

        switch (bidProtype) {
            case CONSTRUCTION:
                // 施工
                mav.setViewName("/progressOfBid/bidEvaluation/result/conResult");
                List<EvalResultSg> conData = evalResultSgService.listRankingBidderByBsId(bidSectionId);
                TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
                mav.addObject("data", conData);
                mav.addObject("tenderDoc", tenderDoc);
                break;
            case EPC:
                // 施工总承包
                mav.setViewName("/progressOfBid/bidEvaluation/result/epcResult");
                List<EvalResultEpc> epcData = evalResultEpcService.listRankingBidderByBsId(bidSectionId);
                mav.addObject("data", epcData);
                break;
            case SUPERVISION:
                // 监理详细评审页面
                mav.setViewName("/progressOfBid/bidEvaluation/result/supResult");
                List<EvalResultJl> supData = evalResultJlService.listRankingBidderByBsId(bidSectionId);
                mav.addObject("data", supData);
                break;
            case QUALIFICATION:
                // 资格预审详细评审页面
                mav.setViewName("/progressOfBid/bidEvaluation/result/quaResult");
                List<Bidder> quaBidders = bidEvalService.listQuaResultBidder(bidSectionId);
                mav.addObject("bidders", quaBidders);
                break;
            case INVESTIGATION:
            case ELEVATOR:
            case DESIGN:
                // 进入勘察、设计、电梯详细评审页面
                mav.setViewName("/progressOfBid/bidEvaluation/result/candidateResult");
                List<Bidder> canBidder = bidderService.listDetailedBidder(bidSectionId);
                mav.addObject("bidders", canBidder);
                break;
            default:
        }
        return mav;
    }

    /**
     * 评审结果为推荐投标人类型 推荐投标人流程 获取推荐排名
     *
     * @return 推荐投标人类型 详细评审 获取推荐排名
     */
    @RequestMapping("/getCanCandidatesResult")
    public List<CandidateSuccess> getCanCandidatesResult() {
        return bidEvalService.getCanCandidatesResult();
    }

    /**
     * Epc独有 跳转推荐候选人页面
     *
     * @return 跳转推荐候选人页面
     */
    @RequestMapping("/candidatesPage")
    public ModelAndView candidatesPage() {
        ModelAndView mav = new ModelAndView("/progressOfBid/bidEvaluation/candidates");

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        List<EvalResultEpc> epcData = evalResultEpcService.listRankingBidderByBsId(bidSectionId);
        mav.addObject("data", epcData);
        return mav;
    }

    /**
     * 跳转清标结果页面 todo
     *
     * @return 跳转清标结果页面
     */
    @RequestMapping("/clearingResultsPage")
    public ModelAndView clearingResultsPage() {
        ModelAndView mav = new ModelAndView("/progressOfBid/bidEvaluation/clearingResults/clearingResults");

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        BidSectionRelate bidSectionRelate = bidSectionRelateService.getBidSectionRelateByBSId(bidSectionId);

        mav.addObject("tenderDoc", tenderDoc);
        mav.addObject("bidSection", bidSection);
        mav.addObject("expert", user);
        mav.addObject("menu", false);
        mav.addObject("bidSectionRelate", bidSectionRelate);

        return mav;
    }

    /**
     * 跳转清标分析结果页面
     *
     * @return
     */
    @RequestMapping("/clearResultAnalysisPage")
    public ModelAndView clearResultAnalysisPage() {
        ModelAndView modelAndView = new ModelAndView("/progressOfBid/bidEvaluation/clearingResults/resultPage");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        BidSectionRelate bidSectionRelate = bidSectionRelateService.getBidSectionRelateByBSId(bidSectionId);

        modelAndView.addObject("bidSectionRelate", bidSectionRelate);
        modelAndView.addObject("tenderDoc", tenderDoc);
        return modelAndView;
    }

    /**
     * 跳转控制价分析页面
     *
     * @return
     */
    @RequestMapping("/controlPriceAnalysisPage")
    public ModelAndView controlPriceAnalysisPage() {
        ModelAndView modelAndView = new ModelAndView("/progressOfBid/bidEvaluation/clearingResults/controlPriceAnalysisPage");
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
     * 跳转其他评审页面
     *
     * @return 跳转其他评审页面
     */
    @RequestMapping("/otherPage")
    public ModelAndView otherPage() {
        ModelAndView mav = new ModelAndView();

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        if (BidProtype.CONSTRUCTION.getCode().equals(bidSection.getBidClassifyCode()) && Enabled.YES.getCode().equals(tenderDoc.getMutualSecurityStatus())) {
            mav.setViewName("redirect:/gov/bidEval/mutualEvalPage");
        } else {
            mav.addObject("/gov/bidEval/linkInProgressPage");
        }
        return mav;
    }

    /**
     * 其他评审 互保共建页面
     *
     * @return 互保共建页面
     */
    @RequestMapping("/mutualEvalPage")
    public ModelAndView mutualEvalPage() {
        ModelAndView mav = new ModelAndView("/progressOfBid/bidEvaluation/other/mutualEval");

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getOtherGradeId().split(",");

        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);

        // 获取初步评审的评标办法
        Grade grade = gradeService.listGrade(gradeIds, EvalProcess.OTHER.getCode()).get(0);
        //评标专家
        List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);

        mav.addObject("bidders", bidders);
        mav.addObject("expertUsers", expertUsers);
        mav.addObject("grade", grade);
        return mav;
    }

    /**
     * 互保共建 获取所有投标人的结果数据
     *
     * @param gradeId gradeId
     * @return 获取所有投标人的结果数据
     */
    @RequestMapping("/getGroupMutualResultData")
    public List<BidderResultDTO> getGroupMutualResultData(Integer gradeId) {
        return bidEvalService.getGroupMutualResultData(gradeId);
    }

    /**
     * 互保共建 获取专家的结果数据
     *
     * @param bidderId 投标人ID
     * @param gradeId  gradeId
     */
    @RequestMapping("/getBidderMutualResultData")
    public Map<String, Object> getBidderMutualResultData(Integer bidderId, Integer gradeId) {
        return bidEvalService.getBidderMutualResultData(bidderId, gradeId);
    }

    /**
     * 打开项目暂停页面
     *
     * @return
     */
    @RequestMapping("/pauseProjectPage")
    public ModelAndView pauseProjectPage() {
        ModelAndView mav = new ModelAndView("/progressOfBid/projectManage/pauseToContinue");
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        ProjectPause projectPause = projectPauseService.getProjectPauseByBidSectionId(bidSectionId);
        TenderProject tenderProject = tenderProjectService.getTenderProjectById(bidSection.getTenderProjectId());
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        mav.addObject("tenderProject", tenderProject);
        mav.addObject("bidSection", bidSection);
        mav.addObject("projectPause", projectPause);
        mav.addObject("tenderDoc", tenderDoc);
        return mav;
    }

    /**
     * 项目暂停、继续
     *
     * @param bidSectionId 标段主键
     * @param pauseStatus  暂停或继续
     * @param pauseReason  暂停理由
     * @return
     */
    @RequestMapping("/pauseProject")
    @UserLog(value = "'修改项目进度信息(项目暂停、继续): bidSectionId='+#bidSectionId+',pauseStatus='+#pauseStatus+',pauseReason='+#pauseReason", dmlType = DMLType.UPDATE)
    public Boolean pauseProject(Integer bidSectionId, Integer pauseStatus, String pauseReason) {
        AuthUser user = CurrentUserHolder.getUser();
        ProjectPause projectPause = projectPauseService.getProjectPauseByBidSectionId(bidSectionId);
        ProjectPause build = ProjectPause.builder()
                .bidSectionId(bidSectionId)
                .operatorId(user.getUserId())
                .operatorName(user.getUsername())
                .pauseStatus(pauseStatus)
                .pauseReason(pauseReason)
                .build();
        if (projectPause != null) {
            build.setId(projectPause.getId());
            return projectPauseService.updateById(build);
        } else {
            return projectPauseService.save(build);
        }
    }

    /**
     * 项目回退
     *
     * @return
     */
    @RequestMapping("/fallbackAuditPage")
    public ModelAndView fallbackAuditPage() {
        ModelAndView mav = new ModelAndView("/progressOfBid/projectManage/fallbackAudit");
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        ProjectPause projectPause = projectPauseService.getProjectPauseByBidSectionId(bidSectionId);
        TenderProject tenderProject = tenderProjectService.getTenderProjectById(bidSection.getTenderProjectId());
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        // 待审核回退申请
        FreeBackApply waiteFreeBackApply = freeBackApplyService.getFreeBackAppliesBeautify(freeBackApplyService.getFreeBackApplyByBidSectionId(bidSectionId));
        // 审核历史记录列表
        List<FreeBackApply> listFreeBackApply = freeBackApplyService.listFreeBackApplyBeautify(freeBackApplyService.listFreeBackApply(bidSectionId));
        mav.addObject("tenderProject", tenderProject);
        mav.addObject("bidSection", bidSection);
        mav.addObject("projectPause", projectPause);
        mav.addObject("tenderDoc", tenderDoc);
        mav.addObject("waiteFreeBackApply", waiteFreeBackApply);
        mav.addObject("listFreeBackApply", listFreeBackApply);
        return mav;
    }

    /**
     * 回退审核
     *
     * @param id          回退审核表主键
     * @param checkStatus 回退审核状态
     * @return
     */
    @RequestMapping("/updateBackApply")
    @UserLog(value = "'修改回退审核状态: id='+#id+', checkStatus='+#checkStatus", dmlType = DMLType.UPDATE)
    public Boolean updateBackApply(Integer id, String checkStatus) throws Exception {
        AuthUser user = CurrentUserHolder.getUser();
        try {
            RedissonUtil.lock(CacheName.ROLLBACK_APPLY_LOCK + id, 10 * 60);
            long start = System.currentTimeMillis();
            FreeBackApply backApply = freeBackApplyService.getFreeBackApplyById(id);
            // 已操作过的
            if (!EvalStatus.UNSTART.toString().equals(backApply.getCheckStatus())) {
                // 用户重复点击
                this.setSystemActionMessage("后台处理中，请勿重复点击!", WebSocketMessage.Dialog.MSG, WebSocketMessage.Type.ERROR);
                return false;
            }
            //更新状态不允许为空
            if (CommonUtil.isEmpty(checkStatus)) {
                return false;
            }
            // 审核驳回
            if (BackStatus.NOPASS.toString().equals(checkStatus)) {
                //初始化推送信息
                BackPushStatus backPushStatus = BackPushStatus.builder()
                        .bidSectionId(user.getBidSectionId())
                        .pushResult(0)
                        .backId(id)
                        .build();
                backPushStatusService.initBackPush(backPushStatus);
                bidEvalService.updateBackApplyById(id, checkStatus);
                return true;
            }
            // 审核通过
            assert user != null;
            // 清空回退合成状态
            RedisUtil.delete(CacheName.ROLL_BACK_NOW_EXPERT + user.getUserId());
            // 生成回退前专家个人评审报告
            boolean status = bidEvalService.generateBackBeforeEvaluationData(backApply);
            // 回退数据生成失败
            if (!status){
                this.setSystemActionMessage("回退评审报告失败!", WebSocketMessage.Dialog.MSG, WebSocketMessage.Type.ERROR);
            }
            // 更新状态、删除评审结果
            boolean result = bidEvalService.updateBackApply(id, checkStatus);
            // 删除评标报告
            bidSectionService.updateBidSectionById(BidSection.builder()
                    .id(user.getBidSectionId())
                    .evalPdfGenerateStatus(0)
                    .build());
            if (result) {
                // (还原报价得分计算状态）
                BidSection bidSection = bidSectionService.getBidSectionById(backApply.getBidSectionId());
                bidSectionService.updateBidSectionById(BidSection.builder()
                        .id(bidSection.getId())
                        .priceRecordStatus(Status.NOT_START.getCode())
                        .build());
                long end = System.currentTimeMillis();
                // 数据生成、删除成功后，修改回退状态
                return bidEvalService.updateBackApplyById(id, checkStatus);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            RedissonUtil.unlock(CacheName.ROLLBACK_APPLY_LOCK + id);
        }
        return false;
    }

    /**
     * 查询当前回退数据生成状态
     *
     * @return
     */
    @RequestMapping("nowBackPdf")
    public Map<String, Object> currentConversionPDF() {
        AuthUser user = CurrentUserHolder.getUser();
        Map<String, Object> map = new HashMap<>();
        Object currentFallback = RedisUtil.get(CacheName.ROLL_BACK_NOW_EXPERT + user.getUserId());
        map.put("currentBack", currentFallback);
        return map;
    }

    /**
     * 项目复议
     *
     * @return
     */
    @RequestMapping("/reEvalPage")
    public ModelAndView reEvalPage() {
        ModelAndView mav = new ModelAndView("/progressOfBid/projectManage/reEval");
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderProject tenderProject = tenderProjectService.getTenderProjectById(bidSection.getTenderProjectId());
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        List<ReevalLog> reevalLogs = reevalLogService.listLogByBidSectionId(bidSectionId);

        mav.addObject("tenderProject", tenderProject);
        mav.addObject("bidSection", bidSection);
        mav.addObject("tenderDoc", tenderDoc);
        mav.addObject("reevalLogs", reevalLogs);
        return mav;
    }

    /**
     * 复议开启
     * @param reevalLog
     * @return
     */
    @RequestMapping("/reEvalThis")
    public Boolean reEvalThis(ReevalLog reevalLog){
        return bidEvalService.reEvalThis(reevalLog);
    }
}
