package com.ejiaoyi.common.service.impl;

import com.ejiaoyi.common.dto.GradeDTO;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.*;
import com.ejiaoyi.common.service.IEvalResultEpcService;
import com.ejiaoyi.common.service.IExpertReviewService;
import com.ejiaoyi.common.service.IQuoteScoreResultAppendixService;
import com.ejiaoyi.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 【EPC】获取评标报告模板数据
 * @author fengjunhong
 * @since 2020-11-26
 */
@Service
public class EpcServiceImpl extends ReportServiceImpl{

    @Autowired
    IEvalResultEpcService evalResultEpcService;
    @Autowired
    IQuoteScoreResultAppendixService quoteScoreResultAppendixService;
    @Autowired
    private IExpertReviewService expertReviewService;

    /**
     * 【评标报告】模板
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public List<TemplateNameEnum> listTemplateNameEnum(Integer bidSectionId){
        // 获取标段信息
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        List<TemplateNameEnum> list = new ArrayList<>();
        // 封面
        list.add(TemplateNameEnum.COVER_REPORT);
        // 专家签到表
        list.add(TemplateNameEnum.EXPERT_SING_IN);
        // 专家评标诚信承诺书
        list.add(TemplateNameEnum.EXPERT_PROMISE);
        // 资格审查
        list.add(TemplateNameEnum.QUALIFICATION_REVIEW);
        // 初步评审
        list.add(TemplateNameEnum.PRELIMINARY_EVALUATION);
        // 详细评审明细表
        list.add(TemplateNameEnum.DETAILED_REVIEW);
        // 详细评审个人表
        list.add(TemplateNameEnum.DETAILED_PERSON_REVIEW);
        // 报价得分表
        list.add(TemplateNameEnum.QUOTE_SCORE);
        // 专家修改了报价得分
        if (Enabled.YES.getCode().equals(bidSection.getPriceRecordStatus())){
            // 报价得分修正表
            list.add(TemplateNameEnum.QUOTE_SCORE_UPDATE);
        }
        // 评标得分汇总表
        list.add(TemplateNameEnum.EVALUATION_SCORE);
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
    public List<TemplateNameEnum> listCancelTempNameEnum(Integer bidSectionId) {
        List<TemplateNameEnum> cancelTemps = new ArrayList<>();
        // 封面
        cancelTemps.add(TemplateNameEnum.COVER_REPORT);
        // 专家签到表
        cancelTemps.add(TemplateNameEnum.EXPERT_SING_IN);
        // 专家承若书
        cancelTemps.add(TemplateNameEnum.EXPERT_PROMISE);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        // 不进入资格预审、初步评审（不足三家）
        // EPC项目可在资格审查、初步评审废标
        boolean group1 = super.isGroupCompletion(gradeIds, EvalProcess.QUALIFICATION.getCode());
        boolean group2 = super.isGroupCompletion(gradeIds, EvalProcess.PRELIMINARY.getCode());
        if (group1){
            // 资格审查
            cancelTemps.add(TemplateNameEnum.QUALIFICATION_REVIEW);
        }
        if (group1 && group2){
            // 初步评审表
            cancelTemps.add(TemplateNameEnum.PRELIMINARY_EVALUATION);
        }
        // 评审意见汇总表
        cancelTemps.add(TemplateNameEnum.REVIEW_OPINION);
        // 评审工作履职情况记录表
        cancelTemps.add(TemplateNameEnum.PERFORMANCE_DUTIES_FORM);
        return cancelTemps;
    }

    /**
     * 【复会报告】模板
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public List<TemplateNameEnum> listResumptionTemplateNameEnum(Integer bidSectionId) {
        return super.listResumptionTemplateNameEnum(bidSectionId);
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
                list.add(TemplateNameEnum.EVALUATION_SCORE);
                if (evalProcess.equals(EvalProcess.RESULT.getCode())){
                    break;
                }
            case CALC_PRICE_SCORE:
                list.add(TemplateNameEnum.QUOTE_SCORE);
                if (evalProcess.equals(EvalProcess.CALC_PRICE_SCORE.getCode())){
                    break;
                }
            case DETAILED:
                list.add(TemplateNameEnum.DETAILED_PERSON_REVIEW);
                if (evalProcess.equals(EvalProcess.DETAILED.getCode())){
                    break;
                }
            case PRELIMINARY:
                list.add(TemplateNameEnum.PRELIMINARY_EVALUATION);
                if (evalProcess.equals(EvalProcess.PRELIMINARY.getCode())){
                    break;
                }
            case QUALIFICATION:
                // 资格审查
                list.add(TemplateNameEnum.QUALIFICATION_REVIEW);
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


    public Map<String, Object> getEPCReportData(Integer bidSectionId,TemplateNameEnum templateNameEnum){
        // 获取当前标段，报告所需的所有模板
        switch (templateNameEnum){
            case COVER_REPORT:
                return this.getCoverReportData(bidSectionId);
            case QUALIFICATION_REVIEW:
                return this.getQualificationReviewDataMap(bidSectionId);
            case PRELIMINARY_EVALUATION:
                return this.getFirstStepDataMap(bidSectionId);
            case DETAILED_REVIEW:
                return this.getDetailReviewDataMap(bidSectionId);
            case DETAILED_PERSON_REVIEW:
                return this.getDetailPersonReviewDataMap(bidSectionId);
            case QUOTE_SCORE:
                return this.getQuoteScoreDataMap(bidSectionId);
            case QUOTE_SCORE_UPDATE:
                return this.getQuoteScoreUpdateDataMap(bidSectionId);
            case EVALUATION_SCORE:
                return this.getEvaluationScoreDataMap(bidSectionId);
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
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public Map<String, Object> getCoverReportData(Integer bidSectionId) {
        Map<String,Object> map = new HashMap<>();
        map.putAll(super.getCoverReportData(bidSectionId));
        List<EvalResultEpc> evalResultEpcs = evalResultEpcService.listRankingBidderByBsId(bidSectionId);
        // 按照排名升序排列
        evalResultEpcs = evalResultEpcs.stream().sorted(Comparator.comparing(EvalResultEpc::getOrderNo)).collect(Collectors.toList());
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        // 不进入资格审查（不足三家）
        boolean qualificationStepEnd = this.isGroupCompletion(gradeIds, EvalProcess.QUALIFICATION.getCode());
        // 不进入资格预审、初步评审（不足三家）
        // 通过资格审查
        List<Bidder> passQualifyReviewBidders = bidderService.listBidderPassQualifyReview(bidSectionId);
        map.put("qualificationStepEnd", qualificationStepEnd);
        map.put("passQualifyReviewBidders", passQualifyReviewBidders);
        if (!qualificationStepEnd) {
            return map;
        }
        // 不进入初步评审（不足三家）
        boolean firstStepEnd = this.isGroupCompletion(gradeIds, EvalProcess.PRELIMINARY.getCode());
        // 通过初步评审的投标人
        List<Bidder> passFirstStepBidders = bidderService.listDetailedBidder(bidSectionId);
        map.put("passFirstStepBidders", passFirstStepBidders);
        map.put("firstStepEnd", firstStepEnd);
        map.put("evalResultEpcs", evalResultEpcs);
        return map;
    }

    /**
     * 资格审查
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public Map<String,Object> getQualificationReviewDataMap(Integer bidSectionId){
        Map<String,Object> data = new HashMap<>();
        // 通过开标的投标人列表
        List<Bidder> bidders = bidderService.listPassBidOpenBidder(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        // 评审标准
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.QUALIFICATION.getCode());
        // 评分标准DTO
        List<GradeDTO> gradeDTOList = new ArrayList<>();
        for (Grade grade : grades) {
            //判断评分类型
            gradeDTOList.add(gradeService.getGradeDto(grade.getId(), bidSectionId, bidders));
        }
        for (Bidder bidder : bidders) {
            //合格制度
            String totalResult = "1";
            for (Grade grade : grades) {
                /*评审结果表 合格制*/
                BidderReviewResult brs = bidderReviewResultService.getBidderReviewResult(BidderReviewResult.builder()
                        .bidSectionId(bidSectionId)
                        .bidderId(bidder.getId())
                        .gradeId(Integer.valueOf(grade.getId()))
                        .build());
                if (!CommonUtil.isEmpty(brs) && !totalResult.equals(brs.getResult())){
                    // 当前单位不通过
                    totalResult = "0";
                    break;
                }
            }
            bidder.setTotalResult(totalResult);
        }
        data.put("bidders",bidders);
        data.put("grades",grades);
        data.put("gradeDtos", gradeDTOList);
        return data;
    }

    /**
     * 初步评审
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public Map<String, Object> getFirstStepDataMap(Integer bidSectionId) {
        return super.getFirstStepDataMap(bidSectionId);
    }

    /**
     * 详细评审
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public Map<String, Object> getDetailReviewDataMap(Integer bidSectionId) {
        Map<String,Object> data = new HashMap<>();
        // 进入详细评审的投标人列表
        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        // 详细评审所有评分点
        List<Grade> grades = gradeService.listGradeByReviewType(gradeIds, EvalProcess.DETAILED.getCode(),null);
        // 评分标准DTO
        List<GradeDTO> gradeDTOList = new ArrayList<>();
        for (Grade grade : grades) {
            // 打分制
            GradeDTO gradeDtoScore = gradeService.getGradeDtoScore(grade.getId(), bidSectionId, bidders);
            gradeDTOList.add(gradeDtoScore);
        }
        for (Bidder bidder : bidders) {
            // 获取当前投标人，详细评审得分
            EvalResultEpc evalResultEpc = evalResultEpcService.getEvalResultEpc(bidSectionId, bidder.getId());
            bidder.setTotalResult(evalResultEpc.getDetailedScore());
        }
        data.put("bidders",bidders);
        data.put("grades",grades);
        data.put("gradeDtos", gradeDTOList);
        return data;
    }

    /**
     * 详细评审个人
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public Map<String, Object> getDetailPersonReviewDataMap(Integer bidSectionId) {
        return this.getDetailReviewDataMap(bidSectionId);
    }

    /**
     * 报价得分表
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public Map<String, Object> getQuoteScoreDataMap(Integer bidSectionId) {
        Map<String, Object> map = super.getQuoteScoreDataMap(bidSectionId);
        // 投标人报价得分
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        // 1、未修改价格分
        List<QuoteScoreResult> quoteScoreResultList = quoteScoreResultService.listQuoteScoreResult(bidSectionId);
        for (QuoteScoreResult quoteScoreResult : quoteScoreResultList) {
            // 投标人名称
            quoteScoreResult.setBidderName(bidderService.getBidderById(quoteScoreResult.getBidderId()).getBidderName());
        }
        map.put("quoteScoreResults",quoteScoreResultList);
        return map;
    }

    public Map<String, Object> getQuoteScoreUpdateDataMap(Integer bidSectionId){
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        Map<String, Object> map = super.getQuoteScoreDataMap(bidSectionId);
        // 2、修改价格分
        // 显示附表数据
        List<QuoteScoreResultAppendix> quoteScoreResultAppendices = quoteScoreResultAppendixService.listQuoteScoreResultAppendix(bidSectionId);
        for (QuoteScoreResultAppendix quoteScoreResultAppendix : quoteScoreResultAppendices) {
            // 投标人名称
            quoteScoreResultAppendix.setBidderName(bidderService.getBidderById(quoteScoreResultAppendix.getBidderId()).getBidderName());
        }
        map.put("quoteScoreResults",quoteScoreResultAppendices);
        // 修正原因
        map.put("updateScoreReason",bidSection.getUpdateScoreReason());
        return map;
    }

    /**
     * 评标得分汇总表
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public Map<String, Object> getEvaluationScoreDataMap(Integer bidSectionId) {
        Map<String,Object> map = new HashMap<>();
        // 初步评审的投标人列表
        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);
        // EPC项目简要评标结果
        List<EvalResultEpc> evalResultEpcs = evalResultEpcService.listRankingBidderByBsId(bidSectionId);
        map.put("evalResults",evalResultEpcs);
        map.put("bidders",bidders);
        return map;
    }

    /**
     * 评审意见汇总表
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public Map<String, Object> getReviewOpinionDataMap(Integer bidSectionId) {
        Map<String, Object> data = super.getReviewOpinionDataMap(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        // 不进入资格预审、初步评审（不足三家）
        // EPC项目可在资格审查、初步评审废标
        boolean group1 = super.isGroupCompletion(gradeIds, EvalProcess.QUALIFICATION.getCode());
        boolean group2 = super.isGroupCompletion(gradeIds, EvalProcess.PRELIMINARY.getCode());
        // 是否初步流标
        data.put("preliminaryCancel",group1 && group2);
        return data;
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

    /**
     * 评标结果
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public Map<String, Object> getBidderEvalResultDataMap(Integer bidSectionId) {
        return getEvaluationScoreDataMap(bidSectionId);
    }

    /**
     * 流标环节
     * @param bidSectionId 标段主键
     *
     * @return true：资格审查流标，false：初步评审流标
     */
    private boolean failBidSectionStep(Integer bidSectionId){
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        // 获取初步评审所有评分点
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.PRELIMINARY.getCode());
        // 组长
        ExpertUser expertUser = expertUserService.getChairmanByBidSectionId(bidSectionId);
        for (Grade grade : grades) {
            ExpertReview expertReview = expertReviewService.getExpertReview(ExpertReview.builder()
                    .bidSectionId(expertUser.getBidSectionId())
                    .expertId(expertUser.getId())
                    .gradeId(grade.getId())
                    .enabled(1)
                    .build());
            if (CommonUtil.isEmpty(expertReview)) {
                // 初步评审没有数据，则是在资格审查流标
                return true;
            }
        }
        return false;
    }

}
