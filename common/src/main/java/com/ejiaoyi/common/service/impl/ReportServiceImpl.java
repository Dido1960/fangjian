package com.ejiaoyi.common.service.impl;

import com.ejiaoyi.common.dto.*;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.*;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.DateTimeUtil;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评标报告模板获取数据
 * @author fengjunhong
 * @since 2020-11-26
 */
@Service
public class ReportServiceImpl implements IReportService {

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
    IExpertUserService expertUserService;
    @Autowired
    IGradeService gradeService;
    @Autowired
    IBidderReviewResultService bidderReviewResultService;
    @Autowired
    IExpertReviewSingleItemService expertReviewSingleItemService;
    @Autowired
    IQuoteScoreResultService quoteScoreResultService;

    @Override
    public List<TemplateNameEnum> listTemplateNameEnum(Integer bidSectionId) {
        List<TemplateNameEnum> list = new ArrayList<>();
        // 封面
        list.add(TemplateNameEnum.COVER_REPORT);
        // 专家签到表
        list.add(TemplateNameEnum.EXPERT_SING_IN);
        // 专家承若书
        list.add(TemplateNameEnum.EXPERT_PROMISE);
        // 初步评审表
        list.add(TemplateNameEnum.PRELIMINARY_EVALUATION);
        return list;
    }

    @Override
    public List<TemplateNameEnum> listCancelTempNameEnum(Integer bidSectionId) {
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
        // 评审意见汇总表
        list.add(TemplateNameEnum.REVIEW_OPINION);
        // 评审工作履职情况记录表
        list.add(TemplateNameEnum.PERFORMANCE_DUTIES_FORM);
        return list;
    }

    @Override
    public List<TemplateNameEnum> listBackTempNameEnum(FreeBackApply freeBackApply) {
        return null;
    }

    @Override
    public Map<String, Object> getCoverReportData(Integer bidSectionId) {
        Map<String,Object> map = new HashMap<>();
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        // 标段关联信息
        BidSectionRelate bidSectionRelate = bidSectionRelateService.getBidSectionRelateByBSId(bidSectionId);
        // 区划
        Reg reg = regService.getRegById(bidSectionRelate.getRegId());
        Map<String, String> openTime = DateTimeUtil.splitTimeStr(tenderDoc.getBidOpenTime());
        // 获取当前标段的专家组长
        ExpertUser expertChairman = expertUserService.getChairmanByBidSectionId(bidSectionId);
        // 获取标段信息
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        List<Bidder> allBidders = new ArrayList<>();
        if (Status.PROCESSING.getCode().equals(bidSection.getBidOpenOnline())){
            // 网上开标，获取文件上传成功所有投标人
            allBidders = bidderService.listFileUploadSuccessBidder(bidSectionId);
        } else {
            // 现场开标，获取解密成功所有投标人
            allBidders = bidderService.listOfflineDecrySuccessBidder(bidSectionId);
        }
        // 通过开标的投标人
        List<Bidder> passBidOpenBidders = bidderService.listPassBidOpenBidder(bidSectionId);
        // 通过初步评审的投标人
        List<Bidder> passFirstStepBidders = bidderService.listDetailedBidder(bidSectionId);
        // 不进入初步评审（不足三家）
        boolean firstStepEnd = this.isGroupCompletion(gradeIds, EvalProcess.PRELIMINARY.getCode());
        // 未通过初步评审的投标人
        List<Bidder> noPassFirstStepBidders = bidderService.listNoPassFirstStepBidder(bidSectionId);
        map.put("openTime",openTime);
        map.put("reg",reg);
        map.put("expertChairman",expertChairman);
        map.put("allBidders",allBidders);
        map.put("passBidOpenBidders", passBidOpenBidders);
        map.put("tenderDoc",tenderDoc);
        map.put("passFirstStepBidders",passFirstStepBidders);
        map.put("noPassFirstStepBidders",noPassFirstStepBidders);
        map.put("firstStepEnd",firstStepEnd);
        map.put("bidSection",bidSection);
        return map;
    }

    @Override
    public Map<String, Object> getQualificationReviewDataMap(Integer bidSectionId) {
        return null;
    }

    @Override
    public Map<String, Object> getFirstStepDataMap(Integer bidSectionId) {
        Map<String,Object> data = new HashMap<>();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        // 初步评审的投标人列表
        List<Bidder> bidders;
        if (BidProtype.EPC.getCode().equals(bidSection.getBidClassifyCode())){
            //施工总承包 获取通过资格审查的投标人
            bidders = bidderService.listBidderPassQualifyReview(bidSectionId);
        }else {
            //获取通过开标的投标人
            bidders = bidderService.listPassBidOpenBidder(bidSectionId);
        }
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        // 评审标准
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.PRELIMINARY.getCode());
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

    @Override
    public Map<String, Object> getDetailReviewDataMap(Integer bidSectionId) {
        Map<String,Object> data = new HashMap<>();
        // 进入详细评审的投标人列表
        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        // 评审标准
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());
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

    @Override
    public Map<String, Object> getDetailPersonReviewDataMap(Integer bidSectionId) {
        return null;
    }

    @Override
    public Map<String, Object> getQuoteScoreDataMap(Integer bidSectionId) {
        Map<String,Object> data = new HashMap<>();
        // 合格投标人列表
        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);
        data.put("qualifiedBidders",bidders);
        return data;
    }

    /**
     * 评标得分汇总表
     * @param bidSectionId ..
     * @return
     */
    @Override
    public Map<String, Object> getEvaluationScoreDataMap(Integer bidSectionId) {
        return null;
    }

    @Override
    public Map<String, Object> getReviewOpinionDataMap(Integer bidSectionId) {
        Map<String, Object> map = new HashMap<>();
        // 存储每个投标人所有专家的评审意见
        List<BidderDTO> bidderDTOList = new ArrayList<>();
        //获取评审项
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String gradeId = tenderDoc.getGradeId();
        String[] gradeIds = gradeId.split(",");
        //获取所有评审意见
        List<ExpertReviewSingleItem> singleItems = expertReviewSingleItemService.listExpertReviewSingleItem(gradeIds);
        // 按照评审环节排序
        singleItems = expertReviewSingleItemService.sortExpertReviewSingleByStep(bidSectionId,singleItems);
        for (ExpertReviewSingleItem item : singleItems) {
            BidderDTO bDTO = (BidderDTO) map.get(item.getBidderId().toString());
            if (bDTO != null) {
                /**
                 * 同一家投标人，所有专家的评审意见
                 */
                //获取该投标人所有评分点集合
                Map<Integer, GradeItemDTO> gradeItemMap = bDTO.getGradeItemMap();
                GradeItemDTO itemDto = gradeItemMap.get(item.getGradeItemId());
                if (itemDto == null) {
                    List<ExpertReviewDetailDTO> detailDtoList = new ArrayList<>();
                    itemDto = new GradeItemDTO();
                    // 获取当前grade的评审类型、评审环节
                    Grade grade = gradeService.getGradeById(item.getGradeId());
                    if (!CommonUtil.isEmpty(grade)){
                        itemDto.setReviewProcess(EvalProcess.getRemark(grade.getReviewProcess()));
                        // 评审类型
                        if (!CommonUtil.isEmpty(grade.getReviewType())){
                            itemDto.setReviewType(ReviewType.getCode(grade.getReviewType()).getTextName());
                        }

                    }
                    itemDto.setGradeItemId(item.getGradeItemId());
                    itemDto.setGradeItemName(item.getItemContent());
                    itemDto.setExpertReviewDetailDTOS(detailDtoList);
                    gradeItemMap.put(item.getGradeItemId(), itemDto);
                }
                // 评标意见
                List<ExpertReviewDetailDTO> detailDto = itemDto.getExpertReviewDetailDTOS();
                ExpertReviewDetailDTO expertReviewDetailDTO = new ExpertReviewDetailDTO();
                expertReviewDetailDTO.setExpertId(item.getExpertId());
                expertReviewDetailDTO.setExpertName(item.getExpertName());
                expertReviewDetailDTO.setEvalComments(item.getEvalComments());
                detailDto.add(expertReviewDetailDTO);

            } else {
                // 评标意见
                List<ExpertReviewDetailDTO> detailDtoList = new ArrayList<>();
                ExpertReviewDetailDTO expertReviewDetailDTO = new ExpertReviewDetailDTO();
                expertReviewDetailDTO.setExpertId(item.getExpertId());
                expertReviewDetailDTO.setExpertName(item.getExpertName());
                expertReviewDetailDTO.setEvalComments(item.getEvalComments());
                detailDtoList.add(expertReviewDetailDTO);

                Map<Integer, GradeItemDTO> gradeItemMap = new HashMap<>();
                GradeItemDTO itemDto = new GradeItemDTO();
                // 获取当前grade的评审类型、评审环节
                Grade grade = gradeService.getGradeById(item.getGradeId());
                if (!CommonUtil.isEmpty(grade)){
                    itemDto.setReviewProcess(EvalProcess.getRemark(grade.getReviewProcess()));
                    // 评审类型
                    if (!CommonUtil.isEmpty(grade.getReviewType())){
                        itemDto.setReviewType(ReviewType.getCode(grade.getReviewType()).getTextName());
                    }
                }
                itemDto.setGradeItemId(item.getGradeItemId());
                itemDto.setGradeItemName(item.getItemContent());
                itemDto.setExpertReviewDetailDTOS(detailDtoList);
                gradeItemMap.put(item.getGradeItemId(), itemDto);

                BidderDTO bidderDTO = new BidderDTO();
                bidderDTO.setBidderId(item.getBidderId());
                bidderDTO.setBidderName(item.getBidderName());
                bidderDTO.setGradeItemMap(gradeItemMap);
                map.put(item.getBidderId().toString(), bidderDTO);
                // 存储评审意见
                bidderDTOList.add(bidderDTO);
            }

        }
        Map<String, Object> data = new HashMap<>();
        // 不进入初步评审（不足三家）
        boolean firstStepEnd = this.isGroupCompletion(gradeIds, EvalProcess.PRELIMINARY.getCode());
        data.put("firstStepEnd",firstStepEnd);
        data.put("bidders",bidderDTOList);
        return data;
    }

    @Override
    public Map<String, Object> getExpertUsersNoOwner(Integer bidSectionId) {
        Map<String, Object> data = new HashMap<>();
        List<ExpertUser> list = expertUserService.listExpertsByBidSectionId(bidSectionId);
        list.removeIf(expertUser -> expertUser.getCategory() == 3);
        data.put("expertUsers", list);
        return data;
    }

    @Override
    public Map<String, Object> getShangWuReviewDataMap(Integer bidSectionId) {
        return null;
    }

    @Override
    public Map<String, Object> getJiShuGroupReviewDataMap(Integer bidSectionId) {
        return null;
    }


    /**
     * 复会报告
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public List<TemplateNameEnum> listResumptionTemplateNameEnum(Integer bidSectionId) {
        List<TemplateNameEnum> list = new ArrayList<>();
        // 废标投标人列表
        list.add(TemplateNameEnum.NO_PASS_BIDDERS_FORM);
        // 评标结果 （投标人报价得分表）
        list.add(TemplateNameEnum.BID_EVAL_RESULT);
        return list;
    }

    @Override
    public Map<String, Object> getNoPassBiddersDataMap(Integer bidSectionId) {
        Map<String, Object> map = new HashMap<>();
        // 存储每个投标人所有专家的评审意见
        List<BidderDTO> bidderDTOList = new ArrayList<>();
        //获取评审项
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String gradeId = tenderDoc.getGradeId();
        String[] gradeIds = gradeId.split(",");
        //获取所有评审意见
        List<ExpertReviewSingleItem> singleItems = expertReviewSingleItemService.listExpertReviewSingleItem(gradeIds);
        // 筛选废标意见
        singleItems = expertReviewSingleItemService.scrapBiddersResult(bidSectionId,singleItems);
        for (ExpertReviewSingleItem item : singleItems) {
            BidderDTO bDTO = (BidderDTO) map.get(item.getBidderId().toString());
            if (bDTO != null) {
                /**
                 * 同一家投标人，所有专家的评审意见
                 */
                //获取该投标人所有评分点集合
                Map<Integer, GradeItemDTO> gradeItemMap = bDTO.getGradeItemMap();
                GradeItemDTO itemDto = gradeItemMap.get(item.getGradeItemId());
                if (itemDto == null) {
                    List<ExpertReviewDetailDTO> detailDtoList = new ArrayList<>();
                    itemDto = new GradeItemDTO();
                    // 获取当前grade的评审类型、评审环节
                    Grade grade = gradeService.getGradeById(item.getGradeId());
                    if (!CommonUtil.isEmpty(grade)){
                        itemDto.setReviewProcess(EvalProcess.getRemark(grade.getReviewProcess()));
                        // 评审类型
                        if (grade.getReviewType() != null) {
                            itemDto.setReviewType(ReviewType.getCode(grade.getReviewType()).getTextName());
                        }
                    }
                    itemDto.setGradeItemId(item.getGradeItemId());
                    itemDto.setGradeItemName(item.getItemContent());
                    itemDto.setExpertReviewDetailDTOS(detailDtoList);
                    gradeItemMap.put(item.getGradeItemId(), itemDto);
                }
                // 评标意见
                List<ExpertReviewDetailDTO> detailDto = itemDto.getExpertReviewDetailDTOS();
                ExpertReviewDetailDTO expertReviewDetailDTO = new ExpertReviewDetailDTO();
                expertReviewDetailDTO.setExpertId(item.getExpertId());
                expertReviewDetailDTO.setExpertName(item.getExpertName());
                expertReviewDetailDTO.setEvalComments(item.getEvalComments());
                detailDto.add(expertReviewDetailDTO);
            } else {
                // 评标意见
                List<ExpertReviewDetailDTO> detailDtoList = new ArrayList<>();
                ExpertReviewDetailDTO expertReviewDetailDTO = new ExpertReviewDetailDTO();
                expertReviewDetailDTO.setExpertId(item.getExpertId());
                expertReviewDetailDTO.setExpertName(item.getExpertName());
                expertReviewDetailDTO.setEvalComments(item.getEvalComments());
                detailDtoList.add(expertReviewDetailDTO);

                Map<Integer, GradeItemDTO> gradeItemMap = new HashMap<>();
                GradeItemDTO itemDto = new GradeItemDTO();
                // 获取当前grade的评审类型、评审环节
                Grade grade = gradeService.getGradeById(item.getGradeId());
                if (!CommonUtil.isEmpty(grade)){
                    itemDto.setReviewProcess(EvalProcess.getRemark(grade.getReviewProcess()));
                    // 评审类型
                    if (!CommonUtil.isEmpty(grade.getReviewType())){
                        itemDto.setReviewType(ReviewType.getCode(grade.getReviewType()).getTextName());
                    }
                }
                itemDto.setGradeItemId(item.getGradeItemId());
                itemDto.setGradeItemName(item.getItemContent());
                itemDto.setExpertReviewDetailDTOS(detailDtoList);
                gradeItemMap.put(item.getGradeItemId(), itemDto);

                BidderDTO bidderDTO = new BidderDTO();
                bidderDTO.setBidderId(item.getBidderId());
                bidderDTO.setBidderName(item.getBidderName());
                bidderDTO.setGradeItemMap(gradeItemMap);
                map.put(item.getBidderId().toString(), bidderDTO);
                // 存储评审意见
                bidderDTOList.add(bidderDTO);
            }

        }
        Map<String, Object> data = new HashMap<>();
        data.put("bidders",bidderDTOList);
        return data;
    }

    @Override
    public Map<String, Object> getBidderEvalResultDataMap(Integer bidSectionId) {
        return getDetailReviewDataMap(bidSectionId);
    }

    /**
     * 判断当前环节是否结束
     * @param gradeIds 所有评分项
     * @param evalProcess 环节
     * @return
     */
    public boolean isGroupCompletion(String[] gradeIds, Integer evalProcess){
        ProcessCompletionDTO dto = gradeService.getProcessCompletion(gradeIds, evalProcess);
        if (dto == null) {
            return false;
        }
        return dto.getCompleteNum() != null && dto.getCompleteNum() != 0 && (dto.getNoCompleteNum() == null || dto.getNoCompleteNum() == 0);
    }

}
