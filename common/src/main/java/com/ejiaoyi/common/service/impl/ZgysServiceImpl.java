package com.ejiaoyi.common.service.impl;

import com.ejiaoyi.common.entity.Bidder;
import com.ejiaoyi.common.entity.FreeBackApply;
import com.ejiaoyi.common.entity.TenderDoc;
import com.ejiaoyi.common.enums.EvalProcess;
import com.ejiaoyi.common.enums.TemplateNameEnum;
import com.ejiaoyi.common.service.IPreBidEvalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 【资格预审】获取评标报告模板数据
 *
 * @author fengjunhong
 * @since 2020-11-25
 */
@Service
public class ZgysServiceImpl extends ReportServiceImpl {

    @Autowired
    IPreBidEvalService preBidEvalService;

    /**
     * 【评标报告】模板
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public List<TemplateNameEnum> listTemplateNameEnum(Integer bidSectionId){
        List<TemplateNameEnum> list = super.listTemplateNameEnum(bidSectionId);
        // 详细评审
        list.add(TemplateNameEnum.DETAILED_REVIEW);
        // 资格预审评审汇总表
        list.add(TemplateNameEnum.PREQUALIFICATION_REVIEW_SUMMARY);
        // 评审意见汇总表
        list.add(TemplateNameEnum.REVIEW_OPINION);
        // 评审工作履职情况记录表
        list.add(TemplateNameEnum.PERFORMANCE_DUTIES_FORM);
        return list;
    }

    /**
     * 【流标报告】模板
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public List<TemplateNameEnum> listCancelTempNameEnum(Integer bidSectionId){
        List<TemplateNameEnum> list = new ArrayList<>();
        // 封面
        list.add(TemplateNameEnum.COVER_REPORT);
        // 专家签到表
        list.add(TemplateNameEnum.EXPERT_SING_IN);
        // 专家承若书
        list.add(TemplateNameEnum.EXPERT_PROMISE);
        // 不进入初步评审（不足三家）
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        boolean firstStepEnd = this.isGroupCompletion(gradeIds, EvalProcess.PRELIMINARY.getCode());
        if (firstStepEnd){
            // 初步评审表
            list.add(TemplateNameEnum.PRELIMINARY_EVALUATION);
        }
        // 资格预审项目可在初步评审、详细评审废标
        boolean group1 = super.isGroupCompletion(gradeIds, EvalProcess.PRELIMINARY.getCode());
        boolean group2 = super. isGroupCompletion(gradeIds, EvalProcess.DETAILED.getCode());
        if (group1 && group2){
            // 详细评审表
            list.add(TemplateNameEnum.DETAILED_REVIEW);
        }
        // 评审意见汇总表
        list.add(TemplateNameEnum.REVIEW_OPINION);
        // 评审工作履职情况记录表
        list.add(TemplateNameEnum.PERFORMANCE_DUTIES_FORM);
        return list;
    }

    /**
     * 【复会报告】模板
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public List<TemplateNameEnum> listResumptionTemplateNameEnum(Integer bidSectionId) {
        List<TemplateNameEnum> list = new ArrayList<>();
        // 废标投标人列表
        list.add(TemplateNameEnum.NO_PASS_BIDDERS_FORM);
        return list;
    }

    /**
     * 【回退报告】模板
     * @param freeBackApply 回退申请
     * @return
     */
    @Override
    public List<TemplateNameEnum> listBackTempNameEnum(FreeBackApply freeBackApply) {
        List<TemplateNameEnum> list = new ArrayList<>();
        //回退前环节
        Integer stepNow = freeBackApply.getStepNow();
        // 要回退的环节
        Integer step = freeBackApply.getStep();
        EvalProcess evalProcessNow = EvalProcess.getCode(stepNow);
        EvalProcess evalProcess = EvalProcess.getCode(step);
        switch (evalProcessNow){
            case RESULT:
                list.add(TemplateNameEnum.PRELIMINARY_EVALUATION);
                if (evalProcess.equals(EvalProcess.RESULT.getCode())){
                    break;
                }
            case DETAILED:
                list.add(TemplateNameEnum.PREQUALIFICATION_REVIEW_SUMMARY);
                if (evalProcess.equals(EvalProcess.DETAILED.getCode())){
                    break;
                }
            case PRELIMINARY:
                list.add(TemplateNameEnum.PRELIMINARY_EVALUATION);
            default:
                break;
        }
        // 封面
        list.add(TemplateNameEnum.COVER_REPORT);
        // 让list反转，实现排序功能
        Collections.reverse(list);
        list.add(TemplateNameEnum.REVIEW_PERSON_OPINION);
        return list;
    }


    /**
     * 获取报告，模板数据
     * @param bidSectionId 标段主键
     * @param templateNameEnum 模板
     * @return
     */
    public Map<String, Object> getZgysReportData(Integer bidSectionId,TemplateNameEnum templateNameEnum){
        // 获取当前标段，报告所需的所有模板
        switch (templateNameEnum){
            case COVER_REPORT:
                return this.getCoverReportData(bidSectionId);
            case PRELIMINARY_EVALUATION:
                return this.getFirstStepDataMap(bidSectionId);
            case DETAILED_REVIEW:
                return this.getDetailReviewDataMap(bidSectionId);
            case PREQUALIFICATION_REVIEW_SUMMARY:
                return this.getReviewSummaryDataMap(bidSectionId);
            case REVIEW_OPINION:
            case REVIEW_PERSON_OPINION:
                return this.getReviewOpinionDataMap(bidSectionId);
            case PERFORMANCE_DUTIES_FORM:
                return this.getExpertUsersNoOwner(bidSectionId);
            case NO_PASS_BIDDERS_FORM:
                return this.getNoPassBiddersDataMap(bidSectionId);
            case BID_EVAL_RESULT:
                return this.getBidderEvalResultDataMap(bidSectionId);
            default:
                break;
        }
        return null;
    }

    /**
     * 封面
     *
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public Map<String, Object> getCoverReportData(Integer bidSectionId) {
        Map<String, Object> map = super.getCoverReportData(bidSectionId);
        // 通过资格预审的投标人
        List<Bidder> passEvalBidder = new ArrayList<>();
        // 未通过初步评审的投标人
        List<Bidder> noPassEvalStepBidders = new ArrayList<>();
        List<Bidder> bidders = preBidEvalService.listPrePassSortBidder(bidSectionId);
        for (Bidder bidder : bidders) {
            if ("1".equals(bidder.getReviewResult())){
                // 通过资格预审
                passEvalBidder.add(bidder);
            } else {
                // 未通过
                noPassEvalStepBidders.add(bidder);
            }
        }
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        // 资格预审项目可在初步评审、详细评审废标
        boolean firstStepEnd = this.isGroupCompletion(gradeIds, EvalProcess.PRELIMINARY.getCode());
        boolean detailedStepEnd = this.isGroupCompletion(gradeIds, EvalProcess.DETAILED.getCode());
        map.put("firstStepEnd",firstStepEnd);
        map.put("detailedStepEnd",detailedStepEnd);
        map.put("passEvalBidder",passEvalBidder);
        map.put("noPassEvalStepBidders",noPassEvalStepBidders);
        return map;
    }

    /**
     * 初步评审
     *
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public Map<String, Object> getFirstStepDataMap(Integer bidSectionId) {
        return super.getFirstStepDataMap(bidSectionId);
    }

    /**
     * 详细评审
     *
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public Map<String, Object> getDetailReviewDataMap(Integer bidSectionId) {
        return super.getDetailReviewDataMap(bidSectionId);
    }

    /**
     * 资格预审评审汇总表
     *
     * @param bidSectionId 标段主键
     * @return
     */
    Map<String, Object> getReviewSummaryDataMap(Integer bidSectionId) {
        Map<String, Object> map = new HashMap<>();
        // 评审汇总
        List<Bidder> bidders = preBidEvalService.listPrePassSortBidder(bidSectionId);
        map.put("bidderResults",bidders);
        return map;
    }

    /**
     * 评审意见汇总表
     *
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public Map<String, Object> getReviewOpinionDataMap(Integer bidSectionId) {
        Map<String, Object> map = super.getReviewOpinionDataMap(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        // 资格预审项目可在初步评审、详细评审废标
        boolean firstStepEnd = this.isGroupCompletion(gradeIds, EvalProcess.PRELIMINARY.getCode());
        boolean detailedStepEnd = this.isGroupCompletion(gradeIds, EvalProcess.DETAILED.getCode());
        map.put("firstStepEnd",firstStepEnd);
        map.put("detailedStepEnd",detailedStepEnd);
        return map;
    }

    /**
     * 履职情况表（不包含业主）
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public Map<String, Object> getExpertUsersNoOwner(Integer bidSectionId) {
        return super.getExpertUsersNoOwner(bidSectionId);
    }


    /**
     * 废标投标人列表
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public Map<String, Object> getNoPassBiddersDataMap(Integer bidSectionId) {
        return super.getNoPassBiddersDataMap(bidSectionId);
    }
}
