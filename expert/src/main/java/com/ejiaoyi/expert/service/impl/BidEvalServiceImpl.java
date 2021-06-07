package com.ejiaoyi.expert.service.impl;

import cn.hutool.core.lang.Assert;
import com.ejiaoyi.common.constant.CacheName;
import com.ejiaoyi.common.constant.EvalStatus;
import com.ejiaoyi.common.constant.ProjectFileTypeConstant;
import com.ejiaoyi.common.constant.ScoreType;
import com.ejiaoyi.common.dto.BidderResultDTO;
import com.ejiaoyi.common.dto.BidderReviewPointDTO;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.*;
import com.ejiaoyi.common.exception.CustomException;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.service.impl.BaseServiceImpl;
import com.ejiaoyi.common.util.*;
import com.ejiaoyi.expert.dto.ExpertUserMsgDTO;
import com.ejiaoyi.expert.dto.ReportDto;
import com.ejiaoyi.expert.enums.ExpertUserMsgStatus;
import com.ejiaoyi.expert.service.IBidEvalService;
import com.ejiaoyi.expert.service.IExpertService;
import com.ejiaoyi.expert.support.AuthUser;
import com.ejiaoyi.expert.support.CurrentUserHolder;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 专家评标 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-09-07
 */
@Service
public class BidEvalServiceImpl extends BaseServiceImpl implements IBidEvalService {

    @Autowired
    private IGradeItemService gradeItemService;
    @Autowired
    private IExpertReviewSingleItemService expertReviewSingleItemService;
    @Autowired
    private IGradeService gradeService;
    @Autowired
    private IExpertReviewService expertReviewService;
    @Autowired
    private IBidderService bidderService;
    @Autowired
    private IExpertUserService expertUserService;
    @Autowired
    private ITenderDocService tenderDocService;
    @Autowired
    private IBidderReviewResultService bidderReviewResultService;
    @Autowired
    private IExpertReviewSingleItemDeductService expertReviewSingleItemDeductService;
    @Autowired
    private IExpertReviewSingleItemScoreService expertReviewSingleItemScoreService;
    @Autowired
    private IBidderReviewResultDeductService bidderReviewResultDeductService;
    @Autowired
    private IBidSectionService bidSectionService;
    @Autowired
    private IEvalResultSgService evalResultSgService;
    @Autowired
    private IFDFSService fdfsService;
    @Autowired
    private IBidSectionRelateService bidSectionRelateService;
    @Autowired
    private IEvalReportService evalReportService;
    @Autowired
    private IExpertReviewSingleItemDeductScoreService expertReviewSingleItemDeductScoreService;
    @Autowired
    private IBidderReviewResultScoreService bidderReviewResultScoreService;
    @Autowired
    private IEvalResultJlService evalResultJlService;
    @Autowired
    private IQuoteScoreResultService quoteScoreResultService;
    @Autowired
    private IQuoteScoreResultAppendixService quoteScoreResultAppendixService;
    @Autowired
    private IBidderOpenInfoService bidderOpenInfoService;
    @Autowired
    private IEvalResultEpcService evalResultEpcService;
    @Autowired
    private IExpertService expertService;
    @Autowired
    private IPreBidEvalService preBidEvalService;
    @Autowired
    private IBsnChainInfoService bsnChainInfoService;
    @Autowired
    private IBidApplyService bidApplyService;
    @Autowired
    private IFreeBackApplyService freeBackApplyService;

    @Override
    public List<Bidder> listBidderGradeIsEnd(List<Bidder> bidders, Grade currentGrade, String scoreType) {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        if (ScoreType.QUALIFIED.equals(scoreType)) {
            //合格制
            for (Bidder bidder : bidders) {
                boolean isGradeEnd = true;
                List<GradeItem> gradeItems = gradeItemService.listGradeItem(currentGrade.getId());
                List<ExpertReviewSingleItem> scores = expertReviewSingleItemService.listHasScore(user.getUserId(), currentGrade.getId(), bidder.getId());
                if (gradeItems.size() != scores.size()) {
                    isGradeEnd = false;
                }
                bidder.setExpertReviewStatusForBidder(isGradeEnd);
            }
        } else if (ScoreType.ACCUMULATE.equals(scoreType)) {
            //打分制
            for (Bidder bidder : bidders) {
                boolean isGradeEnd = true;
                List<GradeItem> gradeItems = gradeItemService.listGradeItem(currentGrade.getId());
                List<ExpertReviewSingleItemScore> scores = expertReviewSingleItemScoreService.listHasScore(user.getUserId(), currentGrade.getId(), bidder.getId());
                if (gradeItems.size() != scores.size()) {
                    isGradeEnd = false;
                }
                bidder.setExpertReviewStatusForBidder(isGradeEnd);
            }
        } else if (ScoreType.DEDUCT.equals(scoreType)) {
            //扣分制
            for (Bidder bidder : bidders) {
                boolean isGradeEnd = true;
                List<GradeItem> gradeItems = gradeItemService.listGradeItem(currentGrade.getId());
                List<ExpertReviewSingleItemDeduct> scores = expertReviewSingleItemDeductService.listHasScore(user.getUserId(), currentGrade.getId(), bidder.getId());
                if (gradeItems.size() != scores.size()) {
                    isGradeEnd = false;
                }
                bidder.setExpertReviewStatusForBidder(isGradeEnd);
            }
        }
        return bidders;
    }

    @Override
    public Grade getGradeDetailItem(Integer bidSectionId, Integer gradeId, Integer evalProcess) {
        Grade grade = gradeService.getGrade(gradeId, evalProcess);
        AuthUser user = CurrentUserHolder.getUser();

        if (null != grade) {
            assert user != null;
            ExpertReview query = ExpertReview.builder()
                    .gradeId(gradeId)
                    .bidSectionId(bidSectionId)
                    .expertId(user.getUserId())
                    .build();
            ExpertReview expertReview = expertReviewService.getExpertReview(query);

            //已经评分的项目
            if (null != expertReview) {
                if (EvalProcess.DETAILED.getCode().equals(evalProcess)) {
                    BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
                    if (BidProtype.CONSTRUCTION.getCode().equals(bidSection.getBidClassifyCode())) {
                        //施工 详细评审的数据封装
                        ExpertReviewSingleItemDeduct deductQuery = ExpertReviewSingleItemDeduct.builder()
                                .expertReviewId(expertReview.getId())
                                .build();
                        List<ExpertReviewSingleItemDeduct> deducts = expertReviewSingleItemDeductService.listExpertReviewSingleItemDeduct(deductQuery);
                        grade.setExpertReviewSingleItemDeducts(deducts);
                    } else if (BidProtype.QUALIFICATION.getCode().equals(bidSection.getBidClassifyCode())) {
                        //资格预审 详细评审的数据封装
                        List<ExpertReviewSingleItem> expertReviewSingleItems = expertReviewSingleItemService.listExpertReviewSingleItem(ExpertReviewSingleItem.builder()
                                .expertReviewId(expertReview.getId())
                                .build());
                        grade.setExpertReviewSingleItems(expertReviewSingleItems);
                    } else if (BidProtype.SUPERVISION.getCode().equals(bidSection.getBidClassifyCode())) {//监理 详细评审的数据封装

                    } else {
                        /*ExpertReviewSingleItemScore expertReviewSingleItemScore = ExpertReviewSingleItemScore
                                .builder()
                                .expertReviewId(expertReview.getId())
                                .build();*/
//                    List<ExpertReviewSingleItemScore> expertReviewSingleItemScores = expertReviewSingleItemScoreService.listExpertReviewSingleItemScore(expertReviewSingleItemScore);
//                    grade.setScoreList(expertReviewSingleItemScores);
                    }
                } else {
                    ExpertReviewSingleItem expertReviewSingleItem = ExpertReviewSingleItem
                            .builder()
                            .expertReviewId(expertReview.getId())
                            .build();
                    List<ExpertReviewSingleItem> expertReviewSingleItems = expertReviewSingleItemService.listExpertReviewSingleItem(expertReviewSingleItem);
                    grade.setExpertReviewSingleItems(expertReviewSingleItems);
                }
            }
        }
        return grade;
    }

    @Override
    public List<ExpertReviewSingleItem> listItemByBidderId(Integer bidderId, Integer gradeId) {
        AuthUser user = CurrentUserHolder.getUser();
        ExpertReviewSingleItem expertReviewSingleItem = ExpertReviewSingleItem
                .builder()
                .expertId(user.getUserId())
                .bidderId(bidderId)
                .gradeId(gradeId)
                .build();
        return expertReviewSingleItemService.listExpertReviewSingleItem(expertReviewSingleItem);
    }

    @Override
    public Integer saveExpertReviewSingleItem(ExpertReviewSingleItem expertReviewSingleItem) {
        return expertReviewSingleItemService.updateSingleItem(expertReviewSingleItem);
    }

    @Override
    public boolean validBidderReviewComplete(Integer bidderId, Integer gradeId) {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        List<ExpertReviewSingleItem> items = expertReviewSingleItemService.listHasScore(user.getUserId(), gradeId, bidderId);
        List<GradeItem> gradeItems = gradeItemService.listGradeItem(gradeId);
        return gradeItems.size() == items.size();
    }

    @Override
    public void passAllQualified(Integer gradeId, Integer userId) {
        expertReviewSingleItemService.updateListItem(gradeId, userId);
    }

    @Override
    public List<Bidder> listPassOpenBidder(Integer bidSectionId, String[] gradeIds, Integer evalProcess) {
        List<Bidder> bidders = bidderService.listPassBidOpenBidder(bidSectionId);
        if (evalProcess.equals(EvalProcess.DETAILED.getCode())) {
            bidders = bidderService.listDetailedBidder(bidSectionId);
        }
        List<Grade> grades = gradeService.listGrade(gradeIds, evalProcess);
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        for (Bidder bidder : bidders) {
            // 默认当前专家对该投标人评审环节打分结束
            boolean expertReviewStatusForBidder = true;
            for (Grade grade : grades) {
                List<ExpertReviewSingleItem> items = expertReviewSingleItemService.listHasScore(user.getUserId(), grade.getId(), bidder.getId());
                // 如果评审项与专家打分项长度不等，代表该评审环节尚未结束，则跳出该循环，进行下一个投标人打分情况判断
                if (items.size() != grade.getGradeItems().size()) {
                    expertReviewStatusForBidder = false;
                    break;
                }
            }
            bidder.setExpertReviewStatusForBidder(expertReviewStatusForBidder);
        }
        return bidders;
    }

    @Override
    public boolean validAllExpertEval(Integer bidSectionId, Integer gradeId) {
        List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);
        List<ExpertReview> expertReviews = expertReviewService.listCompleteExpertReviewInfo(gradeId);
        return expertUsers.size() <= expertReviews.size();
    }

    @Override
    public void callPersonReview(Integer bidSectionId, Integer evalProcess) {
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        expertReviewService.updateCallPersonReview(gradeIds, evalProcess);
        //设置redis消息 关于环节重评的消息推送
        expertUserMsgPush(bidSectionId, evalProcess, ExpertUserMsgStatus.RE_EVALUATION);
    }

    /**
     * 专家组长对其他专家 推送消息
     *
     * @param bidSectionId 标段ID
     * @param evalProcess  评审环节
     */
    @Override
    public void expertUserMsgPush(Integer bidSectionId, Integer evalProcess, ExpertUserMsgStatus msgStatus) {
        BidApply bidApply = bidApplyService.getBidApplyByBidSectionId(bidSectionId);
        Integer chairMan = bidApply.getChairMan();
        List<ExpertUser> expertUsers = expertUserService.listExpertsExceptLeader(bidSectionId, chairMan);
        String baseKey = CacheName.EXPERT_USER_MSG_ + bidSectionId;
        EvalProcess process = EvalProcess.getCode(evalProcess);
        ExpertUserMsgDTO msg = ExpertUserMsgDTO.builder()
                .msgStatus(msgStatus.getCode())
                .msg("【" + process.getRemake() + "】环节," + msgStatus.getRemark()).build();
        for (ExpertUser expertUser : expertUsers) {
            RedisUtil.set(baseKey + "_" + expertUser.getId(), msg);
        }
    }

    @Override
    public JsonData validQualifiedEnd() {
        JsonData result = new JsonData();

        boolean endStatus = false;
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        if (isFreeBackApplying(bidSectionId)) {
            result.setCode("2");
            result.setMsg("评审回退尚未审核，操作失败!");
            return result;
        }

        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        // 获取 初步评审 的投标人
        List<Bidder> bidders = null;
        if (BidProtype.EPC.getCode().equals(bidSection.getBidClassifyCode())) {
            //当前标段为施工总承包,获取通过资格审查的投标人
            bidders = bidderService.listBidderPassQualifyReview(bidSectionId);
        } else {
            bidders = bidderService.listPassBidOpenBidder(bidSectionId);
        }
        // 获取初步评审的评标办法
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.PRELIMINARY.getCode());
        if (CollectionUtils.isEmpty(grades) || CollectionUtils.isEmpty(bidders)) {
            result.setCode("2");
            result.setMsg("初步评审还有评审项未完成，请检查！");
            return result;
        }
        for (Grade grade : grades) {
            List<ExpertReviewSingleItem> items = expertReviewSingleItemService.listAllHasScore(user.getUserId(), grade.getId());
            endStatus = (grade.getGradeItems().size() * bidders.size()) != items.size();
            if (endStatus) {
                break;
            }
        }

        if (!endStatus) {
            result.setCode("1");
        } else {
            result.setCode("2");
            result.setMsg("初步评审还有评审项未完成，请检查！");
        }

        return result;
    }

    @Override
    public List<ExpertReviewSingleItem> listUnqualifiedItem() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");

        // 获取初步评审的评标办法
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.PRELIMINARY.getCode());
        if (CollectionUtils.isEmpty(grades)) {
            return null;
        }
        List<ExpertReviewSingleItem> expertReviewSingleItems = new ArrayList<>();
        for (Grade grade : grades) {
            ExpertReviewSingleItem expertReviewSingleItem = ExpertReviewSingleItem.builder()
                    .bidSectionId(bidSectionId)
                    .expertId(user.getUserId())
                    .gradeId(grade.getId())
                    .evalResult(Enabled.NO.getCode().toString())
                    .gradeType(Integer.valueOf(grade.getGradeType()))
                    .build();
            List<ExpertReviewSingleItem> items = expertReviewSingleItemService.listExpertReviewSingleItem(expertReviewSingleItem);
            expertReviewSingleItems.addAll(items);
        }

        return expertReviewSingleItems;
    }

    @Override
    public boolean updateReviewEnd(Integer bidSectionId, Integer evalProcess) {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        return expertReviewService.updatePersonalReviewEnd(gradeIds, evalProcess, user.getUserId());
    }

    @Override
    public List<Bidder> listExpertOpinion(Integer bidSectionId, Integer evalProcess) {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");

        // 获取进入当前环节的投标人(包括专家打分完成情况)
        List<Bidder> bidders = null;
        if (EvalProcess.PRELIMINARY.getCode().equals(evalProcess) && BidProtype.EPC.getCode().equals(bidSection.getBidClassifyCode())) {
            //当前标段为施工总承包并且环节为初步评审 则 获取通过资格审查的投标人
            bidders = bidderService.listBidderPassQualifyReview(bidSectionId);
        } else {
            bidders = bidderService.listPassBidOpenBidder(bidSectionId);
        }
        // 获取评标办法
        List<Grade> grades = gradeService.listGrade(gradeIds, evalProcess);

        for (Bidder bidder : bidders) {
            int hasUnqualifiedCount = 0;
            // 封装不合格项意见内容
            List<ExpertReviewSingleItem> expertReviewSingleItems = new ArrayList<>();
            for (Grade grade : grades) {
                ExpertReviewSingleItem expertReviewSingleItem = ExpertReviewSingleItem.builder()
                        .bidSectionId(bidSectionId)
                        .expertId(user.getUserId())
                        .gradeId(grade.getId())
                        .bidderId(bidder.getId())
                        .evalResult(Enabled.NO.getCode().toString())
                        .build();
                List<ExpertReviewSingleItem> items = expertReviewSingleItemService.listExpertReviewSingleItem(expertReviewSingleItem);
                expertReviewSingleItems.addAll(items);
                hasUnqualifiedCount += items.size();
            }
            bidder.setExpertReviewSingleItems(expertReviewSingleItems);
            bidder.setHasUnqualifiedCount(hasUnqualifiedCount);
        }

        return bidders;
    }


    @Override
    public JsonData checkPreDetailedGroupEnd() {
        JsonData result = new JsonData();

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        if (isFreeBackApplying(bidSectionId)) {
            result.setCode("2");
            result.setMsg("评审回退尚未审核，操作失败!");
            return result;
        }

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());

        Integer[] currentGradeIds = new Integer[grades.size()];
        for (int i = 0; i < grades.size(); i++) {
            Grade grade = grades.get(i);
            // 检查所有专家是否结束个人评审
            if (!validAllExpertEval(bidSectionId, grade.getId())) {
                result.setCode("2");
                List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);
                List<ExpertUser> list = expertService.listLinkNoFinishExpertUsers(expertUsers, grade.getId());
                List<String> expertNames = list.stream().map(ExpertUser::getExpertName).collect(Collectors.toList());
                StringBuilder stringBuilder = new StringBuilder();
                for (int j = 0; j < expertNames.size(); j++) {
                    stringBuilder.append(j + 1).append("、").append(expertNames.get(j)).append("<br>");
                }
                result.setMsg("<b>以下专家未结束:</b><br>" + stringBuilder);
                return result;
            }
            currentGradeIds[i] = grade.getId();
        }
        // 检查所有专家是否评审一致
        if (!validAllExpertEvalSame(currentGradeIds)) {
            result.setCode("2");
            result.setMsg("专家评审结果不一致，无法结束小组评审！");
            return result;
        }
        result.setCode("1");
        result.setMsg("是否结束小组评审？");
        return result;
    }

    /**
     * 检测所有专家的各详细评审项的 结果是否一致
     *
     * @param gradeIds 评审标准ids
     * @return
     */
    private boolean validAllExpertEvalSame(Integer[] gradeIds) {
        return expertReviewSingleItemService.listCountByEvalResult(gradeIds);
    }

    @Override
    public void updateGroupReviewEndStatus(Integer bidSectionId, Integer evalProcess) throws CustomException {
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        List<Grade> grades = gradeService.listGrade(gradeIds, evalProcess);
        for (Grade grade : grades) {
            addBidderResult(grade.getId(), bidSectionId);
            Grade build = Grade.builder()
                    .id(grade.getId())
                    .groupEnd(Enabled.YES.getCode())
                    .build();
            boolean status = gradeService.updateGrade(build);
            if (!status) {
                throw new CustomException("评标委员会评审结束失败！");
            }
        }
        expertUserMsgPush(bidSectionId, evalProcess, ExpertUserMsgStatus.GROUP_END);
    }

    @Override
    public List<Bidder> listBidderPassPreliminary(Integer bidSectionId) {
        return bidderService.listDetailedBidder(bidSectionId);
    }

    @Override
    public Grade getGradeForDeduct(Integer bidSectionId, Integer gradeId, Integer evalProcess, Integer bidderId) {
        Grade grade = gradeService.getGradeById(gradeId);
        AuthUser user = CurrentUserHolder.getUser();

        if (null != grade) {
            assert user != null;
            //封装 score 以及 gradeitem
            ExpertReviewSingleItemDeduct query = ExpertReviewSingleItemDeduct.builder()
                    .expertId(user.getUserId())
                    .gradeId(gradeId)
                    .bidderId(bidderId)
                    .build();
            List<ExpertReviewSingleItemDeduct> scores = expertReviewSingleItemDeductService.listExpertReviewSingleItemDeduct(query);
            grade.setExpertReviewSingleItemDeducts(scores);
        }
        return grade;
    }

    @Override
    public Boolean validBidderDeductCompletion(Integer bidderId, Integer gradeId) {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        List<ExpertReviewSingleItemDeduct> items = expertReviewSingleItemDeductService.listHasScore(user.getUserId(), gradeId, bidderId);
        List<GradeItem> gradeItems = gradeItemService.listGradeItem(gradeId);
        return gradeItems.size() == items.size();
    }

    @Override
    public Integer oneKeyNoDeduct(Integer gradeId) {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        return expertReviewSingleItemDeductService.updateListDeduct(user.getUserId(), gradeId);
    }

    @Override
    public JsonData checkPersonalDeductEnd() {
        JsonData result = new JsonData();

        boolean endStatus = false;
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        if (isFreeBackApplying(bidSectionId)) {
            result.setCode("2");
            result.setMsg("评审回退尚未审核，操作失败!");
            return result;
        }

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        // 获取进入详细评审的投标人
        List<Bidder> bidders = listBidderPassPreliminary(user.getBidSectionId());

        // 获取初步评审的评标办法
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());
        if (CollectionUtils.isEmpty(grades) || CollectionUtils.isEmpty(bidders)) {
            result.setCode("2");
            result.setMsg("详细评审还有评审项未完成，请检查！");
            return result;
        }
        for (Grade grade : grades) {
            List<ExpertReviewSingleItemDeduct> items = expertReviewSingleItemDeductService.listAllHasScore(user.getUserId(), grade.getId());
            endStatus = (grade.getGradeItems().size() * bidders.size()) != items.size();
            if (endStatus) {
                break;
            }
        }
        if (!endStatus) {
            result.setCode("1");
        } else {
            result.setCode("2");
            result.setMsg("详细评审还有评审项未完成，请检查！");
        }

        return result;
    }

    @Override
    public Boolean checkDeduct() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");

        // 获取详细评审的评标办法
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());
        if (CollectionUtils.isEmpty(grades)) {
            return null;
        }
        List<ExpertReviewSingleItemDeduct> expertReviewSingleItemDeducts = new ArrayList<>();
        for (Grade grade : grades) {
            ExpertReviewSingleItemDeduct deduct = ExpertReviewSingleItemDeduct.builder()
                    .expertId(user.getUserId())
                    .gradeId(grade.getId())
                    .evalResult(Enabled.NO.getCode().toString())
                    .build();
            List<ExpertReviewSingleItemDeduct> items = expertReviewSingleItemDeductService.listExpertReviewSingleItemDeduct(deduct);
            expertReviewSingleItemDeducts.addAll(items);
        }
        return !CommonUtil.isEmpty(expertReviewSingleItemDeducts) && expertReviewSingleItemDeducts.size() > 0;
    }

    @Override
    public List<Bidder> listDeductOpinion() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");

        // 获取进入详细评审的投标人
        List<Bidder> bidders = listBidderPassPreliminary(user.getBidSectionId());

        // 获取初步评审的评标办法
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());

        for (Bidder bidder : bidders) {
            int hasUnqualifiedCount = 0;
            // 封装不合格项意见内容
            List<ExpertReviewSingleItemDeduct> expertReviewSingleItemDeducts = new ArrayList<>();
            for (Grade grade : grades) {
                ExpertReviewSingleItemDeduct deduct = ExpertReviewSingleItemDeduct.builder()
                        .expertId(user.getUserId())
                        .gradeId(grade.getId())
                        .bidderId(bidder.getId())
                        .evalResult(Enabled.NO.getCode().toString())
                        .build();

                List<ExpertReviewSingleItemDeduct> items = expertReviewSingleItemDeductService.listExpertReviewSingleItemDeduct(deduct);
                expertReviewSingleItemDeducts.addAll(items);
                hasUnqualifiedCount += items.size();
            }
            bidder.setExpertReviewSingleItemDeducts(expertReviewSingleItemDeducts);
            bidder.setHasUnqualifiedCount(hasUnqualifiedCount);
        }

        return bidders;
    }

    @Override
    public JsonData checkDeductGroupEnd() {
        JsonData result = new JsonData();
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        if (isFreeBackApplying(bidSectionId)) {
            result.setCode("2");
            result.setMsg("评审回退尚未审核，操作失败!");
            return result;
        }

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());

        Integer[] currentGradeIds = new Integer[grades.size()];
        for (int i = 0; i < grades.size(); i++) {
            Grade grade = grades.get(i);
            // 检查所有专家是否结束个人评审
            if (!validAllExpertEval(bidSectionId, grade.getId())) {
                result.setCode("2");
                List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);
                List<ExpertUser> list = expertService.listLinkNoFinishExpertUsers(expertUsers, grade.getId());
                List<String> expertNames = list.stream().map(ExpertUser::getExpertName).collect(Collectors.toList());
                StringBuilder stringBuilder = new StringBuilder();
                for (int j = 0; j < expertNames.size(); j++) {
                    stringBuilder.append(j + 1).append("、").append(expertNames.get(j)).append("<br>");
                }
                result.setMsg("<b>以下专家未结束:</b><br>" + stringBuilder);
                return result;
            }
            currentGradeIds[i] = grade.getId();
        }
        //判断安全质量和建筑市场是否意见一致
        if (!checkDeductUnanimous(currentGradeIds, ConDetailedMethod.SAFETY_QUALITY_ACCIDENT.getName(), ConDetailedMethod.BAD_RECORD_MARKET.getName())) {
            result.setCode("2");
            result.setMsg("安全质量事故扣分或建筑市场不良记录扣分未达成一致意见，请进行环节重评！");
            return result;
        }
        result.setCode("1");
        //判断施工能力与设计是否一致
        if (!checkDeductUnanimous(currentGradeIds, ConDetailedMethod.CONSTRUCTION_ABILITY.getName(), ConDetailedMethod.CONSTRUCTION_DESIGN.getName())) {
            result.setMsg("施工能力扣分或施工组织设计扣分未达成一致意见，结束小组评审，将按照少数服从多数进行结果统计！");
            return result;
        }
        result.setMsg("是否结束小组评审？");
        return result;
    }

    @Override
    public Boolean endDeductGroupReview() {
        //获取详细评审的评标办法
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());
        // 获取进入详细评审的投标人
        List<Bidder> bidders = listBidderPassPreliminary(user.getBidSectionId());
        //对每个grade进行扣分统计（少数服从多数）
        for (Grade grade : grades) {
            for (Bidder bidder : bidders) {
                List<BidderResultDTO> bidderResultDTOS = expertReviewSingleItemDeductService.listSumResult(grade.getId(), bidder.getId());
                double deductNum = 0.0;
                for (BidderResultDTO bidderResultDTO : bidderResultDTOS) {
                    if (bidderResultDTO.getDeductSum() > bidderResultDTO.getNoDeductSum()) {
                        deductNum += Double.parseDouble(bidderResultDTO.getScore());
                    }
                }
                BidderReviewResultDeduct newResult = BidderReviewResultDeduct.builder()
                        .bidderId(bidder.getId())
                        .bidSectionId(bidSectionId)
                        .deductScore(new BigDecimal(deductNum).setScale(2, BigDecimal.ROUND_HALF_UP).toString())
                        .gradeId(grade.getId()).build();
                bidderReviewResultDeductService.saveOrUpdateResultDeduct(newResult);
            }
            Grade build = Grade.builder()
                    .id(grade.getId())
                    .groupEnd(Enabled.YES.getCode())
                    .build();
            boolean status = gradeService.updateGrade(build);
            if (!status) {
                return false;
            }
        }
        expertUserMsgPush(bidSectionId, EvalProcess.DETAILED.getCode(), ExpertUserMsgStatus.GROUP_END);
        return true;
    }

    @Override
    public List<EvalResultSg> listConRankingBidder(Integer bidSectionId) {
        //获取当前标段对应施工的排名表
        List<EvalResultSg> list = evalResultSgService.listRankingBidderByBsId(bidSectionId);
        //如果list为null则重新插入数据
        if (CommonUtil.isEmpty(list) || list.size() == 0) {
            //列表为空则重新进行数据封装
            list = evalResultSgService.addResultByBsId(bidSectionId);
        }
        return list;
    }

    @Override
    public Boolean generateReport(AuthUser user) {
        Integer bidSectionId = user.getBidSectionId();
        RedisUtil.set(CacheName.GET_REPORT_PDF_SIGN_INFO + user.getBidSectionId(), user.getUserId());
        RedisUtil.expire(CacheName.GET_REPORT_PDF_SIGN_INFO + user.getBidSectionId(), 3000);
        // 开启缓冲池
//        ExecutorService executorService = Executors.newFixedThreadPool(3);
        String customPath = "";
        try {
            BidSection section = bidSectionService.getBidSectionById(bidSectionId);
            if (Status.PROCESSING.getCode().equals(section.getEvalPdfGenerateStatus())) {
                // 检查评标报告生成状态 1：已生成
                return true;
            }
            TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
            String evaluationMethod = tenderDoc.getEvaluationMethod();
            // 获取当前标段分类所有模板
            if (Enabled.YES.getCode().equals(section.getCancelStatus())) {
                // 生成流标报告
                generateCancelTempReport(bidSectionId, evaluationMethod);
            } else {
                // 评标报告
                generateEvalReport(bidSectionId, evaluationMethod);
            }
            // 生成复会报告
            generateResumptionReport(bidSectionId);
            String fileName = ProjectFileTypeConstant.EVAL_REPORT + "." + FileType.PDF.getSuffix();
            customPath = FileUtil.getEvalReportFilePath(String.valueOf(bidSectionId));
            String reportPath = customPath + File.separator + fileName;
            //pdf的mark
            String reportMark = File.separator + ProjectFileTypeConstant.EVAL_REPORT + File.separator + bidSectionId + File.separator + fileName;
            // 将报告上传到文件服务器
            Boolean aBoolean = fdfsService.uploadProjectFile(ProjectFileTypeConstant.EVAL_REPORT, bidSectionId, new File(reportPath));
            if (aBoolean) {
                Fdfs fdfs = fdfsService.getFdfsByMark(reportMark);
                //评标报告上传区块
                ThreadUtlis.run(() -> bsnChainInfoService.bidEvalReportBsnChainPut(bidSectionId, fdfs.getFileHash()));
                //更新评标状态
                BidSection bidSection = new BidSection();
                bidSection.setId(bidSectionId);
                bidSection.setEvalPdfGenerateStatus(1);
                boolean pdfStatus = bidSectionService.updateBidSectionById(bidSection) > 0;
                if (!pdfStatus) {
                    return false;
                }
                //更新标段关联表评标报告id
                BidSectionRelate bidSectionRelate = new BidSectionRelate();
                bidSectionRelate.setBidSectionId(bidSectionId);
                bidSectionRelate.setEvaluationReportId(fdfs.getId());
                // 关闭线程池
//                executorService.shutdown();
                return bidSectionRelateService.updateRelateBySectionId(bidSectionRelate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 报告生成失败，删除生成报告的专家redis
            RedisUtil.delete(CacheName.GET_REPORT_PDF_SIGN_INFO + user.getBidSectionId());
            //删除本地文件
            FileUtil.removeDir(new File(customPath));
        }
        return false;
    }

    @Override
    public List<Bidder> listBidderGradesIsEnd(List<Bidder> bidders, List<Grade> grades, Integer reviewType) {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        if (ReviewType.VIOLATION.getCode().equals(reviewType)) {
            //违章行为
            for (Bidder bidder : bidders) {
                boolean isGradeEnd = true;
                for (Grade grade : grades) {
                    List<GradeItem> gradeItems = gradeItemService.listGradeItem(grade.getId());
                    List<ExpertReviewSingleItemDeductScore> scores = expertReviewSingleItemDeductScoreService.listHasScore(user.getUserId(), grade.getId(), bidder.getId());
                    if (gradeItems.size() != scores.size()) {
                        isGradeEnd = false;
                        break;
                    }
                }
                bidder.setExpertReviewStatusForBidder(isGradeEnd);
            }
        } else {
            //商务标或技术标
            for (Bidder bidder : bidders) {
                boolean isGradeEnd = true;
                for (Grade grade : grades) {
                    List<GradeItem> gradeItems = gradeItemService.listGradeItem(grade.getId());
                    List<ExpertReviewSingleItemScore> scores = expertReviewSingleItemScoreService.listHasScore(user.getUserId(), grade.getId(), bidder.getId());
                    if (gradeItems.size() != scores.size()) {
                        isGradeEnd = false;
                        break;
                    }
                }
                bidder.setExpertReviewStatusForBidder(isGradeEnd);
            }
        }

        return bidders;
    }

    @Override
    public JsonData saveSupDetailedResult(Integer id, String score, Integer reviewType) {
        JsonData result = new JsonData();

        ExpertReviewSingleItemDeductScore deductScore = null;
        ExpertReviewSingleItemScore itemScore = null;
        GradeItem gradeItem = null;
        if (ReviewType.VIOLATION.getCode().equals(reviewType)) {
            //违章行为
            deductScore = expertReviewSingleItemDeductScoreService.getDeductScoreById(id);
            gradeItem = deductScore.getGradeItem();
        } else {
            itemScore = expertReviewSingleItemScoreService.getScoreById(id);
            gradeItem = itemScore.getGradeItem();
        }
        //判断数据是否符合
        if (!CommonUtil.isEmpty(gradeItem)) {
            if ("fixed".equals(gradeItem.getScoreType())) {
                //固定分值
                List<String> scoreRange = Arrays.asList(gradeItem.getScoreRange().split(","));
                if (!scoreRange.contains(score)) {
                    result.setCode("2");
                    result.setMsg("您输入的“" + score + "”不在取值范围内，请输入（" + gradeItem.getScoreRange() + "）内的数值！");
                    return result;
                }
            } else {
                //判断是否在最大分值到0内
                try {
                    BigDecimal maxScore = new BigDecimal(gradeItem.getScoreRange());
                    BigDecimal numScore = new BigDecimal(score);
                    if (numScore.compareTo(maxScore) <= 0 && numScore.compareTo(new BigDecimal(0)) >= 0) {
                        //判断数字是否未一位小数
                        String[] split = score.split("\\.");
                        if (split.length > 1 && split[1].length() > 1) {
                            result.setCode("2");
                            result.setMsg("请最多输入一位小数！");
                            return result;
                        }
                    } else {
                        result.setCode("2");
                        result.setMsg("您输入的“" + score + "”不在取值范围内，请输入（0 - " + gradeItem.getScoreRange() + "）范围内的数值！");
                        return result;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    result.setCode("2");
                    result.setMsg("您输入的“" + score + "”非正规数值，请输入（0 - " + gradeItem.getScoreRange() + "）范围内的数值！");
                    return result;
                }
            }
        }
        //保存数据
        try {
            if (ReviewType.VIOLATION.getCode().equals(reviewType)) {
                //违章行为
                ExpertReviewSingleItemDeductScore updateDeductScore = ExpertReviewSingleItemDeductScore.builder()
                        .id(id)
                        .evalScore(score)
                        .build();
                expertReviewSingleItemDeductScoreService.updateById(updateDeductScore);
            } else {
                ExpertReviewSingleItemScore updateScore = ExpertReviewSingleItemScore.builder()
                        .id(id)
                        .evalScore(score)
                        .build();
                expertReviewSingleItemScoreService.updateById(updateScore);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode("3");
            result.setMsg("保存失败！");
            return result;
        }
        result.setCode("1");
        return result;
    }

    @Override
    public Boolean validSupBidderCompletion(Integer bidderId, Integer reviewType) {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(user.getBidSectionId());
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        List<GradeItem> gradeItems = gradeItemService.listGradeItemBySth(gradeIds, EvalProcess.DETAILED.getCode(), reviewType);
        if (ReviewType.VIOLATION.getCode().equals(reviewType)) {
            List<ExpertReviewSingleItemDeductScore> deductScoreList = expertReviewSingleItemDeductScoreService.listHasDeductScoreBySth(gradeIds, bidderId, user.getUserId(), null, reviewType);
            return deductScoreList != null && deductScoreList.size() == gradeItems.size();
        } else {
            List<ExpertReviewSingleItemScore> scoreList = expertReviewSingleItemScoreService.listHasScoreBySth(gradeIds, bidderId, user.getUserId(), null, EvalProcess.DETAILED.getCode(), reviewType);
            return scoreList != null && scoreList.size() == gradeItems.size();
        }
    }

    @Override
    public JsonData checkSupPersonalEnd() {
        JsonData result = new JsonData();

        boolean endStatus = false;
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        if (isFreeBackApplying(bidSectionId)) {
            result.setCode("2");
            result.setMsg("评审回退尚未审核，操作失败!");
            return result;
        }

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        // 获取进入详细评审的投标人
        List<Bidder> bidders = listBidderPassPreliminary(bidSectionId);

        // 获取详细的评标办法
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());
        if (CollectionUtils.isEmpty(grades) || CollectionUtils.isEmpty(bidders)) {
            result.setCode("2");
            result.setMsg("详细评审还有评审项未完成，请检查！");
            return result;
        }
        for (Grade grade : grades) {
            if (ReviewType.VIOLATION.getCode().equals(grade.getReviewType())) {
                List<ExpertReviewSingleItemDeductScore> deductScoreList = expertReviewSingleItemDeductScoreService.listAllHasDeductScore(user.getUserId(), grade.getId());
                endStatus = (grade.getGradeItems().size() * bidders.size()) != deductScoreList.size();
            } else {
                List<ExpertReviewSingleItemScore> scoreList = expertReviewSingleItemScoreService.listAllHasScore(user.getUserId(), grade.getId());
                endStatus = (grade.getGradeItems().size() * bidders.size()) != scoreList.size();
            }
            if (endStatus) {
                break;
            }
        }

        if (!endStatus) {
            result.setCode("1");
        } else {
            result.setCode("2");
            result.setMsg("详细评审还有评审项未完成，请检查！");
        }
        return result;
    }

    @Override
    public void supOneKeyNoDeduct() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        expertReviewSingleItemDeductScoreService.updateOneKeyNoDeduct(gradeIds, ReviewType.VIOLATION.getCode(), user.getUserId());
    }

    @Override
    public Map<String, Object> getSupBidderData(Integer bidderId, Integer reviewType) {
        Map<String, Object> map = new HashMap<String, Object>();
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        //获取评审点
        List<BidderReviewPointDTO> bidderReviewPointDTOS = bidderService.getBidderReviewPoints(bidderId).getBidderReviewPointDTOS();
        map.put("bidderReviewPointDTOS", bidderReviewPointDTOS);
        //获取评审结果
        if (ReviewType.VIOLATION.getCode().equals(reviewType)) {
            //违章行为
            List<ExpertReviewSingleItemDeductScore> deductScoreList = expertReviewSingleItemDeductScoreService.listDeductScoreBySth(gradeIds, bidderId, user.getUserId(), null, reviewType);
            map.put("scoreList", deductScoreList);
        } else {
            List<ExpertReviewSingleItemScore> scoreList = expertReviewSingleItemScoreService.listScoreBySth(gradeIds, bidderId, user.getUserId(), null, EvalProcess.DETAILED.getCode(), reviewType);
            map.put("scoreList", scoreList);
        }

        return map;
    }

    @Override
    public JsonData checkSupGroupEnd() {
        JsonData result = new JsonData();

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        if (isFreeBackApplying(bidSectionId)) {
            result.setCode("2");
            result.setMsg("评审回退尚未审核，操作失败!");
            return result;
        }

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());

        //参加详细评审的投标人
        List<Bidder> bidders = listBidderPassPreliminary(bidSectionId);
        for (Grade grade : grades) {
            // 检查所有专家是否结束个人评审
            if (!validAllExpertEval(bidSectionId, grade.getId())) {
                result.setCode("2");
                List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);
                List<ExpertUser> list = expertService.listLinkNoFinishExpertUsers(expertUsers, grade.getId());
                List<String> expertNames = list.stream().map(ExpertUser::getExpertName).collect(Collectors.toList());
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < expertNames.size(); i++) {
                    stringBuilder.append(i + 1).append("、").append(expertNames.get(i)).append("<br>");
                }
                result.setMsg("<b>以下专家未结束:</b><br>" + stringBuilder);
                return result;
            }
        }
        //判断商务标评分是否一致
        if (!expertReviewSingleItemScoreService.checkScoreConsistent(gradeIds, ReviewType.BUSINESS_STANDARD.getCode(), bidders.size())) {
            result.setCode("2");
            result.setMsg("商务标评审结果不一致，无法结束小组评审，请统一意见！");
            return result;
        }
        //判断违章行为评审是否一致
        if (!expertReviewSingleItemDeductScoreService.checkDeductScoreConsistent(gradeIds, ReviewType.VIOLATION.getCode(), bidders.size())) {
            result.setCode("2");
            result.setMsg("违章行为结果不一致，无法结束小组评审，请统一意见！");
            return result;
        }
        result.setCode("1");
        result.setMsg("是否结束小组评审？");
        return result;
    }

    @Override
    public Boolean endSupGroupReview() {
        //获取详细评审的评标办法
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());
        // 获取进入详细评审的投标人
        List<Bidder> bidders = listBidderPassPreliminary(bidSectionId);
        List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);
        if (expertUsers.size() <= 0) {
            return false;
        }
        //对每个grade进行扣分统计（少数服从多数）
        for (Grade grade : grades) {
            for (Bidder bidder : bidders) {
                if (ReviewType.VIOLATION.getCode().equals(grade.getReviewType())) {
                    //违章行为
                    String deduct = expertReviewSingleItemDeductScoreService.getAvgDeductScore(grade.getId(), bidder.getId(), expertUsers.size());
                    deduct = new BigDecimal(deduct).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    BidderReviewResultDeduct newResult = BidderReviewResultDeduct.builder()
                            .bidderId(bidder.getId())
                            .bidSectionId(bidSectionId)
                            .deductScore(deduct)
                            .gradeId(grade.getId()).build();
                    bidderReviewResultDeductService.saveOrUpdateResultDeduct(newResult);
                } else {
                    String score = expertReviewSingleItemScoreService.getAvgScore(grade.getId(), bidder.getId(), expertUsers.size());
                    score = new BigDecimal(score).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    BidderReviewResultScore newResult = BidderReviewResultScore.builder()
                            .bidderId(bidder.getId())
                            .bidSectionId(bidSectionId)
                            .addScore(score)
                            .gradeId(grade.getId()).build();
                    bidderReviewResultScoreService.saveOrUpdateResultScore(newResult);
                }
            }
            Grade build = Grade.builder()
                    .id(grade.getId())
                    .groupEnd(Enabled.YES.getCode())
                    .build();
            boolean status = gradeService.updateGrade(build);
            if (!status) {
                return false;
            }
        }
        //正常结束后对排名数据进行存储
        ThreadUtlis.run(() -> {
            evalResultJlService.addResultByBsId(bidSectionId);
        });
        expertUserMsgPush(bidSectionId, EvalProcess.DETAILED.getCode(), ExpertUserMsgStatus.GROUP_END);
        return true;
    }

    @Override
    public Map<String, Object> getSupBidderDataForResult(Integer bidderId, Integer reviewType, Integer isAllExpertEnd) {
        Map<String, Object> result = new HashMap<>();

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");

        List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);
        if (ReviewType.VIOLATION.getCode().equals(reviewType)) {
            //违章行为
            List<ExpertReviewSingleItemDeductScore> deductScoreList = expertReviewSingleItemDeductScoreService.listDeductScoreBySth(gradeIds, bidderId, null, null, reviewType);
            if (isAllExpertEnd == 1) {
                List<BidderResultDTO> bidderResultDTOS = getBidderDeductResultAvg(gradeIds, reviewType, bidderId, expertUsers.size());
                result.put("bidderResultDTOS", bidderResultDTOS);
            }
            result.put("resultList", deductScoreList);
        } else {
            List<ExpertReviewSingleItemScore> scoreList = expertReviewSingleItemScoreService.listScoreBySth(gradeIds, bidderId, null, null, EvalProcess.DETAILED.getCode(), reviewType);
            if (isAllExpertEnd == 1) {
                List<BidderResultDTO> bidderResultDTOS = getBidderScoreResultAvg(gradeIds, reviewType, bidderId, expertUsers.size());
                result.put("bidderResultDTOS", bidderResultDTOS);
            }
            result.put("resultList", scoreList);
        }
        return result;
    }

    @Override
    public List<EvalResultJl> listSupRankingBidder(Integer bidSectionId) {
        //获取当前标段对应施工的排名表
        List<EvalResultJl> list = evalResultJlService.listRankingBidderByBsId(bidSectionId);
        //如果list为null则重新插入数据
        if (CommonUtil.isEmpty(list) || list.size() == 0) {
            //列表为空则重新进行数据封装
            list = evalResultJlService.addResultByBsId(bidSectionId);
        }
        return list;
    }

    @Override
    public Map<String, Object> getConBidderData(Integer gradeId, Integer bidderId) {
        Map<String, Object> result = new HashMap<>();
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        ExpertReviewSingleItemDeduct query = ExpertReviewSingleItemDeduct.builder()
                .expertId(user.getUserId())
                .gradeId(gradeId)
                .bidderId(bidderId)
                .build();

        List<ExpertReviewSingleItemDeduct> deductList = expertReviewSingleItemDeductService.listExpertReviewSingleItemDeduct(query);
        List<BidderReviewPointDTO> pointList = bidderService.getBidderReviewPoints(bidderId).getBidderReviewPointDTOS();
        result.put("deductList", deductList);
        result.put("pointList", pointList);
        return result;
    }

    @Override
    public Map<String, Object> getConBidderDataForResult(Integer bidderId, Integer gradeId, Integer isAllExpertEnd) {
        Map<String, Object> result = new HashMap<>();

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);
        ExpertReviewSingleItemDeduct query = ExpertReviewSingleItemDeduct.builder()
                .bidderId(bidderId)
                .gradeId(gradeId)
                .build();
        List<ExpertReviewSingleItemDeduct> deducts = expertReviewSingleItemDeductService.listExpertReviewSingleItemDeduct(query);
        result.put("deducts", deducts);
        if (isAllExpertEnd == 1) {
            //汇总数据
            List<BidderResultDTO> bidderResultDTOS = expertReviewSingleItemDeductService.listResultIsConsistent(gradeId, bidderId, expertUsers.size());
            result.put("bidderResultDTOS", bidderResultDTOS);
        }
        return result;
    }

    @Override
    public List<BidderReviewResultDeduct> getConBiddersGradeResult(Integer gradeId) {
        List<BidderReviewResultDeduct> result = new ArrayList<>();
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        // 获取进入详细评审的投标人
        List<Bidder> bidders = listBidderPassPreliminary(user.getBidSectionId());
        //对每个grade进行扣分统计（少数服从多数）
        for (Bidder bidder : bidders) {
            List<BidderResultDTO> bidderResultDTOS = expertReviewSingleItemDeductService.listSumResult(gradeId, bidder.getId());
            BidderReviewResultDeduct newResult = BidderReviewResultDeduct.builder()
                    .bidderId(bidder.getId())
                    .isConsistent(true)
                    .gradeId(gradeId).build();
            double deductNum = 0.0;
            for (BidderResultDTO bidderResultDTO : bidderResultDTOS) {
                if (bidderResultDTO.getDeductSum() != 0 && bidderResultDTO.getNoDeductSum() != 0) {
                    newResult.setIsConsistent(false);
                }
                if (bidderResultDTO.getDeductSum() > bidderResultDTO.getNoDeductSum()) {
                    deductNum += Double.parseDouble(bidderResultDTO.getScore());
                }
            }
            newResult.setDeductScore(new BigDecimal(deductNum).setScale(2, BigDecimal.ROUND_HALF_UP).toString());

            result.add(newResult);
        }
        return result;
    }

    @Override
    public Map<String, Object> getPreBidderData(Integer gradeId, Integer bidderId) {
        Map<String, Object> result = new HashMap<>();
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        ExpertReviewSingleItem query = ExpertReviewSingleItem.builder()
                .expertId(user.getUserId())
                .gradeId(gradeId)
                .bidderId(bidderId)
                .build();

        List<ExpertReviewSingleItem> qualifiedList = expertReviewSingleItemService.listExpertReviewSingleItem(query);
        List<BidderReviewPointDTO> pointList = bidderService.getBidderReviewPoints(bidderId).getBidderReviewPointDTOS();
        result.put("qualifiedList", qualifiedList);
        result.put("pointList", pointList);
        return result;
    }

    @Override
    public JsonData checkPreGroupEnd() {
        JsonData result = new JsonData();

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        if (isFreeBackApplying(bidSectionId)) {
            result.setCode("2");
            result.setMsg("评审回退尚未审核，操作失败!");
            return result;
        }

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.PRELIMINARY.getCode());

        Integer[] currentGradeIds = new Integer[grades.size()];
        for (int i = 0; i < grades.size(); i++) {
            Grade grade = grades.get(i);
            // 检查所有专家是否结束个人评审
            if (!validAllExpertEval(bidSectionId, grade.getId())) {
                result.setCode("2");
                List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);
                List<ExpertUser> list = expertService.listLinkNoFinishExpertUsers(expertUsers, grade.getId());
                List<String> expertNames = list.stream().map(ExpertUser::getExpertName).collect(Collectors.toList());
                StringBuilder stringBuilder = new StringBuilder();
                for (int j = 0; j < expertNames.size(); j++) {
                    stringBuilder.append(j + 1).append("、").append(expertNames.get(j)).append("<br>");
                }
                result.setMsg("<b>以下专家未结束:</b><br>" + stringBuilder);
                return result;
            }
            currentGradeIds[i] = grade.getId();
        }
        // 检查所有专家是否评审一致
        if (!validAllExpertEvalSame(currentGradeIds)) {
            result.setCode("2");
            result.setMsg("专家评审结果不一致，无法结束小组评审！");
            return result;
        }
        result.setCode("1");
        result.setMsg("是否结束小组评审？");
        return result;
    }

    @Override
    public List<BidderReviewResult> getPreBiddersGradeResult(Integer gradeId) {
        List<BidderReviewResult> result = new ArrayList<>();
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        List<Bidder> bidders;
        // 获取进入初步评审的投标人
        if (BidProtype.EPC.getCode().equals(bidSection.getBidClassifyCode())) {
            //施工总承包 获取通过资格审查的投标人
            bidders = bidderService.listBidderPassQualifyReview(bidSectionId);
        } else {
            //获取通过开标的投标人
            bidders = bidderService.listPassBidOpenBidder(bidSectionId);
        }
        // 对每个grade进行不通过统计，判断是否打分一致
        int size = gradeItemService.listGradeItem(gradeId).size();
        for (Bidder bidder : bidders) {
            List<BidderResultDTO> bidderResultDTOS = expertReviewSingleItemService.listGradeResult(gradeId, bidder.getId());
            BidderReviewResult newResult = BidderReviewResult.builder()
                    .bidderId(bidder.getId())
                    .isConsistent(true)
                    .gradeId(gradeId).build();
            if (size == bidderResultDTOS.size()) {
                for (BidderResultDTO bidderResultDTO : bidderResultDTOS) {
                    if (bidderResultDTO.getPassResult() == 0) {
                        newResult.setResult("0");
                        break;
                    }
                }
            } else {
                newResult.setIsConsistent(false);
            }
            result.add(newResult);
        }
        return result;
    }

    @Override
    public Map<String, Object> getPreBidderDataForResult(Integer bidderId, Integer gradeId, Integer isAllExpertEnd) {
        Map<String, Object> result = new HashMap<>();
        ExpertReviewSingleItem query = ExpertReviewSingleItem.builder()
                .bidderId(bidderId)
                .gradeId(gradeId)
                .build();
        List<ExpertReviewSingleItem> singleItems = expertReviewSingleItemService.listExpertReviewSingleItem(query);

        result.put("singleItems", singleItems);
        if (isAllExpertEnd == 1) {
            //汇总数据
            List<BidderResultDTO> bidderResultDTOS = expertReviewSingleItemService.listItemResult(gradeId, bidderId);
            result.put("bidderResultDTOS", bidderResultDTOS);
        }
        return result;
    }

    @Override
    public List<ReviewType> listSupReviewType() {
        List<ReviewType> list = ReviewType.listSupervisionType();
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        for (ReviewType reviewType : list) {
            reviewType.setGradeScore(gradeService.getGradeScoreByReviewType(gradeIds, EvalProcess.DETAILED.getCode(), reviewType.getCode()));
        }
        return list;
    }

    @Override
    public List<BidderResultDTO> getSupBiddersReviewResult(Integer reviewType) {
        List<BidderResultDTO> result = new ArrayList<>();
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        List<GradeItem> gradeItems = gradeItemService.listGradeItemBySth(gradeIds, EvalProcess.DETAILED.getCode(), reviewType);
        List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);

        //获取所有进入详细评审的投标人
        List<Bidder> bidders = listBidderPassPreliminary(bidSectionId);
        for (Bidder bidder : bidders) {
            BidderResultDTO bidderResultDTO = BidderResultDTO.builder().bidderId(bidder.getId()).isConsistent(true).build();
            if (ReviewType.VIOLATION.getCode().equals(reviewType)) {//违章行为
                //判断是否打分一致
                bidderResultDTO.setIsConsistent(expertReviewSingleItemDeductScoreService.checkBidderResultConsistent(gradeIds, reviewType, bidder.getId(), gradeItems.size()));
                if (bidderResultDTO.getIsConsistent()) {
                    //打分一致则获取平均分
                    String deduct = expertReviewSingleItemDeductScoreService.getAvgDeductScoreForReview(gradeIds, reviewType, bidder.getId(), expertUsers.size());
                    bidderResultDTO.setArithmeticScore(new BigDecimal(deduct).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                }
            } else if (ReviewType.BUSINESS_STANDARD.getCode().equals(reviewType)) {
                //商务标
                //判断是否打分一致
                bidderResultDTO.setIsConsistent(expertReviewSingleItemScoreService.checkBidderResultConsistent(gradeIds, reviewType, bidder.getId(), gradeItems.size()));
                if (bidderResultDTO.getIsConsistent()) {
                    //打分一致则获取平均分
                    String score = expertReviewSingleItemScoreService.getAvgScoreForReview(gradeIds, EvalProcess.DETAILED.getCode(), reviewType, bidder.getId(), expertUsers.size());
                    bidderResultDTO.setArithmeticScore(new BigDecimal(score).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                }
            } else if (ReviewType.TECHNICAL_STANDARD.getCode().equals(reviewType)) {
                //技术标
                String score = expertReviewSingleItemScoreService.getAvgScoreForReview(gradeIds, EvalProcess.DETAILED.getCode(), reviewType, bidder.getId(), expertUsers.size());
                bidderResultDTO.setArithmeticScore(new BigDecimal(score).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }
            result.add(bidderResultDTO);
        }

        return result;
    }

    /**
     * 监理获取当前投标人 当前评审类型 每个gradeItem的平均分，并判断评审结果是否一致
     *
     * @param gradeIds   所有的gradeID
     * @param reviewType 评审类型
     * @param bidderId   投标人
     * @param expertSize 专家人数
     * @return 监理获取当前投标人 当前评审类型 每个gradeItem的平均分，并判断评审结果是否一致
     */
    private List<BidderResultDTO> getBidderScoreResultAvg(String[] gradeIds, Integer reviewType, Integer bidderId, Integer expertSize) {
        List<BidderResultDTO> result = new ArrayList<>();
        if (expertSize == 0) {
            return null;
        }
        List<GradeItem> gradeItems = gradeItemService.listGradeItemBySth(gradeIds, EvalProcess.DETAILED.getCode(), reviewType);
        for (GradeItem gradeItem : gradeItems) {
            List<ExpertReviewSingleItemScore> scoreList = expertReviewSingleItemScoreService.listScoreBySth(gradeIds, bidderId, null, gradeItem.getId(), EvalProcess.DETAILED.getCode(), reviewType);
            BidderResultDTO newDto = BidderResultDTO.builder()
                    .bidderId(bidderId)
                    .gradeItemId(gradeItem.getId())
                    .isConsistent(true)
                    .build();
            String oldScore = null;
            double avgReslut = 0.0;
            for (ExpertReviewSingleItemScore score : scoreList) {
                if (CommonUtil.isEmpty(oldScore)) {
                    oldScore = score.getEvalScore();
                }
                if (!oldScore.equals(score.getEvalScore())) {
                    newDto.setIsConsistent(false);
                }
                avgReslut += Double.parseDouble(score.getEvalScore());
            }
            avgReslut = avgReslut / expertSize;
            newDto.setArithmeticScore(new BigDecimal(avgReslut).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            result.add(newDto);
        }
        return result;
    }

    /**
     * 监理获取当前投标人 当前评审类型 每个gradeItem的平均分，并判断评审结果是否一致
     *
     * @param gradeIds   所有的gradeID
     * @param reviewType 评审类型
     * @param bidderId   投标人
     * @param expertSize 专家人数
     * @return 监理获取当前投标人 当前评审类型 每个gradeItem的平均分，并判断评审结果是否一致
     */
    private List<BidderResultDTO> getBidderDeductResultAvg(String[] gradeIds, Integer reviewType, Integer bidderId, Integer expertSize) {
        List<BidderResultDTO> result = new ArrayList<>();
        if (expertSize == 0) {
            return null;
        }
        List<GradeItem> gradeItems = gradeItemService.listGradeItemBySth(gradeIds, EvalProcess.DETAILED.getCode(), reviewType);
        for (GradeItem gradeItem : gradeItems) {
            List<ExpertReviewSingleItemDeductScore> deductScoreList = expertReviewSingleItemDeductScoreService.listDeductScoreBySth(gradeIds, bidderId, null, gradeItem.getId(), reviewType);
            BidderResultDTO newDto = BidderResultDTO.builder()
                    .bidderId(bidderId)
                    .gradeItemId(gradeItem.getId())
                    .isConsistent(true)
                    .build();
            String oldScore = null;
            double avgReslut = 0.0;
            for (ExpertReviewSingleItemDeductScore score : deductScoreList) {
                if (CommonUtil.isEmpty(oldScore)) {
                    oldScore = score.getEvalScore();
                }
                if (!oldScore.equals(score.getEvalScore())) {
                    newDto.setIsConsistent(false);
                }
                avgReslut += Double.parseDouble(score.getEvalScore());
            }
            avgReslut = avgReslut / expertSize;
            newDto.setArithmeticScore(new BigDecimal(avgReslut).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            result.add(newDto);
        }
        return result;
    }

    /**
     * 检查施工详细评审中 当前方法 意见是否一致
     *
     * @param gradeIds    所有的详细评审gradeID
     * @param methodName1 方法名1
     * @param methodName2 方法名2
     * @return 安全质量事故扣分和建筑市场不良记录扣分 意见是否一致
     */
    private Boolean checkDeductUnanimous(Integer[] gradeIds, String methodName1, String methodName2) {
        return expertReviewSingleItemDeductService.checkDeductUnanimous(gradeIds, methodName1, methodName2);
    }

    /**
     * 新增投标人某环节的最终结果 （合格制）
     *
     * @param gradeId      评审标准id
     * @param bidSectionId 标段id
     */
    public void addBidderResult(Integer gradeId, Integer bidSectionId) {
        List<BidderResultDTO> bidderResults = expertReviewSingleItemService.listQualifiedInfo(gradeId, bidSectionId);
        for (BidderResultDTO bidderResult : bidderResults) {
            BidderReviewResult bidderReviewResult = BidderReviewResult.builder()
                    .bidSectionId(bidSectionId)
                    .bidderId(bidderResult.getBidderId())
                    .gradeId(gradeId)
                    .result("0")
                    .build();
            // 如果有不通过的项，则结果未该评审项不通过
            if (bidderResult.getNoPassSum() == 0) {
                bidderReviewResult.setResult("1");
            }
            bidderReviewResultService.saveOrUpdateResult(bidderReviewResult);
        }
    }

    @Override
    public List<Bidder> listBidderQuoteScore(Integer bidSectionId) {
        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);
        for (Bidder bidder : bidders) {
            QuoteScoreResult quoteScoreResult = quoteScoreResultService.getQuoteScoreResultByBidderId(bidder.getId());
            bidder.setQuoteScoreResult(quoteScoreResult);
        }
        return bidders;
    }

    @Override
    public JsonData validQualifyReviewEnd() {
        JsonData result = new JsonData();

        boolean endStatus = false;
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        if (isFreeBackApplying(bidSectionId)) {
            result.setCode("2");
            result.setMsg("评审回退尚未审核，操作失败!");
            return result;
        }

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        // 获取通过开标的投标人
        List<Bidder> bidders = bidderService.listPassBidOpenBidder(bidSectionId);

        // 获取资格预审的评标办法
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.QUALIFICATION.getCode());
        if (CollectionUtils.isEmpty(grades) || CollectionUtils.isEmpty(bidders)) {
            result.setCode("2");
            result.setMsg("资格审查还有评审项未完成，请检查！");
            return result;
        }
        for (Grade grade : grades) {
            List<ExpertReviewSingleItem> items = expertReviewSingleItemService.listAllHasScore(user.getUserId(), grade.getId());
            endStatus = (grade.getGradeItems().size() * bidders.size()) != items.size();
            if (endStatus) {
                break;
            }
        }

        if (!endStatus) {
            result.setCode("1");
        } else {
            result.setCode("2");
            result.setMsg("资格审查还有评审项未完成，请检查！");
        }

        return result;
    }

    @Override
    public List<ExpertReviewSingleItem> listUnQualifyReviewItem() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");

        // 获取初步评审的评标办法
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.QUALIFICATION.getCode());
        if (CollectionUtils.isEmpty(grades)) {
            return null;
        }
        List<ExpertReviewSingleItem> expertReviewSingleItems = new ArrayList<>();
        for (Grade grade : grades) {
            ExpertReviewSingleItem expertReviewSingleItem = ExpertReviewSingleItem.builder()
                    .bidSectionId(bidSectionId)
                    .expertId(user.getUserId())
                    .gradeId(grade.getId())
                    .evalResult(Enabled.NO.getCode().toString())
                    .gradeType(Integer.valueOf(grade.getGradeType()))
                    .build();
            List<ExpertReviewSingleItem> items = expertReviewSingleItemService.listExpertReviewSingleItem(expertReviewSingleItem);
            expertReviewSingleItems.addAll(items);
        }

        return expertReviewSingleItems;
    }

    @Override
    public JsonData checkQualifyGroupEnd() {
        JsonData result = new JsonData();

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        if (isFreeBackApplying(bidSectionId)) {
            result.setCode("2");
            result.setMsg("评审回退尚未审核，操作失败!");
            return result;
        }

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.QUALIFICATION.getCode());

        Integer[] currentGradeIds = new Integer[grades.size()];
        for (int i = 0; i < grades.size(); i++) {
            Grade grade = grades.get(i);
            // 检查所有专家是否结束个人评审
            if (!validAllExpertEval(bidSectionId, grade.getId())) {
                result.setCode("2");
                List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);
                List<ExpertUser> list = expertService.listLinkNoFinishExpertUsers(expertUsers, grade.getId());
                List<String> expertNames = list.stream().map(ExpertUser::getExpertName).collect(Collectors.toList());
                StringBuilder stringBuilder = new StringBuilder();
                for (int j = 0; j < expertNames.size(); j++) {
                    stringBuilder.append(j + 1).append("、").append(expertNames.get(j)).append("<br>");
                }
                result.setMsg("<b>以下专家未结束:</b><br>" + stringBuilder);
                return result;
            }
            currentGradeIds[i] = grade.getId();
        }
        // 检查所有专家是否评审一致
        if (!validAllExpertEvalSame(currentGradeIds)) {
            result.setCode("2");
            result.setMsg("专家评审结果不一致，无法结束小组评审！");
            return result;
        }
        result.setCode("1");
        result.setMsg("是否结束小组评审？");
        return result;
    }

    @Override
    public List<BidderReviewResult> getQualifyBiddersGradeResult(Integer gradeId) {
        List<BidderReviewResult> result = new ArrayList<>();
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        // 获取通过开标的投标人
        List<Bidder> bidders = listPassOpenBidder(bidSectionId, gradeIds, EvalProcess.QUALIFICATION.getCode());
        // 对每个grade进行不通过统计，判断是否打分一致
        int size = gradeItemService.listGradeItem(gradeId).size();
        for (Bidder bidder : bidders) {
            List<BidderResultDTO> bidderResultDTOS = expertReviewSingleItemService.listGradeResult(gradeId, bidder.getId());
            BidderReviewResult newResult = BidderReviewResult.builder()
                    .bidderId(bidder.getId())
                    .isConsistent(true)
                    .gradeId(gradeId).build();
            if (size == bidderResultDTOS.size()) {
                for (BidderResultDTO bidderResultDTO : bidderResultDTOS) {
                    if (bidderResultDTO.getPassResult() == 0) {
                        newResult.setResult("0");
                        break;
                    }
                }
            } else {
                newResult.setIsConsistent(false);
            }
            result.add(newResult);
        }
        return result;
    }

    @Override
    public List<Bidder> listBidderEpcGradesIsEnd(List<Bidder> bidders, List<Grade> grades) {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        for (Bidder bidder : bidders) {
            boolean isGradeEnd = true;
            for (Grade grade : grades) {
                List<GradeItem> gradeItems = gradeItemService.listGradeItem(grade.getId());
                List<ExpertReviewSingleItemScore> scores = expertReviewSingleItemScoreService.listHasScore(user.getUserId(), grade.getId(), bidder.getId());
                if (gradeItems.size() != scores.size()) {
                    isGradeEnd = false;
                    break;
                }
            }
            bidder.setExpertReviewStatusForBidder(isGradeEnd);
        }
        return bidders;
    }

    @Override
    public Map<String, Object> getEpcDetailedBidderData(Integer bidderId) {
        Map<String, Object> map = new HashMap<String, Object>();
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        //获取评审点
        List<BidderReviewPointDTO> bidderReviewPointDTOS = bidderService.getBidderReviewPoints(bidderId).getBidderReviewPointDTOS();
        map.put("bidderReviewPointDTOS", bidderReviewPointDTOS);
        //获取评审结果
        List<ExpertReviewSingleItemScore> scoreList = expertReviewSingleItemScoreService.listScoreBySth(gradeIds, bidderId, user.getUserId(), null, EvalProcess.DETAILED.getCode(), null);
        map.put("scoreList", scoreList);
        return map;
    }

    @Override
    public JsonData saveEpcDetailedResult(Integer id, String score) {
        JsonData result = new JsonData();

        ExpertReviewSingleItemDeductScore deductScore = null;
        ExpertReviewSingleItemScore itemScore = expertReviewSingleItemScoreService.getScoreById(id);
        GradeItem gradeItem = itemScore.getGradeItem();
        //判断数据是否符合
        if (!CommonUtil.isEmpty(gradeItem)) {
            //判断是否在最大分值到0内
            try {
                BigDecimal maxScore = new BigDecimal(gradeItem.getScore());
                BigDecimal numScore = new BigDecimal(score);
                if (numScore.compareTo(maxScore) <= 0 && numScore.compareTo(new BigDecimal(0)) >= 0) {
                    //判断数字是否未一位小数
                    String[] split = score.split("\\.");
                    if (split.length > 1 && split[1].length() > 2) {
                        result.setCode("2");
                        result.setMsg("请最多输入两位小数！");
                        return result;
                    }
                } else {
                    result.setCode("2");
                    result.setMsg("您输入的“" + score + "”不在取值范围内，请输入（0 - " + gradeItem.getScore() + "）范围内的数值！");
                    return result;
                }
            } catch (Exception e) {
                e.printStackTrace();
                result.setCode("2");
                result.setMsg("您输入的“" + score + "”非正规数值，请输入（0 - " + gradeItem.getScore() + "）范围内的数值！");
                return result;
            }
        }
        //保存数据
        try {
            ExpertReviewSingleItemScore updateScore = ExpertReviewSingleItemScore.builder()
                    .id(id)
                    .evalScore(score)
                    .build();
            expertReviewSingleItemScoreService.updateById(updateScore);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode("3");
            result.setMsg("保存失败！");
            return result;
        }
        result.setCode("1");
        return result;
    }

    @Override
    public Boolean validEpcDetailedBidderCompletion(Integer bidderId) {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(user.getBidSectionId());
        String[] gradeIds = tenderDoc.getGradeId().split(",");

        List<GradeItem> gradeItems = gradeItemService.listGradeItemBySth(gradeIds, EvalProcess.DETAILED.getCode(), null);
        List<ExpertReviewSingleItemScore> scoreList = expertReviewSingleItemScoreService.listHasScoreBySth(gradeIds, bidderId, user.getUserId(), null, EvalProcess.DETAILED.getCode(), null);
        return scoreList != null && scoreList.size() == gradeItems.size();
    }

    @Override
    public JsonData checkEpcDetailedPersonalEnd() {
        JsonData result = new JsonData();

        boolean endStatus = false;
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        if (isFreeBackApplying(bidSectionId)) {
            result.setCode("2");
            result.setMsg("评审回退尚未审核，操作失败!");
            return result;
        }

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        // 获取进入详细评审的投标人
        List<Bidder> bidders = listBidderPassPreliminary(bidSectionId);

        // 获取详细的评标办法
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());
        if (CollectionUtils.isEmpty(grades) || CollectionUtils.isEmpty(bidders)) {
            result.setCode("2");
            result.setMsg("详细评审还有评审项未完成，请检查！");
            return result;
        }
        for (Grade grade : grades) {
            List<ExpertReviewSingleItemScore> scoreList = expertReviewSingleItemScoreService.listAllHasScore(user.getUserId(), grade.getId());
            endStatus = (grade.getGradeItems().size() * bidders.size()) != scoreList.size();
            if (endStatus) {
                break;
            }
        }

        if (!endStatus) {
            result.setCode("1");
        } else {
            result.setCode("2");
            result.setMsg("详细评审还有评审项未完成，请检查！");
        }

        return result;
    }

    @Override
    public List<BidderResultDTO> getEpcDetailedGroupBiddersResult() {
        List<BidderResultDTO> result = new ArrayList<>();
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);

        //获取所有进入详细评审的投标人
        List<Bidder> bidders = listBidderPassPreliminary(bidSectionId);
        for (Bidder bidder : bidders) {
            BidderResultDTO bidderResultDTO = BidderResultDTO.builder().bidderId(bidder.getId()).isConsistent(true).build();
            String score = expertReviewSingleItemScoreService.getAvgScoreForReview(gradeIds, EvalProcess.DETAILED.getCode(), null, bidder.getId(), expertUsers.size());
            bidderResultDTO.setArithmeticScore(new BigDecimal(score).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            result.add(bidderResultDTO);
        }

        return result;
    }

    @Override
    public Map<String, Object> getEpcDetailedGroupBidderResult(Integer bidderId, Integer isAllExpertEnd) {
        Map<String, Object> result = new HashMap<>();

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");

        List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);
        List<ExpertReviewSingleItemScore> scoreList = expertReviewSingleItemScoreService.listScoreBySth(gradeIds, bidderId, null, null, EvalProcess.DETAILED.getCode(), null);
        if (isAllExpertEnd == 1 && !CommonUtil.isEmpty(expertUsers) && expertUsers.size() != 0) {
            List<BidderResultDTO> bidderResultDTOS = new ArrayList<>();
            List<GradeItem> gradeItems = gradeItemService.listGradeItemBySth(gradeIds, EvalProcess.DETAILED.getCode(), null);
            for (GradeItem gradeItem : gradeItems) {
                List<ExpertReviewSingleItemScore> itemScoreList = expertReviewSingleItemScoreService.listScoreBySth(gradeIds, bidderId, null, gradeItem.getId(), EvalProcess.DETAILED.getCode(), null);
                BidderResultDTO newDto = BidderResultDTO.builder()
                        .bidderId(bidderId)
                        .gradeItemId(gradeItem.getId())
                        .build();
                double avgReslut = 0.0;
                for (ExpertReviewSingleItemScore score : itemScoreList) {
                    avgReslut += Double.parseDouble(score.getEvalScore());
                }
                avgReslut = avgReslut / expertUsers.size();
                newDto.setArithmeticScore(new BigDecimal(avgReslut).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                bidderResultDTOS.add(newDto);
            }
            result.put("bidderResultDTOS", bidderResultDTOS);
        }
        result.put("resultList", scoreList);
        return result;
    }

    @Override
    public JsonData checkEpcDetailedGroupEnd() {
        JsonData result = new JsonData();

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        if (isFreeBackApplying(bidSectionId)) {
            result.setCode("2");
            result.setMsg("评审回退尚未审核，操作失败!");
            return result;
        }

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());

        for (Grade grade : grades) {
            // 检查所有专家是否结束个人评审
            if (!validAllExpertEval(bidSectionId, grade.getId())) {
                result.setCode("2");
                List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);
                List<ExpertUser> list = expertService.listLinkNoFinishExpertUsers(expertUsers, grade.getId());
                List<String> expertNames = list.stream().map(ExpertUser::getExpertName).collect(Collectors.toList());
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < expertNames.size(); i++) {
                    stringBuilder.append(i + 1).append("、").append(expertNames.get(i)).append("<br>");
                }
                result.setMsg("<b>以下专家未结束:</b><br>" + stringBuilder);
                return result;
            }
        }
        result.setCode("1");
        result.setMsg("是否结束小组评审？");
        return result;
    }

    @Override
    public Boolean endEpcDetailedGroupEnd() {
        //获取详细评审的评标办法
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());
        // 获取进入详细评审的投标人
        List<Bidder> bidders = listBidderPassPreliminary(bidSectionId);
        List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);
        if (expertUsers.size() <= 0) {
            return false;
        }
        //对每个grade进行得分统计（少数服从多数）
        for (Grade grade : grades) {
            for (Bidder bidder : bidders) {
                List<BidderResultDTO> bidderResultDTOS = expertReviewSingleItemScoreService.getAvgScoreBySth(null, grade.getId(), bidSectionId, bidder.getId(), null);
                String score = bidderResultDTOS.get(0).getArithmeticScore();
                score = new BigDecimal(score).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                BidderReviewResultScore newResult = BidderReviewResultScore.builder()
                        .bidderId(bidder.getId())
                        .bidSectionId(bidSectionId)
                        .addScore(score)
                        .gradeId(grade.getId()).build();
                bidderReviewResultScoreService.saveOrUpdateResultScore(newResult);
            }
            Grade build = Grade.builder()
                    .id(grade.getId())
                    .groupEnd(Enabled.YES.getCode())
                    .build();
            boolean status = gradeService.updateGrade(build);
            if (!status) {
                return false;
            }
        }
        expertUserMsgPush(bidSectionId, EvalProcess.DETAILED.getCode(), ExpertUserMsgStatus.GROUP_END);
        return true;
    }

    @Override
    public List<Bidder> listBidderQuoteAppendixScore(Integer bidSectionId) {
        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);
        for (Bidder bidder : bidders) {
            QuoteScoreResultAppendix quoteScoreResultAppendix = quoteScoreResultAppendixService.getQuoteScoreResultAppendix(bidder.getId());
            QuoteScoreResult quoteScoreResult = quoteScoreResultService.getQuoteScoreResultByBidderId(bidder.getId());
            bidder.setQuoteScoreResult(quoteScoreResult);
            bidder.setQuoteScoreResultAppendix(quoteScoreResultAppendix);
        }
        return bidders;
    }

    @Override
    public List<EvalResultEpc> listEpcRankingBidder(Integer bidSectionId) {
        //获取当前标段对应施工的排名表
        List<EvalResultEpc> list = evalResultEpcService.listRankingBidderByBsId(bidSectionId);
        //如果list为null则重新插入数据
        if (CommonUtil.isEmpty(list) || list.size() == 0) {
            //列表为空则重新进行数据封装
            list = evalResultEpcService.addResultByBsId(bidSectionId);
        }
        return list;
    }

    @Override
    public List<Bidder> listQualifiedBidder(Integer bidSectionId, Integer evalFlow) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null !");
        Assert.notNull(evalFlow, "param evalFlow can not be null !");
        List<Bidder> bidders = null;
        EvalProcess code = EvalProcess.getCode(evalFlow);
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        switch (code) {
            case QUALIFICATION:
                bidders = bidderService.listPassBidOpenBidder(bidSectionId);
                break;
            case PRELIMINARY:
                if (BidProtype.EPC.getCode().equals(bidSection.getBidClassifyCode())) {
                    bidders = bidderService.listBidderPassQualifyReview(bidSectionId);
                } else {
                    bidders = bidderService.listPassBidOpenBidder(bidSectionId);
                }
                break;
            case DETAILED:
            case OTHER:
            case CALC_PRICE_SCORE:
                bidders = bidderService.listDetailedBidder(bidSectionId);
                break;
            case RESULT:
                if (BidProtype.QUALIFICATION.getCode().equals(bidSection.getBidClassifyCode())) {
                    List<Bidder> allBidders = preBidEvalService.listPrePassSortBidder(bidSectionId);
                    bidders = new ArrayList<>();
                    for (Bidder bidder : allBidders) {
                        if ("1".equals(bidder.getReviewResult())) {
                            // 通过
                            bidders.add(bidder);
                        }
                    }
                } else {
                    bidders = bidderService.listDetailedBidder(bidSectionId);
                }
                break;
            default:
        }
        return bidders;
    }

    /**
     * 生成复会报告
     *
     * @param bidSectionId 标段主键
     */
    @Override
    public void generateResumptionReport(Integer bidSectionId) {
        // 1、获取项目类型
        BidSection section = bidSectionService.getBidSectionById(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String evaluationMethod = tenderDoc.getEvaluationMethod();
        // 获取当前标段分类所有模板
        List<TemplateNameEnum> tempList = evalReportService.getReportDataMapSubclass(bidSectionId).listResumptionTemplateNameEnum(bidSectionId);
        // 获取所有待合成模板名称
        List<String> temNames = tempList.stream().map(TemplateNameEnum::getName).collect(Collectors.toList());
        for (TemplateNameEnum templateNameEnum : tempList) {
            // 获取数据
            Map<String, Object> map = evalReportService.getTemplateDataMap(bidSectionId, templateNameEnum);
            // 此处存放可重用的数据
            String year = DateTimeUtil.getInternetTime(TimeFormatter.YYYY);
            String month = DateTimeUtil.getInternetTime(TimeFormatter.MM);
            String day = DateTimeUtil.getInternetTime(TimeFormatter.DD);
            map.put("date", new String[]{year, month, day});
            map.put("bidSection", section);
            // 输出路径
            String outPdfPath = FileUtil.getResumptionReportFilePath(String.valueOf(bidSectionId)) + File.separator + templateNameEnum.getName() + ".pdf";
            // 生成复会报告
            ReportPdfUtil.generatePdf(section, map, templateNameEnum.getName(), templateNameEnum.getLevelPrint(), outPdfPath);
        }
        // 合成复会报告
        String dic = FileUtil.getResumptionReportFilePath(String.valueOf(bidSectionId));
        // 输出路径
        String outPutPath = FileUtil.getResumptionReportFilePath(String.valueOf(bidSectionId)) + File.separator + "resumptionReport.pdf";
        String temDic = FileUtil.getResumptionReportFilePath(String.valueOf(bidSectionId));
        // 合成复会报告
        boolean reportStatus = ReportPdfUtil.mergePdf(section, temDic, temNames, evaluationMethod, outPutPath);
        // 上传至fdfs
        String fileName = ProjectFileTypeConstant.RESUMPTION_REPORT + "." + FileType.PDF.getSuffix();
        String customPath = FileUtil.getResumptionReportFilePath(String.valueOf(bidSectionId));
        String reportPath = customPath + File.separator + fileName;
        String reportMark = File.separator + ProjectFileTypeConstant.RESUMPTION_REPORT + File.separator + bidSectionId + File.separator + fileName;
        // 将报告上传到文件服务器
        Boolean aBoolean = fdfsService.uploadProjectFile(ProjectFileTypeConstant.RESUMPTION_REPORT, bidSectionId, new File(reportPath));
        if (aBoolean) {
            Fdfs fdfs = fdfsService.getFdfsByMark(reportMark);
            //更新标段关联表评标报告id
            BidSectionRelate bidSectionRelate = new BidSectionRelate();
            bidSectionRelate.setBidSectionId(bidSectionId);
            bidSectionRelate.setResumptionReportId(fdfs.getId());
            bidSectionRelateService.updateRelateBySectionId(bidSectionRelate);
            // 删除本地复会文件
            FileUtil.removeDir(new File(dic));
        }
    }

    @Override
    public ExpertUserMsgDTO getExpertUserMsg() {
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        String redisKey = CacheName.EXPERT_USER_MSG_ + bidSectionId + "_" + user.getUserId();
        Object msg = RedisUtil.get(redisKey);
        if (CommonUtil.isEmpty(msg)) {
            return null;
        }
        return (ExpertUserMsgDTO) msg;
    }

    @Override
    public void deleteExpertUserMsg() {
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        String redisKey = CacheName.EXPERT_USER_MSG_ + bidSectionId + "_" + user.getUserId();
        RedisUtil.delete(redisKey);
    }

    @Override
    public boolean isFreeBackApplying(Integer bidSectionId) {
        FreeBackApply freeBackApply = freeBackApplyService.getFreeBackApplyByBidSectionId(bidSectionId);
        return !CommonUtil.isEmpty(freeBackApply);
    }

    /**
     * 生成评标报告
     *
     * @param bidSectionId     标段主键
     * @param evaluationMethod 评标办法
     */
    private void generateEvalReport(Integer bidSectionId, String evaluationMethod) throws InterruptedException {
        // 开启缓冲池
//        ExecutorService executorService = Executors.newFixedThreadPool(3);
        AuthUser user = CurrentUserHolder.getUser();
        BidSection section = bidSectionService.getBidSectionById(bidSectionId);
        // 评标报告
        List<TemplateNameEnum> listTemplateNameEnum = evalReportService.getReportDataMapSubclass(bidSectionId).listTemplateNameEnum(bidSectionId);
        // 模板名称
        List<String> temNames = listTemplateNameEnum.stream().map(TemplateNameEnum::getName).collect(Collectors.toList());
        // ===============================将所有模板及状态，存入redis========================================
        List<ReportDto> reportDtos = new ArrayList<>();
        for (TemplateNameEnum temp : listTemplateNameEnum) {
            ReportDto reportDto = ReportDto.builder()
                    .templateName(temp.getTemplateChineseName())
                    .status(0)
                    .build();
            reportDtos.add(reportDto);
        }
        RedisUtil.set(CacheName.REPORT_FLOWS_DTO + bidSectionId, reportDtos);
        // =======================================================================
        //生成单个的报告文件
        for (TemplateNameEnum templateNameEnum : listTemplateNameEnum) {
            assert user != null;
            Map<String, Object> map = evalReportService.getEvalReportData(bidSectionId, templateNameEnum, user.getUserId());
            // 使用多线程
//            executorService.execute(()-> {
            boolean generateStatus = ReportPdfUtil.generatePdf(section, map, templateNameEnum.getName(), templateNameEnum.getLevelPrint(), null);
            // ===============================更新模板中的状态，存入redis========================================
            List<ReportDto> reportDtoList = (List<ReportDto>) RedisUtil.get(CacheName.REPORT_FLOWS_DTO + bidSectionId);
            for (ReportDto reportDto : reportDtoList) {
                // 更新当前合成的模板状态
                if (templateNameEnum.getTemplateChineseName().equals(reportDto.getTemplateName())) {
                    reportDto.setStatus(generateStatus ? 1 : 2);
                    break;
                }
            }
            RedisUtil.set(CacheName.REPORT_FLOWS_DTO + bidSectionId, reportDtoList);
//            });
        }
//        Thread.sleep(5000);
        // 删除进度缓存
        RedisUtil.delete(CacheName.REPORT_FLOWS_DTO + bidSectionId);
        //合成评标报告
        String temDic = FileUtil.getEvalReportFilePath(String.valueOf(bidSectionId));
        // 合成评标报告
        ReportPdfUtil.mergePdf(section, temDic, temNames, evaluationMethod, null);
    }

    /**
     * 生成流标报告
     *
     * @param bidSectionId     标段主键
     * @param evaluationMethod 评标办法
     */
    private void generateCancelTempReport(Integer bidSectionId, String evaluationMethod) throws InterruptedException {
        // 开启缓冲池
//        ExecutorService executorService = Executors.newFixedThreadPool(3);
        AuthUser user = CurrentUserHolder.getUser();
        BidSection section = bidSectionService.getBidSectionById(bidSectionId);
        // 获取流标报告模板
        List<TemplateNameEnum> listTemplateNameEnum = evalReportService.getReportDataMapSubclass(bidSectionId).listCancelTempNameEnum(bidSectionId);
        // 获取所有待合成模板名称
        List<String> temChineseNames = listTemplateNameEnum.stream().map(TemplateNameEnum::getName).collect(Collectors.toList());
        // 模板名称
        List<String> temNames = listTemplateNameEnum.stream().map(TemplateNameEnum::getName).collect(Collectors.toList());
        // ===============================将所有模板及状态，存入redis========================================
        List<ReportDto> reportDtos = new ArrayList<>();
        for (TemplateNameEnum temp : listTemplateNameEnum) {
            ReportDto reportDto = ReportDto.builder()
                    .templateName(temp.getTemplateChineseName())
                    .status(0)
                    .build();
            reportDtos.add(reportDto);
        }
        RedisUtil.set(CacheName.REPORT_FLOWS_DTO + bidSectionId, reportDtos);
        // =======================================================================
        //生成单个的报告文件
        for (TemplateNameEnum templateNameEnum : listTemplateNameEnum) {
            assert user != null;
            Map<String, Object> map = evalReportService.getEvalReportData(bidSectionId, templateNameEnum, user.getUserId());
            // 使用多线程
//            executorService.execute(()-> {
            // 生成报告
            boolean generateStatus = ReportPdfUtil.generatePdf(section, map, templateNameEnum.getName(), templateNameEnum.getLevelPrint(), null);
            // ===============================更新模板中的状态，存入redis========================================
            List<ReportDto> reportDtoList = (List<ReportDto>) RedisUtil.get(CacheName.REPORT_FLOWS_DTO + bidSectionId);
            for (ReportDto reportDto : reportDtoList) {
                // 更新当前合成的模板状态
                if (templateNameEnum.getTemplateChineseName().equals(reportDto.getTemplateName())) {
                    reportDto.setStatus(generateStatus ? 1 : 2);
                    break;
                }
            }
            RedisUtil.set(CacheName.REPORT_FLOWS_DTO + bidSectionId, reportDtoList);
//            });
        }
        // 删除生成报告进度缓存
        RedisUtil.delete(CacheName.REPORT_FLOWS_DTO + bidSectionId);
        String temDic = FileUtil.getEvalReportFilePath(String.valueOf(bidSectionId));
        String outPutPath = FileUtil.getEvalReportFilePath(String.valueOf(bidSectionId)) + File.separator + "evalReport.pdf";
        //合成评标报告
        ReportPdfUtil.mergePdf(section, temDic, temNames, evaluationMethod, outPutPath);
    }

}
