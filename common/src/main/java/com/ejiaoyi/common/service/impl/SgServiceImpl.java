package com.ejiaoyi.common.service.impl;

import com.ejiaoyi.common.dto.GradeDTO;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.EvalProcess;
import com.ejiaoyi.common.enums.Status;
import com.ejiaoyi.common.enums.TemplateNameEnum;
import com.ejiaoyi.common.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 【施工】获取评标报告模板数据
 * @author fengjunhong
 * @since 2020-11-25
 */
@Service
public class  SgServiceImpl extends ReportServiceImpl {

    @Autowired
    IBidderService bidderService;
    @Autowired
    IBidSectionService bidSectionService;
    @Autowired
    ITenderDocService tenderDocService;
    @Autowired
    IBidSectionRelateService bidSectionRelateService;
    @Autowired
    IEvalResultSgService evalResultSgService;
    @Autowired
    IRegService regService;
    @Autowired
    IBidderOpenInfoService bidderOpenInfoService;
    @Autowired
    IGradeService gradeService;
    @Autowired
    IExpertReviewSingleItemService expertReviewSingleItemService;
    @Autowired
    private IBidderQuantityScoreService quantityScoreService;

    /**
     * 【评标报告】模板
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public List<TemplateNameEnum> listTemplateNameEnum(Integer bidSectionId){
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        // 招标文件信息
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        List<TemplateNameEnum> list = super.listTemplateNameEnum(bidSectionId);
        // 详细评审
        list.add(TemplateNameEnum.DETAILED_REVIEW);
        // 详细评审专家个人表
        list.add(TemplateNameEnum.DETAILED_PERSON_REVIEW);
        // 报价得分表
        list.add(TemplateNameEnum.QUOTE_SCORE);
        if (Status.PROCESSING.getCode().equals(tenderDoc.getMutualSecurityStatus())){
            // 开启了互保共建
            list.add(TemplateNameEnum.MUTUAL_PROTECTION);
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
    public List<TemplateNameEnum> listCancelTempNameEnum(Integer bidSectionId){
        return super.listTemplateNameEnum(bidSectionId);
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
            case OTHER:
            case RESULT:
                list.add(TemplateNameEnum.EVALUATION_SCORE);
                if (evalProcess.equals(EvalProcess.OTHER.getCode()) || evalProcess.equals(EvalProcess.RESULT.getCode())){
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
    public Map<String, Object> getSgReportData(Integer bidSectionId,TemplateNameEnum templateNameEnum){
        // 获取当前标段，报告所需的所有模板
        switch (templateNameEnum){
            case COVER_REPORT:
                return this.getCoverReportData(bidSectionId);
            case PRELIMINARY_EVALUATION:
                return this.getFirstStepDataMap(bidSectionId);
            case DETAILED_REVIEW:
                return this.getDetailReviewDataMap(bidSectionId);
            case DETAILED_PERSON_REVIEW:
                return this.getDetailPersonReviewDataMap(bidSectionId);
            case QUOTE_SCORE:
                return this.getQuoteScoreDataMap(bidSectionId);
            case MUTUAL_PROTECTION:
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
        // 施工项目简要评标结果
        List<EvalResultSg> evalResultSgs = evalResultSgService.listRankingBidderByBsId(bidSectionId);
        for (EvalResultSg evalResultSg : evalResultSgs) {
            // 名称
            evalResultSg.setBidderName(bidderService.getBidderById(evalResultSg.getBidderId()).getBidderName());
        }
        map.put("evalResultSgs",evalResultSgs);
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
     * 详细评审
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public Map<String, Object> getDetailReviewDataMap(Integer bidSectionId) {
        // 施工能力扣分表、施工组织设计扣分表、安全质量事故扣分表、建筑市场不良记录扣分表
        Map<String,Object> map = new HashMap<>();
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        // 详细评审,评审标准
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());
        // 通过初步评审的投标人列表
        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);
        // 评分标准DTO
        List<GradeDTO> gradeDTOList = new ArrayList<>();
        for (Grade grade : grades) {
            // 打分制
            gradeDTOList.add(gradeService.getGradeDtoDeduct(grade.getId(), bidSectionId, bidders));
        }
        // 施工项目简要评标结果
        List<EvalResultSg> evalResultSgs = evalResultSgService.listRankingBidderByBsId(bidSectionId);
        map.put("evalResultSgs",evalResultSgs);
        map.put("gradeDtos", gradeDTOList);
        map.put("bidders",bidders);
        return map;
    }

    /**
     * 详细评审专家个人打分表
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public Map<String, Object> getDetailPersonReviewDataMap(Integer bidSectionId) {
        // 施工能力扣分表、施工组织设计扣分表、安全质量事故扣分表、建筑市场不良记录扣分表
        Map<String,Object> map = new HashMap<>();
        // 初步评审的投标人列表
        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        // 详细评审,评审标准
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());
        // 评分标准DTO
        List<GradeDTO> gradeDTOList = new ArrayList<>();
        for (Grade grade : grades) {
            //判断评分类型
            gradeDTOList.add(gradeService.getGradeDtoDeduct(grade.getId(), bidSectionId, bidders));
        }
        // 施工项目简要评标结果
        List<EvalResultSg> evalResultSgs = evalResultSgService.listRankingBidderByBsId(bidSectionId);
        map.put("evalResultSgs",evalResultSgs);
        // 施工详细评审，个人评审只显示前两个grade
        map.put("gradeDtos", gradeDTOList.subList(0,2));
        map.put("bidders",bidders);

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
     * 针对回退审核
     * @param bidSectionId
     * @return
     */
    public Map<String, Object> getBackDetailPersonReviewDataMap(Integer bidSectionId) {
        // 施工能力扣分表、施工组织设计扣分表、安全质量事故扣分表、建筑市场不良记录扣分表
        Map<String,Object> map = new HashMap<>();
        // 初步评审的投标人列表
        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        // 详细评审,评审标准
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());
        // 评分标准DTO
        List<GradeDTO> gradeDTOList = new ArrayList<>();
        for (Grade grade : grades) {
            //判断评分类型
            gradeDTOList.add(gradeService.getGradeDtoDeduct(grade.getId(), bidSectionId, bidders));
        }
        // 施工项目简要评标结果
        List<EvalResultSg> evalResultSgs = evalResultSgService.listRankingBidderByBsId(bidSectionId);
        map.put("evalResultSgs",evalResultSgs);
        // 施工详细评审，个人评审只显示前两个grade
        map.put("gradeDtos", gradeDTOList);
        map.put("bidders",bidders);
        return map;
    }

    /**
     * 报价得分表
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public Map<String, Object> getQuoteScoreDataMap(Integer bidSectionId) {
//        List<String> quoteStrList = new ArrayList<>();
//        quoteStrList.add("分部分项工程量清单报价");
//        quoteStrList.add("措施项目清单报价1");
//        quoteStrList.add("措施项目清单报价2");
//        quoteStrList.add("总承包服务费");
//        quoteStrList.add("规费清单报价");
//        quoteStrList.add("税金清单报价");
//        quoteStrList.add("综合单价");
//        quoteStrList.add("主要材料设备单价");
//        quoteStrList.add("报价总分");
        Map<String, Object> quoteScoreDataMap = super.getQuoteScoreDataMap(bidSectionId);
//        quoteScoreDataMap.put("quoteList",quoteStrList);

        List<BidderQuantityScore> bidders = quantityScoreService.listBidderQuantityScoreByBidSectionId(bidSectionId);
        quoteScoreDataMap.put("bidders",bidders);

        return quoteScoreDataMap;
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
        // 施工项目简要评标结果
        List<EvalResultSg> evalResultSgs = evalResultSgService.listRankingBidderByBsId(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        map.put("tenderDoc",tenderDoc);
        map.put("evalResultSgs",evalResultSgs);
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
        return super.getReviewOpinionDataMap(bidSectionId);
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
