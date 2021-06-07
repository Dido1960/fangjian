package com.ejiaoyi.expert.service.impl;

import com.ejiaoyi.common.constant.EvalResult;
import com.ejiaoyi.common.constant.ScoreType;
import com.ejiaoyi.common.dto.BidderResultDTO;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.*;
import com.ejiaoyi.common.exception.CustomException;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.service.impl.BaseServiceImpl;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.DateTimeUtil;
import com.ejiaoyi.common.util.ThreadUtlis;
import com.ejiaoyi.expert.service.IBidEvalService;
import com.ejiaoyi.expert.service.IPreBidEvalService;
import com.ejiaoyi.expert.support.AuthUser;
import com.ejiaoyi.expert.support.CurrentUserHolder;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 专家评标 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-09-07
 */
@Service
public class PreBidEvalServiceImpl extends BaseServiceImpl implements IPreBidEvalService {

    @Autowired
    private IGradeService gradeService;

    @Autowired
    private ITenderDocService tenderDocService;

    @Autowired
    private IExpertReviewSingleItemService expertReviewSingleItemService;

    @Autowired
    private IBidEvalService bidEvalService;

    @Autowired
    private IBidderService bidderService;

    @Autowired
    private IBidderReviewResultService bidderReviewResultService;

    @Override
    public JsonData validPersonReviewEnd() {
        JsonData result = new JsonData();

        boolean endStatus = false;
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        if (bidEvalService.isFreeBackApplying(bidSectionId)) {
            result.setCode("2");
            result.setMsg("评审回退尚未审核，操作失败!");
            return result;
        }

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        // 获取进入详细评审的投标人
        List<Bidder> bidders = bidEvalService.listBidderPassPreliminary(bidSectionId);

        // 获取详细评审的评标办法
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());
        if (CollectionUtils.isEmpty(grades) || CollectionUtils.isEmpty(bidders)) {
            result.setCode("2");
            result.setMsg("详细评审还有评审项未完成，请检查！");
            return result;
        }
        for (Grade grade : grades) {
            List<ExpertReviewSingleItem> items = expertReviewSingleItemService.listAllHasScore(user.getUserId(), grade.getId());
            endStatus = (grade.getGradeItems().size() * bidders.size()) != items.size();
            if (endStatus) {
                break;
            }
        }

        if (!endStatus){
            result.setCode("1");
        }else {
            result.setCode("2");
            result.setMsg("详细评审还有评审项未完成，请检查！");
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

        // 获取详细评审的评标办法
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());
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
    public List<Bidder> listPrePassSortBidder(Integer bidSectionId) {
        List<Bidder> bidders = bidderService.listPassBidOpenBidder(bidSectionId);

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        // 初步评审项
        List<Grade> preGrades = gradeService.listGrade(gradeIds, EvalProcess.PRELIMINARY.getCode());
        // 详细评审项
        List<Grade> detailGrades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());
        for (Bidder bidder : bidders) {
            // 初步评审结果查询
            Boolean isPreQualified = bidderReviewResultService.listBidderProcessResult(bidSectionId, bidder.getId(), preGrades);
            bidder.setPreReviewResult(isPreQualified ? EvalResult.QUALIFIED : EvalResult.UNQUALIFIED);
            // 详细评审结果查询
            Boolean isDetailQualified = bidderReviewResultService.listBidderProcessResult(bidSectionId, bidder.getId(), detailGrades);
            bidder.setDetailReviewResult(isDetailQualified ? EvalResult.QUALIFIED : EvalResult.UNQUALIFIED);
            // 资格预审评审结果
            bidder.setReviewResult((isPreQualified && isDetailQualified) ? EvalResult.QUALIFIED : EvalResult.UNQUALIFIED);
        }
        return bidders;
    }

}
