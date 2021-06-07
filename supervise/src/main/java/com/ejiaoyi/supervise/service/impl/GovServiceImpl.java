package com.ejiaoyi.supervise.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.common.constant.EvalStatus;
import com.ejiaoyi.common.constant.ProjectFileTypeConstant;
import com.ejiaoyi.common.dto.BidOpenFlowSituationDTO;
import com.ejiaoyi.common.dto.ProcessCompletionDTO;
import com.ejiaoyi.common.entity.*;

import com.ejiaoyi.common.enums.*;
import com.ejiaoyi.common.mapper.BidSectionMapper;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.service.impl.BaseServiceImpl;
import com.ejiaoyi.common.service.impl.GradeServiceImpl;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.supervise.service.IGovService;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 主管部门服务实现层
 *
 * @author Make
 * @date 2020/10/8 15:24
 */
@Service
public class GovServiceImpl extends BaseServiceImpl implements IGovService {
    @Autowired
    private BidSectionMapper bidSectionMapper;
    @Autowired
    private IBidSectionService bidSectionService;
    @Autowired
    private ITenderDocService tenderDocService;
    @Autowired
    private ILineStatusService lineStatusService;
    @Autowired
    private IBidderService bidderService;
    @Autowired
    private IBidderReviewResultService bidderReviewResultService;
    @Autowired
    private IFDFSService fdfsService;
    @Autowired
    private GradeServiceImpl gradeService;

    @Override
    public String listBidSection(BidSection bidSection) {
        Page page = this.getPageForLayUI();
        List<BidSection> bidSections = bidSectionMapper.listBidSection(page, bidSection, null);
        return this.initJsonForLayUI(bidSections, (int) page.getTotal());
    }

    @Override
    public Integer getProjectTotal(BidSection bidSection) {
        return bidSectionMapper.listBidSection(bidSection, null).size();
    }

    @Override
    public List<Bidder> listBiddersWithVeto(Integer bidSectionId) {
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        List<Bidder> bidders = bidderService.listAllBidders(bidSectionId, false);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        for (Bidder bidder : bidders) {
            bidder.setVetoFlow(0);
            if (bidder.getIsPassBidOpen() != 1) {
                bidder.setVetoFlow(1);
                BidderOpenInfo boi = bidder.getBidderOpenInfo();

                //标书拒绝
                if (!CommonUtil.isEmpty(boi.getTenderRejection()) && boi.getTenderRejection() == 1) {
                    bidder.setVetoReason("标书拒绝");
                    continue;
                }
                // 未签到原因
                if (boi.getNotCheckin() == 1) {
                    bidder.setVetoReason("迟到");
                    continue;
                }
                if (boi.getNotCheckin() == 2) {
                    bidder.setVetoReason("弃标");
                    continue;
                }
                if (boi.getNotCheckin() == 9) {
                    String vetoReason = "未递交";
                    if (!CommonUtil.isEmpty(boi.getNotCheckinReason())) {
                        vetoReason += "," + boi.getNotCheckinReason();
                    }
                    bidder.setVetoReason(vetoReason);
                    continue;
                }

                Integer authentication = boi.getAuthentication();
                Integer urgentSigin = boi.getUrgentSigin();
                boolean status = (authentication != null && authentication == 1) || (urgentSigin != null && urgentSigin == 1);
                if (!status) {
                    String vetoReason = "未完成签到";
                    bidder.setVetoReason(vetoReason);
                    continue;
                }
                bidder.setVetoReason("其它");
                continue;
            }

            //非纸质标
            if (!Enabled.YES.getCode().toString().equals(bidSection.getPaperEval())){
                String gradeIdStr = tenderDoc.getGradeId();
                String[] gradeIds = gradeIdStr.split(",");
                for (String gradeId : gradeIds) {
                    BidderReviewResult bidderReviewResult = bidderReviewResultService.getBidderReviewResult(BidderReviewResult.builder()
                            .bidSectionId(bidSectionId)
                            .bidderId(bidder.getId())
                            .gradeId(Integer.valueOf(gradeId))
                            .build());
                    if (bidderReviewResult != null && QualifiedType.UNQUALIFIED.getCode().equals(bidderReviewResult.getResult())) {
                        bidder.setVetoFlow(2);
                        bidder.setVetoReason("详见评标流程");
                        break;
                    }
                }
            }
        }
        return bidders;
    }

    @Override
    public List<BidOpenFlowSituationDTO> listBidOpenFlowSituation(Integer bidSectionId) {
        lineStatusService.updateFileUploadOrsigninStatus(bidSectionId);
        LineStatus lineStatus = lineStatusService.getLineStatusBySectionId(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        List<BidOpenFlowSituationDTO> list = new ArrayList<>();
        // 签到信息
        list.add(this.signInfoStatus(lineStatus.getSigninStatus()));
        // 身份检查
        list.add(this.getSingleStatus(BidOpenProcessGov.IDENTITY_CHECK.getProcessName(), lineStatus.getBidderCheckStatus()));
        // 浮动点抽取
        list.add(this.getSingleStatus(BidOpenProcessGov.FLOAT_POINT.getProcessName(), tenderDoc.getFloatPoint(), lineStatus.getBidderCheckStatus()));
        // 标书解密
        list.add(this.getSingleStatus(BidOpenProcessGov.BIDDER_FILE_DECRYPT.getProcessName(), lineStatus.getDecryptionStatus()));
        // 招标控制价
        list.add(this.getSingleStatus(BidOpenProcessGov.BID_CONTROL_PRICE.getProcessName(), tenderDoc.getControlPrice(), lineStatus.getDecryptionStatus()));
        // 开标记录表
        BidOpenFlowSituationDTO dto = BidOpenFlowSituationDTO.builder()
                .flowName(BidOpenProcessGov.BID_OPEN_RECORD.getProcessName())
                .flowStatus(Status.NOT_START.getCode())
                .build();
        String mark = File.separator + ProjectFileTypeConstant.BID_OPEN_RECORD + File.separator + bidSectionId + File.separator + ProjectFileTypeConstant.BID_OPEN_RECORD + ".pdf";
        if (StringUtil.isNotEmpty(fdfsService.getUrlByMark(mark))) {
            dto.setFlowStatus(Status.END.getCode());
        }
        list.add(dto);
        return list;
    }

    /**
     * 签到 流程判断
     *
     * @param signStatus 签到状态
     * @return 对应 Status 0：未开始 1：进行中 2：已完成
     */
    private BidOpenFlowSituationDTO signInfoStatus(Integer signStatus) {
        BidOpenFlowSituationDTO bidOpenFlowSituationDTO = BidOpenFlowSituationDTO.builder()
                .flowName(BidOpenProcessGov.SIGN_INFO.getProcessName())
                .flowStatus(Status.NOT_START.getCode())
                .build();
        if (Status.PROCESSING.getCode().equals(signStatus)) {
            bidOpenFlowSituationDTO.setFlowStatus(Status.PROCESSING.getCode());
        }
        if (Status.END.getCode().equals(signStatus)) {
            bidOpenFlowSituationDTO.setFlowStatus(Status.END.getCode());
        }
        return bidOpenFlowSituationDTO;
    }

    /**
     * 单个开标流程 流程判断
     *
     * @param flowName 流程名称
     * @param status   状态
     * @return 对应 Status 0：未开始 1：进行中 2：已完成
     */
    private BidOpenFlowSituationDTO getSingleStatus(String flowName, Integer status) {
        BidOpenFlowSituationDTO bidOpenFlowSituationDTO = BidOpenFlowSituationDTO.builder()
                .flowName(flowName)
                .flowStatus(Status.NOT_START.getCode())
                .build();
        if (Status.PROCESSING.getCode().equals(status)) {
            bidOpenFlowSituationDTO.setFlowStatus(status);
        }
        if (Status.END.getCode().equals(status)) {
            bidOpenFlowSituationDTO.setFlowStatus(status);
        }
        return bidOpenFlowSituationDTO;
    }

    /**
     * 单个开标流程 流程判断
     *
     * @param flowName     流程名称
     * @param floatControl 浮动点或控制价抽取结果
     * @param lastProcess  上个环节完成情况
     * @return 对应 Status 0：未开始 1：进行中 2：已完成
     */
    private BidOpenFlowSituationDTO getSingleStatus(String flowName, String floatControl, Integer lastProcess) {
        BidOpenFlowSituationDTO bidOpenFlowSituationDTO = BidOpenFlowSituationDTO.builder()
                .flowName(flowName)
                .flowStatus(Status.NOT_START.getCode())
                .build();
        if (Status.END.getCode().equals(lastProcess)) {
            bidOpenFlowSituationDTO.setFlowStatus(Status.PROCESSING.getCode());
        }
        if (!CommonUtil.isEmpty(floatControl)) {
            bidOpenFlowSituationDTO.setFlowStatus(Status.END.getCode());
        }
        return bidOpenFlowSituationDTO;
    }

    @Override
    public String getNowEvalFlow(Integer bidSectionId) {
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        String bidClassifyCode = bidSection.getBidClassifyCode();
        BidProtype bidProtype = BidProtype.getBidProtypeByCode(bidClassifyCode);

        switch (bidProtype) {
            case EPC:
                if (Status.END.getCode().equals(bidSection.getEvalStatus())) {
                    return EvalFlow.END_BID_EVAL.getShowName();
                }
                if (this.isGroupCompletion(gradeIds, EvalProcess.CALC_PRICE_SCORE.getCode())) {
                    return EvalFlow.RESULT.getShowName();
                }
                if (this.isGroupCompletion(gradeIds, EvalProcess.DETAILED.getCode())) {
                    return EvalFlow.QUOTE_SCORE.getShowName();
                }
                if (this.isGroupCompletion(gradeIds, EvalProcess.PRELIMINARY.getCode())) {
                    return EvalFlow.DETAIL_REVIEW.getShowName();
                }
                if (this.isGroupCompletion(gradeIds, EvalProcess.QUALIFICATION.getCode())) {
                    return EvalFlow.PRELIMINARY_REVIEW.getShowName();
                }
                if (Status.END.getCode().equals(bidSection.getBidOpenStatus())) {
                    if (StringUtils.isEmpty(bidSection.getEvalStartTime())) {
                        return EvalFlow.END_BID_OPENING.getShowName();
                    } else {
                        return EvalFlow.QUALIFICATION_REVIEW.getShowName();
                    }
                }
                break;
            case QUALIFICATION:
            case INVESTIGATION:
            case DESIGN:
            case ELEVATOR:
            case SUPERVISION:
                if (Status.END.getCode().equals(bidSection.getEvalStatus())) {
                    return EvalFlow.END_BID_EVAL.getShowName();
                }
                if (this.isGroupCompletion(gradeIds, EvalProcess.DETAILED.getCode())) {
                    return EvalFlow.RESULT.getShowName();
                }
                if (this.isGroupCompletion(gradeIds, EvalProcess.PRELIMINARY.getCode())) {
                    return EvalFlow.DETAIL_REVIEW.getShowName();
                }
                if (Status.END.getCode().equals(bidSection.getBidOpenStatus())) {
                    if (StringUtils.isEmpty(bidSection.getEvalStartTime())) {
                        return EvalFlow.END_BID_OPENING.getShowName();
                    } else {
                        return EvalFlow.PRELIMINARY_REVIEW.getShowName();
                    }
                }
                break;
            case CONSTRUCTION:
                if (Status.END.getCode().equals(bidSection.getEvalStatus())) {
                    return EvalFlow.END_BID_EVAL.getShowName();
                }
                if (Enabled.YES.getCode().equals(tenderDoc.getMutualSecurityStatus())) {
                    if (this.isGroupCompletion(gradeIds, EvalProcess.OTHER.getCode())) {
                        return EvalFlow.RESULT.getShowName();
                    }
                    if (!CommonUtil.isEmpty(bidSection.getPriceRecordStatus())
                            && ExecuteCode.SUCCESS.getCode().equals(bidSection.getPriceRecordStatus())) {
                        return EvalFlow.OTHER_REVIEW.getShowName();
                    }
                } else {
                    if (!CommonUtil.isEmpty(bidSection.getPriceRecordStatus())
                            && ExecuteCode.SUCCESS.getCode().equals(bidSection.getPriceRecordStatus())) {
                        return EvalFlow.RESULT.getShowName();
                    }
                }

                if (this.isGroupCompletion(gradeIds, EvalProcess.DETAILED.getCode())) {
                    return EvalFlow.QUOTE_SCORE.getShowName();
                }
                if (this.isGroupCompletion(gradeIds, EvalProcess.PRELIMINARY.getCode())) {
                    return EvalFlow.DETAIL_REVIEW.getShowName();
                }
                if (Status.END.getCode().equals(bidSection.getBidOpenStatus())) {
                    if (StringUtils.isEmpty(bidSection.getEvalStartTime())) {
                        return EvalFlow.END_BID_OPENING.getShowName();
                    } else {
                        return EvalFlow.PRELIMINARY_REVIEW.getShowName();
                    }
                }
                break;
            default:
        }
        return "...";

    }

    private boolean isGroupCompletion(String[] gradeIds, Integer evalProcess){
        ProcessCompletionDTO dto = gradeService.getProcessCompletion(gradeIds, evalProcess);
        if (dto == null) {
            return false;
        }
        return dto.getCompleteNum() != null && dto.getCompleteNum() != 0 && (dto.getNoCompleteNum() == null || dto.getNoCompleteNum() == 0);
    }
}
