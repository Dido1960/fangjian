package com.ejiaoyi.expert.service.impl;

import cn.hutool.core.date.DateUtil;
import com.ejiaoyi.common.constant.BackStatus;
import com.ejiaoyi.common.constant.EvalStatus;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.dto.ProcessCompletionDTO;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.BidProtype;
import com.ejiaoyi.common.enums.Enabled;
import com.ejiaoyi.common.enums.EvalProcess;
import com.ejiaoyi.common.enums.Status;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.expert.service.IEvalBackService;
import com.ejiaoyi.expert.service.IExpertService;
import com.ejiaoyi.expert.support.AuthUser;
import com.ejiaoyi.expert.support.CurrentUserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Auther: liuguoqiang
 * @Date: 2020-10-7 16:30
 */
@Service
public class EvalBackServiceImpl implements IEvalBackService {
    @Autowired
    private IBidSectionService bidSectionService;

    @Autowired
    private ITenderDocService tenderDocService;
    @Autowired
    private IFreeBackApplyService freeBackApplyService;
    @Autowired
    private IGradeService gradeService;
    @Autowired
    private IBackPushStatusService backPushStatusService;

    @Override
    public JsonData validBack() {
        JsonData result = new JsonData();
        AuthUser user = CurrentUserHolder.getUser();
        //如果不是专家组长直接返回2
        if (!"1".equals(user.getIsChairman())) {
            result.setCode("2");
            result.setMsg("只有评标组长才能进行评审回退！");
            return result;
        }

        BidSection bidSection = bidSectionService.getBidSectionById(user.getBidSectionId());
        result.setCode("1");
        Integer bidSectionId = user.getBidSectionId();
        //如果有一个正在审核的申请，则不允许添加更多数据
        List<FreeBackApply> applyings = freeBackApplyService.getApplyingByBsId(bidSectionId);
        if (CommonUtil.isEmpty(applyings) || applyings.size() == 0) {
            if (bidSection.getEvalStatus().equals(EvalStatus.UNSTART)) {
                result.setCode("2");
                result.setMsg("未开始评标，无法进行评标回退！");
            }
            if (bidSection.getEvalStatus().equals(EvalStatus.FINISH)) {
                result.setCode("2");
                result.setMsg("评标已结束，无法进行评标回退！");
            }
        } else {
            result.setCode("2");
            result.setMsg("尚有回退申请未审核，请勿重复申请！");
        }

        return result;
    }

    @Override
    public JsonData addFreeBackApply(FreeBackApply freeBackApply) {
        JsonData result = new JsonData();

        AuthUser user = CurrentUserHolder.getUser();
        freeBackApply.setBidSectionId(user.getBidSectionId());
        freeBackApply.setApplyUser(user.getUserId());
        freeBackApply.setApplyTime(DateUtil.formatLocalDateTime(LocalDateTime.now()));
        //获取当前回退时的环节
        freeBackApply.setStepNow(getEvalFlowNow(user.getBidSectionId()));
        freeBackApply.setCheckStatus(BackStatus.UNREVIEWED.toString());
        result.setCode("2");
        if (freeBackApplyService.addFreeBackApply(freeBackApply) != 1) {
            return result;
        }

        result.setCode("1");
        result.setMsg("回退申请已提交，请等待主管部门审核！");
        return result;
    }

    @Override
    public Integer getEvalFlowNow(Integer bidSectionId) {
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        String bidClassifyCode = bidSection.getBidClassifyCode();
        BidProtype bidProtype = BidProtype.getBidProtypeByCode(bidClassifyCode);
        boolean preliminary;
        switch (bidProtype) {
            case CONSTRUCTION:
                if (Enabled.YES.getCode().equals(tenderDoc.getMutualSecurityStatus())) {
                    if (isGroupCompletion(gradeIds, EvalProcess.OTHER.getCode())) {
                        return EvalProcess.RESULT.getCode();
                    }
                    if (Enabled.YES.getCode().equals(bidSection.getPriceRecordStatus())) {
                        return EvalProcess.OTHER.getCode();
                    }
                } else {
                    if (Enabled.YES.getCode().equals(bidSection.getPriceRecordStatus())) {
                        return EvalProcess.RESULT.getCode();
                    }
                }

                if (isGroupCompletion(gradeIds, EvalProcess.DETAILED.getCode())) {
                    return EvalProcess.RESULT.getCode();
                }

                if (isGroupCompletion(gradeIds, EvalProcess.PRELIMINARY.getCode())) {
                    return EvalProcess.DETAILED.getCode();
                }

                preliminary = (Status.END.getCode().equals(bidSection.getBidOpenStatus())
                        && EvalStatus.PROGRESSING.equals(bidSection.getEvalStatus()));

                if (preliminary) {
                    return EvalProcess.PRELIMINARY.getCode();
                }
                break;
            case EPC:
                if (Enabled.YES.getCode().equals(bidSection.getPriceRecordStatus())) {
                    return EvalProcess.RESULT.getCode();
                }
                if (isGroupCompletion(gradeIds, EvalProcess.DETAILED.getCode())) {
                    return EvalProcess.CALC_PRICE_SCORE.getCode();
                }

                if (isGroupCompletion(gradeIds, EvalProcess.PRELIMINARY.getCode())) {
                    return EvalProcess.DETAILED.getCode();
                }

                if (isGroupCompletion(gradeIds, EvalProcess.QUALIFICATION.getCode())) {
                    return EvalProcess.PRELIMINARY.getCode();
                }

                preliminary = (Status.END.getCode().equals(bidSection.getBidOpenStatus())
                        && EvalStatus.PROGRESSING.equals(bidSection.getEvalStatus()));
                if (preliminary) {
                    return EvalProcess.QUALIFICATION.getCode();
                }
                break;
            case QUALIFICATION:
            case INVESTIGATION:
            case ELEVATOR:
            case SUPERVISION:
            case DESIGN:
                if (isGroupCompletion(gradeIds, EvalProcess.DETAILED.getCode())) {
                    return EvalProcess.RESULT.getCode();
                }

                if (isGroupCompletion(gradeIds, EvalProcess.PRELIMINARY.getCode())) {
                    return EvalProcess.DETAILED.getCode();
                }

                preliminary = (Status.END.getCode().equals(bidSection.getBidOpenStatus())
                        && EvalStatus.PROGRESSING.equals(bidSection.getEvalStatus()));

                if (preliminary) {
                    return EvalProcess.PRELIMINARY.getCode();
                }
                break;
            default:
        }

        return null;
    }



    @Override
    public boolean isGroupCompletion(String[] gradeIds, Integer evalProcess){
        ProcessCompletionDTO dto = gradeService.getProcessCompletion(gradeIds, evalProcess);
        if (dto == null) {
            return false;
        }
        return dto.getCompleteNum() != null && dto.getCompleteNum() != 0 && (dto.getNoCompleteNum() == null || dto.getNoCompleteNum() == 0);
    }


    @Override
    public JsonData getBackPush() {
        JsonData result = new JsonData();
        AuthUser user = CurrentUserHolder.getUser();
        List<BackPushStatus> list = backPushStatusService.listBackPushByExpertId(user.getUserId());
        if (!CommonUtil.isEmpty(list) && list.size() > 0) {
            BackPushStatus backPushStatus = list.get(0);
            if (0 == backPushStatus.getPushResult()) {
                FreeBackApply freeBackApply = freeBackApplyService.getApplyById(backPushStatus.getBackId());
                EvalProcess evalProcess = EvalProcess.getCode(freeBackApply.getStep());
                if (BackStatus.PASS.toString().equals(freeBackApply.getCheckStatus())) {
                    result.setCode(BackStatus.PASS.toString());
                    result.setData(evalProcess.getCode());
                    result.setMsg("评审回退申请已通过，环节已回退到【" + evalProcess.getRemake() + "】");
                } else if (BackStatus.NOPASS.toString().equals(freeBackApply.getCheckStatus())) {
                    result.setCode(BackStatus.NOPASS.toString());
                    result.setMsg("评审回退到【" + evalProcess.getRemake() + "】的申请已被主管部门驳回!");
                } else {
                    result.setCode(freeBackApply.getCheckStatus());
                }
            }
        }
        return result;
    }

    @Override
    public Integer updateBackPush() {
        AuthUser user = CurrentUserHolder.getUser();
        return backPushStatusService.updateAllBackPush(user.getBidSectionId(), user.getUserId());
    }


}
