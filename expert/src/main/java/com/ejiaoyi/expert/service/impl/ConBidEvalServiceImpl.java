package com.ejiaoyi.expert.service.impl;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSONObject;
import com.ejiaoyi.common.dto.*;
import com.ejiaoyi.common.dto.quantity.QuantityBidder;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.*;
import com.ejiaoyi.common.enums.quantity.FloatPoint;
import com.ejiaoyi.common.enums.quantity.QuantityServiceVersion;
import com.ejiaoyi.common.exception.CustomException;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.service.impl.*;
import com.ejiaoyi.common.util.CalcQuoteScoreUtil;
import com.ejiaoyi.common.util.CalcUtil;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.ThreadUtlis;
import com.ejiaoyi.expert.dto.ClearBidProcessDTO;
import com.ejiaoyi.expert.enums.ExpertUserMsgStatus;
import com.ejiaoyi.expert.service.IBidEvalService;
import com.ejiaoyi.expert.service.IConBidEvalService;
import com.ejiaoyi.expert.service.IExpertService;
import com.ejiaoyi.expert.support.AuthUser;
import com.ejiaoyi.expert.support.CurrentUserHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 施工专家评标 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-12-02
 */
@Service
@Slf4j
public class ConBidEvalServiceImpl extends BaseServiceImpl implements IConBidEvalService {

    @Autowired
    private ICalcScoreParamService calcScoreParamService;

    @Autowired
    private IBidSectionService bidSectionService;

    @Autowired
    private IBidEvalService bidEvalService;

    @Autowired
    private TenderDocServiceImpl tenderDocService;

    @Autowired
    private BidderServiceImpl bidderService;

    @Autowired
    private BidSectionRelateServiceImpl bidSectionRelateService;

    @Autowired
    private BidderQuantityScoreServiceImpl bidderQuantityScoreService;

    @Autowired
    private BidderQuantityServiceImpl bidderQuantityService;

    @Autowired
    private BidderFileInfoServiceImpl bidderFileInfoService;

    @Autowired
    private EvalResultSgServiceImpl evalResultSgService;

    @Autowired
    private IGradeService gradeService;

    @Autowired
    private IGradeItemService gradeItemService;

    @Autowired
    private IExpertReviewMutualService expertReviewMutualService;

    @Autowired
    private IExpertReviewService expertReviewService;

    @Autowired
    private IExpertUserService expertUserService;

    @Autowired
    private IExpertService expertService;


//    @Override
//    public void calcPriceScore(Integer bidSectionId) {
//        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
//        BidSectionRelate bidSectionRelate = bidSectionRelateService.getBidSectionRelateByBSId(bidSectionId);
//        String priceScoreUid = bidSectionRelate.getPriceScoreUid();
//        if (StringUtils.isEmpty(priceScoreUid)) {
//            List<QuantityBidder> quantityBidders = bidderService.listPriceScoreQuantityBidder(bidSectionId);
//            String overallAnalysis = quantityService.createOverallAnalysis(tenderDoc.getXmlUid(), quantityBidders, QuantityServiceVersion.V1);
//            String floatPointStr = String.valueOf(CalcUtil.divide(tenderDoc.getFloatPoint(), "100", 4));
//            FloatPoint floatPoint = FloatPoint.getEnum(floatPointStr);
//            String priceUid = quantityService.calcQuantityScore(overallAnalysis, floatPoint, QuantityServiceVersion.V1);
//            bidSectionRelateService.updateRelateBySectionId(BidSectionRelate.builder()
//                    .bidSectionId(bidSectionId)
//                    .calcPriceUid(overallAnalysis)
//                    .priceScoreUid(priceUid)
//                    .build());
//        }
//    }

    @Override
    public JsonData validPriceScore(Integer bidSectionId) {
        JsonData data = new JsonData();
//        BidSectionRelate bidSectionRelate = bidSectionRelateService.getBidSectionRelateByBSId(bidSectionId);
//        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
//
//        String priceScoreUid = bidSectionRelate.getPriceScoreUid();
//        //  code = -1计算失败  code = 0尚未计算 code = 1 计算完成  code=2计算未完成
//        if (StringUtils.isNotEmpty(priceScoreUid)) {
//            ServiceResultDTO serviceResult = quantityService.getServiceResult(priceScoreUid, QuantityServiceVersion.V1);
//            if (ServiceState.COMPLETED.getCode().equals(serviceResult.getState())) {
//                String quantityScoreResult = quantityService.getQuantityScoreResult(bidSectionRelate.getCalcPriceUid(), QuantityServiceVersion.V1);
//                JSONObject jsonObject = JSONObject.parseObject(quantityScoreResult);
//                List<BidderQuantityScoreDTO> bidderQuantityScoreDTOS = JSONObject.parseArray(jsonObject.getString("bidder_quantity_score_list"), BidderQuantityScoreDTO.class);
//                bidderQuantityScoreService.saveBidderQuantityScoreList(bidSectionId,bidderQuantityScoreDTOS);
//                bidSectionService.updateBidSectionById(BidSection.builder()
//                        .id(bidSectionId)
//                        .priceRecordStatus(Enabled.YES.getCode())
//                        .build());
//                //正常结束后 如果没选择互保共建则 处理排名
//                if (!Enabled.YES.getCode().equals(tenderDoc.getMutualSecurityStatus())){
//                    ThreadUtlis.run(() -> {
//                        evalResultSgService.addResultByBsId(bidSectionId);
//                    });
//                }
//                data.setCode("1");
//                data.setMsg("报价得分计算完成！");
//            } else if (ServiceState.WAIT.getCode().equals(serviceResult.getState()) || ServiceState.ACTIVE.getCode().equals(serviceResult.getState())) {
//                data.setCode("2");
//                data.setData(serviceResult.getProcess());
//                data.setMsg("报价得分计算中...");
//
//            } else {
//                data.setCode("-1");
//                data.setMsg("报价得分计算失败");
//            }
//        } else {
//            data.setCode("0");
//            data.setMsg("报价得分尚未计算");
//        }
        return data;
    }

    @Override
    public ClearBidProcessDTO initClearBidProcessData(Integer bidSectionId) {
        List<Bidder> bidders = bidderService.listPassBidOpenBidder(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        List<BidderQuantity> bidderQuantities = new ArrayList<>();
        List<String> totalPercentageList = new ArrayList<>();

        int completeNum = 0;
        int notCompleteNum = 0;
        String totalPercentage;
        for (Bidder bidder : bidders) {
            List<String> addProcess = new ArrayList<>();
            String bidderTotalPercentage;
            BidderQuantity bidderQuantity = null;
            BidderFileInfo bidderFileInfo = bidderFileInfoService.getBidderFileInfoByBidderId(bidder.getId());
            String xmlUid = bidderFileInfo.getXmlUid();
//            if (StringUtils.isNotEmpty(xmlUid)) {
//                bidderQuantity = bidderQuantityService.getBidderQuantityByBidXmlUid(xmlUid);
//            }

            String structureAnalysisProcess = "0";
            String arithmeticAnalysisProcess = "0";
            String priceAnalysisProcess = "0";
            String ruleAnalysisProcess = "0";

            String structureAnalysisState;
            String arithmeticAnalysisState;
            String priceAnalysisState;
            String ruleAnalysisState;
            if (bidderQuantity == null) {
                bidderTotalPercentage = "0";
                bidderQuantity = BidderQuantity.builder()
                        .bidderName(bidder.getBidderName())
                        .bidderTotalPercentage(bidderTotalPercentage)
                        .build();
                notCompleteNum++;
            } else {
                // 投标人清标总进度是否完成
                boolean totalComplete;
                // 清标选择的清标项
                int num = 1;
                // 算术性分析进度
                arithmeticAnalysisState = bidderQuantity.getArithmeticAnalysisState();
                boolean arithmeticComplete = ServiceState.COMPLETED.getCode().equals(arithmeticAnalysisState);
                if (arithmeticComplete) {
                    arithmeticAnalysisProcess = "100";
                } else {
                    if (StringUtils.isNotEmpty(bidderQuantity.getArithmeticAnalysisProcess())) {
                        arithmeticAnalysisProcess = bidderQuantity.getArithmeticAnalysisProcess();
                    }
                }
                addProcess.add(arithmeticAnalysisProcess);
                totalComplete = arithmeticComplete;

                // 错漏项分析进度
                if (Enabled.YES.getCode().equals(tenderDoc.getStructureStatus())) {
                    structureAnalysisState = bidderQuantity.getStructureAnalysisState();
                    boolean structureComplete = ServiceState.COMPLETED.getCode().equals(structureAnalysisState);
                    if (structureComplete) {
                        structureAnalysisProcess = "100";
                    } else {
                        if (StringUtils.isNotEmpty(bidderQuantity.getStructureAnalysisProcess())) {
                            structureAnalysisProcess = bidderQuantity.getStructureAnalysisProcess();
                        }
                    }
                    num++;
                    addProcess.add(structureAnalysisProcess);
                    totalComplete = totalComplete && structureComplete;
                }

                // 取费基础分析进度
                if (Enabled.YES.getCode().equals(tenderDoc.getFundBasisStatus())) {
                    ruleAnalysisState = bidderQuantity.getRuleAnalysisState();
                    boolean ruleComplete = ServiceState.COMPLETED.getCode().equals(ruleAnalysisState);
                    if (ruleComplete) {
                        ruleAnalysisProcess = "100";
                    } else {
                        if (StringUtils.isNotEmpty(bidderQuantity.getRuleAnalysisProcess())) {
                            ruleAnalysisProcess = bidderQuantity.getRuleAnalysisProcess();
                        }
                    }
                    num++;
                    addProcess.add(ruleAnalysisProcess);
                    totalComplete = totalComplete && ruleComplete;
                }

                // 零负报价分析进度
                if (Enabled.YES.getCode().equals(tenderDoc.getPriceStatus())) {
                    priceAnalysisState = bidderQuantity.getPriceAnalysisState();
                    boolean priceComplete = ServiceState.COMPLETED.getCode().equals(priceAnalysisState);
                    if (priceComplete) {
                        priceAnalysisProcess = "100";
                    } else {
                        if (StringUtils.isNotEmpty(bidderQuantity.getPriceAnalysisProcess())) {
                            priceAnalysisProcess = bidderQuantity.getPriceAnalysisProcess();
                        }
                    }
                    num++;
                    addProcess.add(priceAnalysisProcess);
                    totalComplete = totalComplete && priceComplete;
                }

                // 计算投标人清标总进度
                String bidderTotalPercentages = CalcUtil.addMore(addProcess);
                double divide = CalcUtil.divide(bidderTotalPercentages, String.valueOf(num), 2);
                bidderTotalPercentage = String.valueOf(divide);

                // 统计投标人清标完成和未完成数量
                if (totalComplete) {
                    completeNum++;
                } else {
                    notCompleteNum++;
                    // 未完成，且总进度不等于0，默认减去0.01
                    if (!"0".equals(bidderTotalPercentage) && !"0.0".equals(bidderTotalPercentage)) {
                        bidderTotalPercentage = CalcUtil.subtract(bidderTotalPercentage, "0.01");
                    }
                }

            }
            totalPercentageList.add(bidderTotalPercentage);
            bidderQuantity.setStructureAnalysisProcess(structureAnalysisProcess);
            bidderQuantity.setPriceAnalysisProcess(priceAnalysisProcess);
            bidderQuantity.setRuleAnalysisProcess(ruleAnalysisProcess);
            bidderQuantity.setArithmeticAnalysisProcess(arithmeticAnalysisProcess);
            bidderQuantity.setBidderTotalPercentage(bidderTotalPercentage);
            bidderQuantity.setBidderName(bidder.getBidderName());
            bidderQuantities.add(bidderQuantity);
        }
        // 清标总进度计算
        String bidderTotalPercentages = CalcUtil.addMore(totalPercentageList);
        totalPercentage = String.valueOf(CalcUtil.divide(bidderTotalPercentages, String.valueOf(bidderQuantities.size()), 2));

        // 未完成，且总进度不等于0，默认减去0.01
        /*if (!"0".equals(totalPercentage)) {
            totalPercentage = CalcUtil.subtract(totalPercentage, "0.01");
        }*/
        return ClearBidProcessDTO.builder()
                .bidderQuantities(bidderQuantities)
                .completeNum(completeNum)
                .notCompleteNum(notCompleteNum)
                .totalPercentage(totalPercentage)
                .build();
    }

    @Override
    public Integer mutualGradeInit() {

        //创建grade
        OtherEvalMethod mutualGrade = OtherEvalMethod.MUTUAL_PROTECTION;
        Grade grade = Grade.builder()
                .name(mutualGrade.getName())
                .type(mutualGrade.getType())
                .calcType(mutualGrade.getCalcType())
                .reviewProcess(mutualGrade.getReviewProcess())
                .score(mutualGrade.getScore()).build();
        Integer gradeId = gradeService.insertGrade(grade);

        List<GradeItem> gradeItems = new ArrayList<>();

        MutualItemMethod mutualItemMethod1 = MutualItemMethod.MUTUAL_ITEM_METHOD_1;
        MutualItemMethod mutualItemMethod2 = MutualItemMethod.MUTUAL_ITEM_METHOD_2;
        MutualItemMethod mutualItemMethod3 = MutualItemMethod.MUTUAL_ITEM_METHOD_3;

        gradeItems.add(GradeItem.builder()
                .gradeId(gradeId)
                .itemContent(mutualItemMethod1.getItemContent())
                .score(mutualItemMethod1.getScore())
                .scoreType(mutualItemMethod1.getScoreType())
                .scoreRange(mutualItemMethod1.getScoreRange())
                .remark(mutualItemMethod1.getRemark())
                .build());

        gradeItems.add(GradeItem.builder()
                .gradeId(gradeId)
                .itemContent(mutualItemMethod2.getItemContent())
                .score(mutualItemMethod2.getScore())
                .scoreType(mutualItemMethod2.getScoreType())
                .scoreRange(mutualItemMethod2.getScoreRange())
                .remark(mutualItemMethod2.getRemark())
                .build());

        gradeItems.add(GradeItem.builder()
                .gradeId(gradeId)
                .itemContent(mutualItemMethod3.getItemContent())
                .score(mutualItemMethod3.getScore())
                .scoreType(mutualItemMethod3.getScoreType())
                .scoreRange(mutualItemMethod3.getScoreRange())
                .remark(mutualItemMethod3.getRemark())
                .build());

        gradeItemService.saveBatch(gradeItems);

        return gradeId;
    }

    @Override
    public List<Bidder> otherMutualGradeIsEnd(List<Bidder> bidders, Integer gradeId, Integer expertId) {

        for (Bidder bidder : bidders) {
            ExpertReviewMutual query = ExpertReviewMutual.builder()
                    .bidderId(bidder.getId())
                    .expertId(expertId)
                    .gradeId(gradeId)
                    .build();
            List<ExpertReviewMutual> list = expertReviewMutualService.listExpertReviewMutual(query, true);
            if (!CommonUtil.isEmpty(list) && list.size() > 0) {
                bidder.setExpertReviewStatusForBidder(true);
            } else {
                bidder.setExpertReviewStatusForBidder(false);
            }
        }

        return bidders;
    }

    @Override
    public JsonData checkPersonalOtherMutualEnd(Integer gradeId) {
        JsonData result = new JsonData();
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        if (bidEvalService.isFreeBackApplying(bidSectionId)){
            result.setCode("2");
            result.setMsg("评审回退尚未审核，操作失败!");
            return result;
        }
        //筛选 通过初步评审的投标人
        List<Bidder> bidders = bidEvalService.listBidderPassPreliminary(bidSectionId);

        List<ExpertReviewMutual> list = expertReviewMutualService.listExpertReviewMutual(ExpertReviewMutual.builder()
                .expertId(user.getUserId())
                .gradeId(gradeId)
                .build(), true);

        if (CommonUtil.isEmpty(bidders) || CommonUtil.isEmpty(list)) {
            result.setCode("2");
            result.setMsg("其他评审还有评审项未完成，请检查！");
            return result;
        }
        if (bidders.size() == list.size()){
            result.setCode("1");
        }else {
            result.setCode("2");
            result.setMsg("其他评审还有评审项未完成，请检查！");
        }
        return result;
    }

    @Override
    public Boolean personalOtherMutualEnd() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(user.getBidSectionId());
        String[] gradeIds = tenderDoc.getOtherGradeId().split(",");

        return expertReviewService.updatePersonalReviewEnd(gradeIds, EvalProcess.OTHER.getCode(), user.getUserId());
    }

    @Override
    public void restartOtherMutualReview() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(user.getBidSectionId());
        String[] gradeIds = tenderDoc.getOtherGradeId().split(",");
        expertReviewService.updateCallPersonReview(gradeIds, EvalProcess.OTHER.getCode());
        bidEvalService.expertUserMsgPush(user.getBidSectionId(), EvalProcess.OTHER.getCode(), ExpertUserMsgStatus.RE_EVALUATION);
    }

    @Override
    public JsonData validOtherMutualGroupEnd() {
        JsonData result = new JsonData();

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getOtherGradeId().split(",");

        if (bidEvalService.isFreeBackApplying(bidSectionId)){
            result.setCode("2");
            result.setMsg("评审回退尚未审核，操作失败!");
            return result;
        }

        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.OTHER.getCode());
        Grade grade = grades.get(0);

        // 检查所有专家是否结束个人评审
        if (!bidEvalService.validAllExpertEval(bidSectionId, grade.getId())) {
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

        List<Bidder> bidders = bidEvalService.listBidderPassPreliminary(bidSectionId);

        //检查 专家的选择是否一致
        List<ExpertReviewMutual> resultList = expertReviewMutualService.listMutualResultGroup(grade.getId(), null);

        if (CommonUtil.isEmpty(bidders) || CommonUtil.isEmpty(resultList)) {
            result.setCode("2");
            result.setMsg("小组结束失败！");
            return result;
        }

        if (bidders.size() != resultList.size()) {
            result.setCode("2");
            result.setMsg("专家评审结果不一致，无法结束小组评审！");
            return result;
        }

        result.setCode("1");
        result.setMsg("是否结束小组评审？");
        return result;
    }

    @Override
    public Map<String, Object> getBidderMutualResultData(Integer bidderId, Integer gradeId, Integer isAllExpertEnd) {
        Map<String, Object> result = new HashMap<>();

        List<ExpertReviewMutual> expertReviewMutuals = expertReviewMutualService.listExpertReviewMutual(ExpertReviewMutual.builder()
                .gradeId(gradeId)
                .bidderId(bidderId).build(), false);

        result.put("expertReviewMutuals", expertReviewMutuals);

        if (isAllExpertEnd == 1) {
            //汇总数据
            List<ExpertReviewMutual> groups = expertReviewMutualService.listMutualResultGroup(gradeId, bidderId);
            result.put("groups", groups);
        }
        return result;
    }

    @Override
    public List<BidderResultDTO> getGroupMutualResultData(Integer gradeId) {
        List<BidderResultDTO> result = new ArrayList<>();

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        //筛选 通过初步评审的投标人
        List<Bidder> bidders = bidEvalService.listBidderPassPreliminary(bidSectionId);


        for (Bidder bidder : bidders) {
            List<ExpertReviewMutual> groups = expertReviewMutualService.listMutualResultGroup(gradeId, bidder.getId());
            BidderResultDTO build = BidderResultDTO.builder()
                    .bidderId(bidder.getId()).build();
            if (!CommonUtil.isEmpty(groups) && groups.size() == 1){
                build.setIsConsistent(true);
                build.setScore(groups.get(0).getEvalResult());
            }else {
                build.setIsConsistent(false);
            }
            result.add(build);
        }
        return result;
    }

    @Override
    public Boolean endGroupMutualResult(Integer gradeId) {

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        Grade build = Grade.builder()
                .id(gradeId)
                .groupEnd(Enabled.YES.getCode())
                .build();
        boolean status = gradeService.updateGrade(build);
        if (!status) {
            return false;
        }

        //正常结束后对排名数据进行存储
        ThreadUtlis.run(() -> {
            evalResultSgService.addResultByBsId(bidSectionId);
        });
        bidEvalService.expertUserMsgPush(bidSectionId, EvalProcess.OTHER.getCode(), ExpertUserMsgStatus.GROUP_END);

        return true;
    }

}
