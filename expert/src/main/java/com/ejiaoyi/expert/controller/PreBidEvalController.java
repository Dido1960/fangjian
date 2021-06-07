package com.ejiaoyi.expert.controller;

import com.ejiaoyi.common.constant.CacheName;
import com.ejiaoyi.common.constant.EvalStatus;
import com.ejiaoyi.common.constant.ScoreType;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.EvalProcess;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.RedissonUtil;
import com.ejiaoyi.expert.service.IBidEvalService;
import com.ejiaoyi.expert.service.IPreBidEvalService;
import com.ejiaoyi.expert.support.AuthUser;
import com.ejiaoyi.expert.support.CurrentUserHolder;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 资格预审 专家评标控制器
 *
 * @author yyb
 * @since 2020/9/7
 */
@RestController
@RequestMapping("/expert/preBidEval")
public class PreBidEvalController extends BaseController {

    @Autowired
    private IBidSectionService bidSectionService;

    @Autowired
    private ITenderDocService tenderDocService;

    @Autowired
    private IGradeService gradeService;

    @Autowired
    private IBidderService bidderService;

    @Autowired
    private IBidEvalService bidEvalService;

    @Autowired
    private IExpertReviewService expertReviewService;

    @Autowired
    private IExpertReviewSingleItemService expertReviewSingleItemService;

    @Autowired
    private IPreBidEvalService preBidEvalService;

    @Autowired
    private IBidderReviewResultService bidderReviewResultService;

    @Autowired
    private IExpertUserService expertUserService;

    @Autowired
    private IFDFSService fdfsService;

    @Autowired
    private IGradeItemService gradeItemService;

    @Autowired
    ISignatureService signatureService;

    /**
     * 跳转详细评审内容页面
     *
     * @param gradeId      评审id
     * @return
     */
    @RequestMapping("/loadDetailedGradePage")
    public ModelAndView loadDetailedGradePage(Integer gradeId) {
        ModelAndView mav = new ModelAndView("/qualification/detailed/loadGradeDetailedPage");

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");

        // 获取详细评审的评标办法
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());
        if (gradeId == null) {
            gradeId = grades.get(0).getId();
        }

        Grade grade = gradeService.getGrade(gradeId, EvalProcess.DETAILED.getCode());
        // 获取进入详细评审的投标人(包括专家打分完成情况)
        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);

        ExpertReview expertReview = expertReviewSingleItemService.initExpertSingleItem(bidSectionId, user.getUserId(), grade, bidders);

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
     * 个人评审结束校验（是否符合个人评审条件）
     *
     * @return
     */
    @RequestMapping("/validQualifiedEnd")
    public JsonData validQualifiedEnd() {
        return preBidEvalService.validPersonReviewEnd();
    }

    /**
     * 校验是否有不合格项评审
     * @return
     */
    @RequestMapping("/validUnqualified")
    public boolean validUnqualified() {
        return CollectionUtils.isEmpty(preBidEvalService.listUnqualifiedItem());
    }

    /**
     * 个人评审结束
     *
     * @return
     */
    @RequestMapping("/personalReviewEnd")
    public boolean personalReviewEnd() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        return bidEvalService.updateReviewEnd(bidSectionId, EvalProcess.DETAILED.getCode());
    }

    /**
     * 跳转到不合格项意见填写页面
     * @return
     */
    @RequestMapping("/jumpOpinionPage")
    public ModelAndView jumpOpinionPage() {
        ModelAndView mav = new ModelAndView("/qualification/detailed/opinionPage");

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        List<Bidder> bidders = bidEvalService.listExpertOpinion(bidSectionId, EvalProcess.DETAILED.getCode());
        mav.addObject("bidders", bidders);
        return mav;
    }

    /**
     * 保存不合格项的评审意见
     *
     * @param expertReviewSingleItems 不合格评审意见
     * @return
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
        bidEvalService.callPersonReview(user.getBidSectionId(), EvalProcess.DETAILED.getCode());
    }

    /**
     * 验证详细评审 小组评审结束 的相关条件
     * 1.所有专家评审是否结束
     * 2.意见是否一致
     * @return
     */
    @RequestMapping("/validGroupReview")
    public JsonData validGroupReview() {
        return bidEvalService.checkPreDetailedGroupEnd();
    }

    /**
     * 评标委员会评审结束
     * @return 评标委员会评审结束
     */
    @RequestMapping("/endGroupReview")
    public boolean endGroupReview() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        try {
            bidEvalService.updateGroupReviewEndStatus(bidSectionId, EvalProcess.DETAILED.getCode());
            return true;
        } catch (Exception e) {
            e.getStackTrace();
            return false;
        }
    }


    /**
     * 专家详细评审结果框架页面
     *
     * @return
     */
    @RequestMapping("/evalResultBasePage")
    public ModelAndView evalResultBasePage() {
        ModelAndView mav = new ModelAndView("/qualification/detailed/result/evalResultBasePage");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        // 获取详细评审的评标办法
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());

        mav.addObject("bidSection", bidSection);
        mav.addObject("grade", grades.get(0));
        mav.addObject("expert", user);
        return mav;
    }


    /**
     * 展示详细评审专家个人评审结果
     *
     * @param bidSectionId 标段主键
     * @return
     */
    @RequestMapping("/personReviewResultPage")
    public ModelAndView personReviewResultPage(Integer bidSectionId) {
        ModelAndView mav = new ModelAndView("/qualification/detailed/result/personReviewResultPage");
        String[] gradeIds = tenderDocService.getTenderDocBySectionId(bidSectionId).getGradeId().split(",");
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        // 获取初步评审的评标办法
        Grade grade = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode()).get(0);
        //是否个人评审结束
        ExpertReview query = ExpertReview.builder()
                .gradeId(grade.getId())
                .bidSectionId(bidSectionId)
                .expertId(user.getUserId())
                .build();
        ExpertReview expertReview = expertReviewService.getExpertReview(query);
        mav.addObject("grade", grade);
        mav.addObject("expertReview", expertReview);
        mav.addObject("bidders", bidEvalService.listPassOpenBidder(bidSectionId, gradeIds, EvalProcess.DETAILED.getCode()));
        mav.addObject("bidSection", bidSectionService.getBidSectionById(bidSectionId));
        return mav;
    }

    /**
     * 小组评审汇总页面
     *
     * @param bidSectionId 标段主键
     * @return
     */
    @RequestMapping("/groupReviewResultPage")
    public ModelAndView groupReviewResultPage(Integer bidSectionId) {
        ModelAndView mav = new ModelAndView("/qualification/detailed/result/groupReviewResultPage");

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;

        String[] gradeIds = tenderDocService.getTenderDocBySectionId(bidSectionId).getGradeId().split(",");
        Grade grade = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode()).get(0);
        // 获取进入详细评审的投标人(包括专家打分完成情况)
        List<Bidder> bidders = bidEvalService.listPassOpenBidder(bidSectionId, gradeIds, EvalProcess.DETAILED.getCode());
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
        Integer isAllExpertEnd = 1;
        if (!bidEvalService.validAllExpertEval(bidSectionId, grade.getId())){
            isAllExpertEnd = 0;
        }

        mav.addObject("bidSectionId", bidSectionId);
        mav.addObject("grade", grade);
        mav.addObject("bidders", bidders);
        mav.addObject("expertUsers", expertUsers);
        mav.addObject("expertReview", expertReview);
        mav.addObject("isAllExpertEnd", isAllExpertEnd);
        mav.addObject("endExpertIds", endExpertIds);
        return mav;
    }

    /**
     * 跳转到修改专家个人意见页面 (合格制)
     *
     * @return
     */
    @RequestMapping("/updatePersonResult")
    public ModelAndView updatePersonResultPage(Integer singId) {
        ModelAndView mav = new ModelAndView("/qualification/detailed/result/updatePersonResultPage");
        ExpertReviewSingleItem single = expertReviewSingleItemService.getSingleById(singId);
        GradeItem gradeItem = gradeItemService.getById(single.getGradeItemId());
        mav.addObject("gradeItem", gradeItem);
        mav.addObject("single", single);
        return mav;
    }


    /**
     * 评审结果 通过名单排序页面
     *
     * @return 投标人名单页面
     */
    @RequestMapping("/passListSortPage")
    public ModelAndView passListSortPage() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        ModelAndView mav = new ModelAndView("/qualification/result/passListSort");
        List<Bidder> bidders = preBidEvalService.listPrePassSortBidder(user.getBidSectionId());
        mav.addObject("bidders", bidders);
        return mav;
    }

    /**
     * 评标结束页面
     *
     * @return 评标结束页面
     */
    @RequestMapping("/evaluationEndPage")
    public ModelAndView evaluationEndPage() {
        ModelAndView mav = new ModelAndView("/qualification/result/evaluationEnd");
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
     * 评标报告页面
     *
     * @return 评标报告页面
     */
    @RequestMapping("/evaluationReportPage")
    public ModelAndView evaluationReportPage() {
        ModelAndView mav = new ModelAndView("/qualification/result/evaluationReport");
        AuthUser user = CurrentUserHolder.getUser();
        try {
            RedissonUtil.lock(CacheName.GENERATE_EVAL_REPORT_ + user.getBidSectionId(),300, TimeUnit.SECONDS);
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
            mav.addObject("fdfs", fdfsService.getFdfsByMark(mark));
            mav.addObject("user", user);
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
}
