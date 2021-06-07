package com.ejiaoyi.common.service.impl;

import com.ejiaoyi.common.dto.GradeDTO;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.*;
import com.ejiaoyi.common.service.IEvalResultJlService;
import com.ejiaoyi.common.service.IGradeItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 【监理】获取评标报告模板数据
 * @author fengjunhong
 * @since 2020-11-28
 */
@Service
public class JlServiceImpl extends ReportServiceImpl {

    @Autowired
    IEvalResultJlService evalResultJlService;
    @Autowired
    private IGradeItemService gradeItemService;

    /**
     * 【评标报告】模板
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public List<TemplateNameEnum> listTemplateNameEnum(Integer bidSectionId){
        List<TemplateNameEnum> list = super.listTemplateNameEnum(bidSectionId);
        // 合格投标人名单
        list.add(TemplateNameEnum.QUALIFIED_BIDDERS);
        // 商务评审表
        list.add(TemplateNameEnum.SHANG_WU_EVALUATION_TABLE);
        // 小组 技术标评分明细表
        list.add(TemplateNameEnum.JI_SHU_GROUP_EVALUATION_TABLE);
        // 个人 技术评审表
        list.add(TemplateNameEnum.JI_SHU_PERSON_EVALUATION_TABLE);
        // 得分汇总表
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
    public List<TemplateNameEnum> listCancelTempNameEnum(Integer bidSectionId){
        return super.listCancelTempNameEnum(bidSectionId);
    }

    /**
     * 复会报告
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
    public List<TemplateNameEnum> listBackTempNameEnum(FreeBackApply freeBackApply){
        List<TemplateNameEnum> list = new ArrayList<>();
        //回退前环节
        Integer stepNow = freeBackApply.getStepNow();
        // 要回退的环节
        Integer step = freeBackApply.getStep();
        EvalProcess evalProcessNow = EvalProcess.getCode(stepNow);
        EvalProcess evalProcess = EvalProcess.getCode(step);
        switch (evalProcessNow){
            case DETAILED:
                list.add(TemplateNameEnum.EVALUATION_SCORE);
                list.add(TemplateNameEnum.JI_SHU_PERSON_EVALUATION_TABLE);
                list.add(TemplateNameEnum.SHANG_WU_EVALUATION_TABLE);
                if (evalProcess.equals(EvalProcess.DETAILED.getCode())){
                    break;
                }
            case PRELIMINARY:
                list.add(TemplateNameEnum.QUALIFIED_BIDDERS);
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
     * 获取评标报告，模板数据
     * @param bidSectionId
     * @param templateNameEnum
     * @return
     */
    public Map<String, Object> getJLReportData(Integer bidSectionId,TemplateNameEnum templateNameEnum){
        // 获取当前标段，报告所需的所有模板
        switch (templateNameEnum){
            case COVER_REPORT:
                return this.getCoverReportData(bidSectionId);
            case PRELIMINARY_EVALUATION:
                return this.getFirstStepDataMap(bidSectionId);
            case QUALIFIED_BIDDERS:
                return this.getQuoteScoreDataMap(bidSectionId);
            case SHANG_WU_EVALUATION_TABLE:
                return this.getShangWuReviewDataMap(bidSectionId);
            case JI_SHU_GROUP_EVALUATION_TABLE:
            case JI_SHU_PERSON_EVALUATION_TABLE:
                return this.getJiShuGroupReviewDataMap(bidSectionId);
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

//    /**
//     * 获取流标报告，模板数据
//     * @param bidSectionId
//     * @param templateNameEnum
//     * @return
//     */
//    public Map<String, Object> getJLCancelReportData(Integer bidSectionId,CancelTemplateNameEnum templateNameEnum){
//        // 获取当前标段，报告所需的所有模板
//        switch (templateNameEnum){
//            case COVER_REPORT:
//                return this.getCoverReportData(bidSectionId);
//            case PRELIMINARY_EVALUATION:
//                return this.getFirstStepDataMap(bidSectionId);
//            case REVIEW_OPINION:
//                return this.getReviewOpinionDataMap(bidSectionId);
//            case PERFORMANCE_DUTIES_FORM:
//                return this.getExpertUsersNoOwner(bidSectionId);
//            default:
//                break;
//        }
//        return null;
//    }


    /**
     * 封面
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public Map<String, Object> getCoverReportData(Integer bidSectionId) {
        Map<String,Object> map = new HashMap<>();
        map.putAll(super.getCoverReportData(bidSectionId));
        // 监理项目简要评标结果
        List<EvalResultJl> evalResultJls = evalResultJlService.listRankingBidderByBsId(bidSectionId);
        for (EvalResultJl evalResultJl : evalResultJls) {
            // 名称
            evalResultJl.setBidderName(bidderService.getBidderById(evalResultJl.getBidderId()).getBidderName());
        }
        map.put("evalResultJls",evalResultJls);
        return map;
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
     * 评审意见汇总表
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public Map<String, Object> getReviewOpinionDataMap(Integer bidSectionId) {
        return super.getReviewOpinionDataMap(bidSectionId);
    }

    /**
     * 合格投标人
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public Map<String, Object> getQuoteScoreDataMap(Integer bidSectionId) {
        return super.getQuoteScoreDataMap(bidSectionId);
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
     * 商务评审表
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public Map<String, Object> getShangWuReviewDataMap(Integer bidSectionId) {
        Map<String,Object> data = new HashMap<>();
        // 进入详细评审的投标人列表
        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        // 商务标
        List<Grade> grades = gradeService.listGradeByReviewType(gradeIds, EvalProcess.DETAILED.getCode(), ReviewType.BUSINESS_STANDARD.getCode());
        // 评分标准DTO
        List<GradeDTO> gradeDTOList = new ArrayList<>();
        for (Grade grade : grades) {
            // 打分制
            gradeDTOList.add(gradeService.getGradeDtoScore(grade.getId(), bidSectionId, bidders));
        }
        // 违章行为
        List<Grade> gradesViolation = gradeService.listGradeByReviewType(gradeIds, EvalProcess.DETAILED.getCode(), ReviewType.VIOLATION.getCode());
        for (Grade grade : gradesViolation) {
            // 扣分
            gradeDTOList.add(gradeService.getGradeViolationDto(grade.getId(),bidSectionId,bidders));
        }
        grades.addAll(gradesViolation);
        data.put("bidders",bidders);
        data.put("gradeDtos", gradeDTOList);
        return data;
    }

    /**
     * 技术评审（小组、个人）
     * @param bidSectionId
     * @return
     */
    @Override
    public Map<String, Object> getJiShuGroupReviewDataMap(Integer bidSectionId) {
        Map<String,Object> data = new HashMap<>();
        // 进入详细评审的投标人列表
        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        // 技术标
        List<Grade> grades = gradeService.listGradeByReviewType(gradeIds, EvalProcess.DETAILED.getCode(), ReviewType.TECHNICAL_STANDARD.getCode());
        // 评分标准DTO
        List<GradeDTO> gradeDTOList = new ArrayList<>();
        for (Grade grade : grades) {
            // 打分制
            GradeDTO gradeDtoScore = gradeService.getGradeDtoScore(grade.getId(), bidSectionId, bidders);
            gradeDTOList.add(gradeDtoScore);
        }
        for (Bidder bidder : bidders) {
            // 获取当前投标人，监理项目简要评标结果
            EvalResultJl evalResultJl = evalResultJlService.getEvalResultJl(bidSectionId, bidder.getId());
           if (evalResultJl != null){
               bidder.setTotalResult(evalResultJl.getTechnicalScore());
           }
        }
        data.put("bidders",bidders);
        data.put("grades",grades);
        data.put("gradeDtos", gradeDTOList);
        return data;
    }

    /**
     * 评标得分汇总表
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public Map<String, Object> getEvaluationScoreDataMap(Integer bidSectionId) {
        Map<String,Object> data = new HashMap<>();
        List<EvalResultJl> evalResultJls = evalResultJlService.listRankingBidderByBsId(bidSectionId);
        data.put("evalResultJls",evalResultJls);
        return data;
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

}
