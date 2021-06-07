package com.ejiaoyi.expert.controller;

import com.ejiaoyi.common.constant.CacheName;
import com.ejiaoyi.common.constant.EvalStatus;
import com.ejiaoyi.common.constant.ScoreType;
import com.ejiaoyi.common.dto.BidderResultDTO;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.dto.WebSocketMessage;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.Enabled;
import com.ejiaoyi.common.enums.EvalProcess;
import com.ejiaoyi.common.enums.Status;
import com.ejiaoyi.common.exception.CustomException;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.RedisUtil;
import com.ejiaoyi.common.util.RedissonUtil;
import com.ejiaoyi.expert.service.IBidEvalService;
import com.ejiaoyi.expert.service.IEpcBidEvalService;
import com.ejiaoyi.expert.service.IExpertService;
import com.ejiaoyi.expert.support.AuthUser;
import com.ejiaoyi.expert.support.CurrentUserHolder;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 施工总承包 专家评标控制器
 *
 * @author Make
 * @since 2020/12/01
 */
@RestController
@RequestMapping("/expert/epcBidEval")
public class EpcBidEvalController extends BaseController {

    @Autowired
    IBidSectionService bidSectionService;

    @Autowired
    private ITenderDocService tenderDocService;

    @Autowired
    private IGradeService gradeService;

    @Autowired
    private IBidEvalService bidEvalService;

    @Autowired
    private IEpcBidEvalService epcBidEvalService;

    @Autowired
    private ICalcScoreParamService calcScoreParamService;

    @Autowired
    private IBidderService bidderService;

    @Autowired
    private IExpertReviewService expertReviewService;

    @Autowired
    private IExpertReviewSingleItemService expertReviewSingleItemService;

    @Autowired
    private IExpertUserService expertUserService;

    @Autowired
    private IExpertService expertService;

    @Autowired
    private IExpertReviewSingleItemScoreService expertReviewSingleItemScoreService;

    @Autowired
    private IQuoteScoreResultAppendixService quoteScoreResultAppendixService;

    @Autowired
    private IFDFSService fdfsService;

    @Autowired
    ISignatureService signatureService;

    /**
     * 资格审查页面
     *
     * @return 资格审查页面
     */
    @RequestMapping("/qualifyReviewPage")
    public ModelAndView qualifyReviewPage() {
        ModelAndView mav = new ModelAndView();
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");

        if (!(bidSection.getBidOpenStatus() != null && Status.END.getCode().equals(bidSection.getBidOpenStatus()))) {
            //判断为当前开标未结束
            mav.setViewName("redirect:/expert/startEval");
            this.setSystemActionMessage("当前环节还未开始！", WebSocketMessage.Dialog.MSG, WebSocketMessage.Type.ERROR);
            return mav;
        }


        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.QUALIFICATION.getCode());

        mav.setViewName("/epc/qualifyReview/qualifyReviewIndex");
        mav.addObject("grades", grades);
        mav.addObject("expert", user);
        mav.addObject("bidSection", bidSection);
        return mav;
    }

    @RequestMapping("/qualifyReviewContentPage")
    public ModelAndView qualifyReviewContentPage(Integer gradeId) {
        ModelAndView mav = new ModelAndView("/epc/qualifyReview/qualifyReviewContent");

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);

        Grade grade = gradeService.getGrade(gradeId, EvalProcess.QUALIFICATION.getCode());
        // 获取通过开标的投标人
        List<Bidder> bidders = bidderService.listPassBidOpenBidder(bidSectionId);

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
     * 资格审查 个人评审结束校验（是否符合个人评审条件）
     *
     * @return 个人评审结束校验
     */
    @RequestMapping("/validQualifiedEnd")
    public JsonData validQualifiedEnd() {
        return bidEvalService.validQualifyReviewEnd();
    }

    /**
     * 资格审查 校验是否有不合格项评审
     *
     * @return 校验是否有不合格项评审
     */
    @RequestMapping("/validUnqualified")
    public boolean validUnqualified() {
        List<ExpertReviewSingleItem> items = bidEvalService.listUnQualifyReviewItem();
        return CollectionUtils.isEmpty(items);
    }

    /**
     * 资格审查 个人评审结束
     *
     * @return 个人评审结束
     */
    @RequestMapping("/personalReviewEnd")
    public boolean personalReviewEnd() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        return bidEvalService.updateReviewEnd(bidSectionId, EvalProcess.QUALIFICATION.getCode());
    }

    /**
     * 资格审查 跳转到不合格项意见填写页面
     *
     * @return 资格审查 跳转到不合格项意见填写页面
     */
    @RequestMapping("/jumpOpinionPage")
    public ModelAndView jumpOpinionPage() {
        ModelAndView mav = new ModelAndView("/epc/qualifyReview/opinionPage");

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        List<Bidder> bidders = bidEvalService.listExpertOpinion(bidSectionId, EvalProcess.QUALIFICATION.getCode());
        mav.addObject("bidders", bidders);
        return mav;
    }

    /**
     * 资格审查 环节重新评审
     */
    @RequestMapping("/callQualifyPersonReview")
    public void callQualifyPersonReview() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        bidEvalService.callPersonReview(user.getBidSectionId(), EvalProcess.QUALIFICATION.getCode());
    }

    /**
     * 资格审查 验证评标委员会评审结束的相关条件
     *
     * @return 资格审查  验证评标委员会评审结束的相关条件
     */
    @RequestMapping("/validQualifyGroupReview")
    public JsonData validQualifyGroupReview() {
        return bidEvalService.checkQualifyGroupEnd();
    }

    /**
     * 资格审查 评标委员会评审结束
     *
     * @return 资格审查 评标委员会评审结束
     */
    @RequestMapping("/endQualifyGroupReview")
    public boolean endQualifyGroupReview() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        try {
            bidEvalService.updateGroupReviewEndStatus(bidSectionId, EvalProcess.QUALIFICATION.getCode());
            return true;
        } catch (Exception e) {
            e.getStackTrace();
            return false;
        }
    }


    /**
     * 资格审查 专家评审汇总框架页面
     *
     * @return 资格审查 专家评审汇总框架页面
     */
    @RequestMapping("/qualifiedEvalResultPage")
    public ModelAndView qualifiedEvalResultPage() {
        return new ModelAndView("/epc/qualifyReview/result/evaluationResultPage", "expert", CurrentUserHolder.getUser());
    }

    /**
     * 资格审查 展示专家个人评审结果框架
     *
     * @return 资格审查 展示专家个人评审结果框架
     */
    @RequestMapping("/showPersonReviewResultPage")
    public ModelAndView showPersonReviewResultPage() {
        ModelAndView mav = new ModelAndView("/epc/qualifyReview/result/showPersonReviewResultPage");

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");

        // 获取进入开标的投标人
        List<Bidder> bidders = bidderService.listPassBidOpenBidder(bidSectionId);
        // 获取资格审查的评标办法
        Grade grade = gradeService.listGrade(gradeIds, EvalProcess.QUALIFICATION.getCode()).get(0);
        //是否个人评审结束

        ExpertReview expertReview = expertReviewSingleItemService.initExpertSingleItem(bidSectionId, user.getUserId(), grade, bidders);

        mav.addObject("bidders", bidders);
        mav.addObject("expertReview", expertReview);
        mav.addObject("grade", grade);
        return mav;
    }


    /**
     * 资格审查 小组评审汇总
     *
     * @return 小组评审汇总
     */
    @RequestMapping("/showGroupReviewResultPage")
    public ModelAndView showGroupReviewResultPage() {
        ModelAndView mav = new ModelAndView("/epc/qualifyReview/result/showGroupReviewResultPage");

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
     * 获取资格审查 的 投标人小组结果汇总数据
     *
     * @param gradeId gradeId
     * @return 投标人小组结果数据
     */
    @RequestMapping("/getBiddersGradeResult")
    public List<BidderReviewResult> getBiddersGradeResult(Integer gradeId) {
        return bidEvalService.getQualifyBiddersGradeResult(gradeId);
    }

    /**
     * 获取资格审查 的 小组投标人结果详细数据
     *
     * @param bidderId       投标人ID
     * @param gradeId        gradeId
     * @param isAllExpertEnd 是否所有专家都个人评审结束
     * @return 投标人小组结果数据
     */
    @RequestMapping("/getBidderDataForResult")
    public Map<String, Object> getBidderDataForResult(Integer bidderId, Integer gradeId, Integer isAllExpertEnd) {
        return bidEvalService.getPreBidderDataForResult(bidderId, gradeId, isAllExpertEnd);
    }

    /**
     * 详细评审加载投标人
     *
     * @return 详细评审加载投标人
     */
    @RequestMapping("/loadDetailedGradeDetailedPage")
    public ModelAndView loadDetailedGradeDetailedPage() {
        ModelAndView mav = new ModelAndView("/epc/detailed/loadDetailedGradeDetailed");

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        //筛选 参加详细评审的投标人 筛选
        List<Bidder> bidders = bidEvalService.listBidderPassPreliminary(bidSectionId);

        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());

        //初始化打分项
        ExpertReview expertReview = expertReviewSingleItemScoreService.initReviewItemScore(bidSectionId, user.getUserId(), grades, bidders);

        //判断对应投标人对当前评审类型的评审方法是否评审完成当前评审类型是否评审完毕
        bidders = bidEvalService.listBidderEpcGradesIsEnd(bidders, grades);

        mav.addObject("bidders", bidders);
        mav.addObject("grades", grades);
        mav.addObject("expertReview", expertReview);
        mav.addObject("expert", user);
        mav.addObject("tenderDoc", tenderDoc);

        return mav;
    }

    /**
     * 获取投标人打分数据
     *
     * @param bidderId 投标人Id
     * @return 获取投标人打分数据
     */
    @RequestMapping("/getEpcDetailedBidderData")
    public Map<String, Object> getEpcDetailedBidderData(Integer bidderId) {
        return bidEvalService.getEpcDetailedBidderData(bidderId);
    }

    /**
     * 保存单项打分数据
     *
     * @param id    单项ID
     * @param score 大分分数
     * @return 保存结果
     */
    @RequestMapping("/saveDetailedResult")
    public JsonData saveDetailedResult(Integer id, String score) {
        return bidEvalService.saveEpcDetailedResult(id, score);
    }

    /**
     * 检查当前投标人当前评审类型是否打分完成
     *
     * @param bidderId 投标人Id
     * @return 检查当前投标人当前评审类型是否打分完成
     */
    @RequestMapping("/validBidderScoreCompletion")
    public Boolean validBidderScoreCompletion(Integer bidderId) {
        return bidEvalService.validEpcDetailedBidderCompletion(bidderId);
    }

    /**
     * 检查是否可以个人评审结束
     *
     * @return 检查是否可以个人评审结束
     */
    @RequestMapping("/checkEpcDetailedPersonalEnd")
    public JsonData checkEpcDetailedPersonalEnd() {
        return bidEvalService.checkEpcDetailedPersonalEnd();
    }

    /**
     * 个人评审结束
     *
     * @return 个人评审结束
     */
    @RequestMapping("/epcDetailedPersonalEnd")
    public Boolean epcDetailedPersonalEnd() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        return bidEvalService.updateReviewEnd(user.getBidSectionId(), EvalProcess.DETAILED.getCode());
    }

    /**
     * 环节重评
     */
    @RequestMapping("/epcDetailedRestartReview")
    public void epcDetailedRestartReview() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        bidEvalService.callPersonReview(user.getBidSectionId(), EvalProcess.DETAILED.getCode());
    }


    /**
     * 详细评审 小组评审结束检查
     *
     * @return 小组评审结束检查
     */
    @RequestMapping("/checkEpcDetailedGroupEnd")
    public JsonData checkSupGroupEnd() {
        return bidEvalService.checkEpcDetailedGroupEnd();
    }

    /**
     * 详细评审 小组评审结束
     *
     * @return 小组评审结束
     */
    @RequestMapping("/endEpcDetailedGroupEnd")
    public Boolean endEpcDetailedGroupEnd() {
        return bidEvalService.endEpcDetailedGroupEnd();
    }

    /**
     * 详细评审 评审结果总框架
     *
     * @return 专家评审汇总框架页面
     */
    @RequestMapping("/epcDetailedEvalResultIndex")
    public ModelAndView epcDetailedEvalResultIndex() {
        ModelAndView mav = new ModelAndView("/epc/detailed/result/evalResultDetailedIndex");
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        mav.addObject("bidSection", bidSection);
        mav.addObject("expert", user);
        return mav;
    }

    /**
     * 详细评审 展示专家个人评审结果
     *
     * @return 详细评审 展示专家个人评审结果
     */
    @RequestMapping("/epcDetailedPersonContent")
    public ModelAndView detailedPersonPage() {
        ModelAndView mav = new ModelAndView("/epc/detailed/result/epcDetailedPersonContent");
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");

        // 获取进入详细评审的投标人(包括专家打分完成情况)
        List<Bidder> bidders = bidEvalService.listBidderPassPreliminary(bidSectionId);

        // 获取详细评审当前评审类型的评标办法
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());

        //是否个人评审结束
        ExpertReview expertReview = expertReviewSingleItemScoreService.initReviewItemScore(bidSectionId, user.getUserId(), grades, bidders);

        mav.addObject("bidders", bidders);
        mav.addObject("grades", grades);
        mav.addObject("expertReview", expertReview);
        return mav;
    }

    /**
     * 详细评审 获取当前专家的结果数据
     */
    @RequestMapping("/getEpcDetailedPersonData")
    public Map<String, Object> getEpcDetailedPersonData() {
        Map<String, Object> result = new HashMap<>();

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        List<ExpertReviewSingleItemScore> scoreList = expertReviewSingleItemScoreService.listScoreBySth(gradeIds, null, user.getUserId(), null, EvalProcess.DETAILED.getCode(), null);
        result.put("data", scoreList);
        return result;
    }

    /**
     * 详细评审 小组汇总 详细结果页面
     *
     * @return 小组汇总详细结果页面
     */
    @RequestMapping("/epcDetailedGroupContent")
    public ModelAndView epcDetailedGroupContent() {
        ModelAndView mav = new ModelAndView("/epc/detailed/result/epcDetailedGroupContent");
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");

        // 获取进入详细评审的投标人(包括专家打分完成情况)
        List<Bidder> bidders = bidEvalService.listBidderPassPreliminary(bidSectionId);

        List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);
        // 获取详细评审当前评审类型的评标办法
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());

        //获取已经完成个人评审的评标专家
        String endExpertIds = expertUserService.listExpertIdsForPersonEnd(bidSectionId, grades.get(0).getId());


        ExpertReview query = ExpertReview.builder()
                .gradeId(grades.get(0).getId())
                .bidSectionId(bidSectionId)
                .expertId(user.getUserId())
                .build();
        ExpertReview expertReview = expertReviewService.getExpertReview(query);

        //判断是否所有人已经结束个人评审
        int isAllExpertEnd = 1;
        for (int i = 0; i < grades.size(); i++) {
            Grade grade = grades.get(i);
            // 检查所有专家是否结束个人评审
            if (!bidEvalService.validAllExpertEval(bidSectionId, grade.getId())) {
                isAllExpertEnd = 0;
                break;
            }
        }

        mav.addObject("bidders", bidders);
        mav.addObject("expertUsers", expertUsers);
        mav.addObject("expertReview", expertReview);
        mav.addObject("grades", grades);
        mav.addObject("isAllExpertEnd", isAllExpertEnd);
        mav.addObject("endExpertIds", endExpertIds);
        return mav;
    }

    /**
     * 详细评审 小组汇总 所有投标人 结果数据
     * @return 小组汇总 所有投标人 结果数据
     */
    @RequestMapping("/getEpcDetailedGroupBiddersResult")
    public List<BidderResultDTO> getEpcDetailedGroupBiddersResult(){
        return bidEvalService.getEpcDetailedGroupBiddersResult();
    }


    /**
     * 详细评审 小组汇总 当前投标人 结果数据
     * @param bidderId 投标人ID
     * @param isAllExpertEnd 是否所有专家都个人评审结束
     * @return 小组汇总 当前投标人 结果数据
     */
    @RequestMapping("/getEpcDetailedGroupBidderResult")
    public Map<String, Object> getEpcDetailedGroupBidderResult(Integer bidderId, Integer isAllExpertEnd){
        return bidEvalService.getEpcDetailedGroupBidderResult(bidderId, isAllExpertEnd);
    }

    /**
     * 计算报价得分页面
     *
     * @return 计算报价得分页面
     */
    @RequestMapping("/priceScoreReviewIndex")
    public ModelAndView priceScoreReviewIndex() {
        ModelAndView mav = new ModelAndView("/epc/calcPrice/priceScoreReviewIndex");
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
        // 筛选参加报价分计算的投标人
        List<Bidder> bidders;
        if (!Enabled.YES.getCode().equals(bidSection.getPriceRecordStatus())) {
            String redisKey = "CALC_QUOTE_SCORE_" + bidSectionId;
            Object calcQuoteScore = RedisUtil.get(redisKey);
            if (calcQuoteScore == null) {
                RedisUtil.set(redisKey, Status.NOT_START.getCode(), 300);
                try {
                    boolean status = epcBidEvalService.addQuotationScore(bidSectionId);
                    if (status) {
                        bidders = bidEvalService.listBidderQuoteScore(bidSectionId);
                        this.setSystemActionMessage("报价得分计算完成！", WebSocketMessage.Dialog.MSG, WebSocketMessage.Type.INFO);
                    } else {
                        bidders = bidEvalService.listBidderPassPreliminary(bidSectionId);
                        this.setSystemActionMessage("报价得分计算失败！", WebSocketMessage.Dialog.ALERT, WebSocketMessage.Type.ERROR);
                    }
                } catch (CustomException e) {
                    bidders = bidEvalService.listBidderPassPreliminary(bidSectionId);
                    this.setSystemActionMessage("报价得分计算失败！", WebSocketMessage.Dialog.ALERT, WebSocketMessage.Type.ERROR);
                } finally {
                    RedisUtil.delete(redisKey);
                }
            } else {
                bidders = bidEvalService.listBidderPassPreliminary(bidSectionId);
                this.setSystemActionMessage("报价得分计算中，清等待...", WebSocketMessage.Dialog.ALERT, WebSocketMessage.Type.INFO);
            }
        } else {
            if (!Enabled.YES.getCode().equals(bidSection.getUpdateScoreStatus())) {
                bidders = bidEvalService.listBidderQuoteScore(bidSectionId);
            } else {
                bidders = bidEvalService.listBidderQuoteAppendixScore(bidSectionId);
            }
        }

        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.CALC_PRICE_SCORE.getCode());
        //初始化打分项
        ExpertReview expertReview = epcBidEvalService.initExpertPriceReview(bidSectionId, user.getUserId(), grades, bidders);
        CalcScoreParam calcScoreParam = calcScoreParamService.getCalcScoreParamBySectionId(bidSectionId);

        bidSection = bidSectionService.getBidSectionById(bidSectionId);

        mav.addObject("expertReview", expertReview);
        mav.addObject("expert", user);
        mav.addObject("bidders", bidders);
        mav.addObject("calcScoreParam", calcScoreParam);
        mav.addObject("bidSection", bidSection);
        mav.addObject("currentGrade", grades.get(0));
        mav.addObject("tenderDoc", tenderDoc);
        return mav;
    }


    /**
     * 价格分计算个人评审结束
     *
     * @return
     */
    @RequestMapping("/epcPricePersonalEnd")
    public JsonData epcPricePersonalEnd() {
        JsonData result = new JsonData();

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        if (bidEvalService.isFreeBackApplying(bidSectionId)){
            result.setCode("2");
            result.setMsg("评审回退尚未审核，操作失败!");
            return result;
        }

        if (bidEvalService.updateReviewEnd(bidSectionId, EvalProcess.CALC_PRICE_SCORE.getCode())){
            result.setCode("1");
        }else {
            result.setCode("2");
            result.setMsg("个人评审结束失败！");
        }

        return result;
    }


    /**
     * 价格分计算 小组评审结束检查
     *
     * @return 小组评审结束检查
     */
    @RequestMapping("/checkEpcPriceGroupEnd")
    public JsonData checkEpcPriceGroupEnd() {
        return epcBidEvalService.checkEpcPriceGroupEnd();
    }

    /**
     * 价格分计算小组评审结束
     *
     * @return
     */
    @RequestMapping("/epcPriceGroupEnd")
    public Boolean epcPriceGroupEnd() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        return epcBidEvalService.epcPriceGroupEnd(user.getBidSectionId());
    }

    /**
     * 跳转价格分修改页面
     *
     * @return 计算报价得分页面
     */
    @RequestMapping("/updatePriceScorePage")
    public ModelAndView updatePriceScorePage() {
        ModelAndView mav = new ModelAndView("/epc/calcPrice/updatePriceScorePage");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);

        //初始化价格分修改投标人结果表
        List<QuoteScoreResultAppendix> quoteScoreResultAppendixList = epcBidEvalService.initQuoteScoreResultAppendix(bidSectionId);
        CalcScoreParam calcScoreParam = calcScoreParamService.getCalcScoreParamBySectionId(bidSectionId);

        mav.addObject("bidSection", bidSection);
        mav.addObject("calcScoreParam", calcScoreParam);
        mav.addObject("quoteScoreResultAppendixList", quoteScoreResultAppendixList);
        return mav;
    }


    /**
     * 修改价格分计算参数
     * @param calcScoreParam 价格分计算参数
     * @return 修改状态
     */
    @RequestMapping("/updateCalcParam")
    public boolean updateCalcParam(CalcScoreParam calcScoreParam) {
        return calcScoreParamService.updateCalcScoreParam(calcScoreParam) > 0;
    }

    /**
     * 修改投标人价格分附录
     * @param quoteScoreResultAppendix 投标人价格分附录
     * @return 修改状态
     */
    @RequestMapping("/updateAppendix")
    public boolean updateAppendix(QuoteScoreResultAppendix quoteScoreResultAppendix) {
        return quoteScoreResultAppendixService.updateById(quoteScoreResultAppendix);
    }


    /**
     * 评审结果 综合排名页面
     *
     * @return 综合排名页面
     */
    @RequestMapping("/rankingSummaryPage")
    public ModelAndView rankingSummaryPage() {
        ModelAndView mav = new ModelAndView("/epc/result/rankingSummary");
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        List<EvalResultEpc> list = bidEvalService.listEpcRankingBidder(bidSectionId);
        mav.addObject("list", list);
        return mav;
    }

    /**
     * 评审结果 推荐候选人
     *
     * @return 推荐候选人
     */
    @RequestMapping("/candidatesPage")
    public ModelAndView candidatesPage() {
        ModelAndView mav = new ModelAndView("/epc/result/candidates");
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        List<EvalResultEpc> list = bidEvalService.listEpcRankingBidder(bidSectionId);
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
        ModelAndView mav = new ModelAndView("/epc/result/evaluationReport");

        AuthUser user = CurrentUserHolder.getUser();
        try {
            RedissonUtil.lock(CacheName.GENERATE_EVAL_REPORT_ + user.getBidSectionId(), 300, TimeUnit.SECONDS);
            Integer bidSectionId = user.getBidSectionId();
            BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
            //生成评标报告
            if (!Status.PROCESSING.getCode().equals(bidSection.getEvalPdfGenerateStatus())) {
                // 未生成时（！=1），生成报告
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
     * @return 重新生成报告
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
        ModelAndView mav = new ModelAndView("/epc/result/evaluationEnd");
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
