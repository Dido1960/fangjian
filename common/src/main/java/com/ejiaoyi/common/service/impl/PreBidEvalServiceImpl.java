package com.ejiaoyi.common.service.impl;

import com.ejiaoyi.common.constant.EvalResult;
import com.ejiaoyi.common.entity.Bidder;
import com.ejiaoyi.common.entity.Grade;
import com.ejiaoyi.common.entity.TenderDoc;
import com.ejiaoyi.common.enums.EvalProcess;
import com.ejiaoyi.common.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 专家评标 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-09-07
 */
@Service("bidEvalService")
public class PreBidEvalServiceImpl extends BaseServiceImpl implements IPreBidEvalService {

    @Autowired
    private IGradeService gradeService;

    @Autowired
    private ITenderDocService tenderDocService;

    @Autowired
    private IBidderService bidderService;

    @Autowired
    private IBidderReviewResultService bidderReviewResultService;


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
