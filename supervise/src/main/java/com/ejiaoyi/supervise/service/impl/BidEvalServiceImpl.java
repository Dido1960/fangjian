package com.ejiaoyi.supervise.service.impl;

import cn.hutool.core.date.DateUtil;
import com.ejiaoyi.common.constant.*;
import com.ejiaoyi.common.dto.BidderResultDTO;
import com.ejiaoyi.common.dto.ExpertUserDto;
import com.ejiaoyi.common.dto.NowConvertPdf;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.*;
import com.ejiaoyi.common.exception.CustomException;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.service.impl.*;
import com.ejiaoyi.common.util.*;
import com.ejiaoyi.supervise.service.IBidEvalService;
import com.ejiaoyi.supervise.support.AuthUser;
import com.ejiaoyi.supervise.support.CurrentUserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 监管 评标流程接口实现
 *
 * @auther: liuguoqiang
 * @Date: 2020-12-7 16:39
 */
@Service
@Slf4j
public class BidEvalServiceImpl implements IBidEvalService {

    @Autowired
    private IBidSectionService bidSectionService;
    @Autowired
    private IGradeService gradeService;
    @Autowired
    private IBidApplyService bidApplyService;
    @Autowired
    private ITenderDocService tenderDocService;
    @Autowired
    private ICandidateSuccessService candidateSuccessService;
    @Autowired
    private IEvalResultSgService evalResultSgService;
    @Autowired
    private IEvalResultJlService evalResultJlService;
    @Autowired
    private IEvalResultEpcService evalResultEpcService;
    @Autowired
    private IBidderService bidderService;
    @Autowired
    private IGradeItemService gradeItemService;
    @Autowired
    private IExpertReviewSingleItemScoreService expertReviewSingleItemScoreService;
    @Autowired
    private IExpertReviewSingleItemService expertReviewSingleItemService;
    @Autowired
    private IExpertUserService expertUserService;
    @Autowired
    private IExpertReviewSingleItemDeductService expertReviewSingleItemDeductService;
    @Autowired
    private IExpertReviewSingleItemDeductScoreService expertReviewSingleItemDeductScoreService;
    @Autowired
    private IBidderOpenInfoService bidderOpenInfoService;
    @Autowired
    private ICandidateResultsService candidateResultsService;
    @Autowired
    private IQuoteScoreResultService quoteScoreResultService;
    @Autowired
    private IQuoteScoreResultAppendixService quoteScoreResultAppendixService;
    @Autowired
    private IBidderReviewResultService bidderReviewResultService;
    @Autowired
    private IExpertReviewService expertReviewService;
    @Autowired
    private IBidderReviewResultScoreService bidderReviewResultScoreService;
    @Autowired
    private IBidderReviewResultDeductService bidderReviewResultDeductService;
    @Autowired
    private IBidVoteService bidVoteService;
    @Autowired
    private ICalcScoreParamService calcScoreParamService;
    @Autowired
    private IBidSectionRelateService bidSectionRelateService;
    @Autowired
    private IFreeBackApplyService freeBackApplyService;
    @Autowired
    private IFDFSService fdfsService;
    @Autowired
    private IBackPushStatusService backPushStatusService;
    @Autowired
    private IEvalReportService evalReportService;
    @Autowired
    private ZgysServiceImpl zgysService;
    @Autowired
    private SgServiceImpl sgService;
    @Autowired
    private SjServiceImpl sjService;
    @Autowired
    private JlServiceImpl jlService;
    @Autowired
    private EpcServiceImpl epcService;
    @Autowired
    private IExpertReviewMutualService expertReviewMutualService;
    @Autowired
    private IReevalLogService reevalLogService;


    @Override
    public Map<String, Integer> listProcessStatus(Integer bidSectionId) {
        Map<String, Integer> result = new HashMap<>();

        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);

        BidProtype bidProtype = BidProtype.getBidProtypeByCode(bidSection.getBidClassifyCode());
        boolean isOtherEval = false;
        if (BidProtype.CONSTRUCTION.equals(bidProtype)) {
            TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
            if (Enabled.YES.getCode().equals(tenderDoc.getMutualSecurityStatus())) {
                isOtherEval = true;
            }
        }

        List<EvalProcessGov> processList = EvalProcessGov.listEvalProcessGovByBp(bidProtype, isOtherEval);

        Integer status = null;
        for (EvalProcessGov evalProcessGov : processList) {
            if (!CommonUtil.isEmpty(status) && (Status.PROCESSING.getCode().equals(status) || Status.NOT_START.getCode().equals(status))) {
                //如果其中一个流程状态为进行中或未开始，则不对以下流程进行判断。默认为未开始
                result.put(evalProcessGov.getProcessName(), Status.NOT_START.getCode());
                continue;
            }
            switch (evalProcessGov) {
                case SELECT_LEADER:
                    //推选组长
                    status = selectLeaderStatus(bidSection);
                    break;
                case QUALIFICATION:
                    //资格审查
                    status = gradeStatus(bidSection, EvalProcess.QUALIFICATION.getCode());
                    break;
                case PRELIMINARY:
                    //初步评审
                    status = gradeStatus(bidSection, EvalProcess.PRELIMINARY.getCode());
                    break;
                case DETAILED:
                    //详细评审
                    status = gradeStatus(bidSection, EvalProcess.DETAILED.getCode());
                    break;
                case OTHER:
                    //其他评审
                    status = gradeStatus(bidSection, EvalProcess.OTHER.getCode());
                    break;
                case CALC_PRICE_SCORE:
                    //投标报价
                    if (BidProtype.EPC.getCode().equals(bidSection.getBidClassifyCode())) {
                        //判断是否为EPC
                        status = gradeStatus(bidSection, EvalProcess.CALC_PRICE_SCORE.getCode());
                    } else {
                        status = (!CommonUtil.isEmpty(bidSection.getPriceRecordStatus()) && Enabled.YES.getCode().equals(bidSection.getPriceRecordStatus())) ? 2 : 1;
                    }
                    break;
                case RESULT:
                case CANDIDATES:
                    //评审结果及推选状态一致
                    status = resultStatus(bidSection);
                    break;
                default:
                    status = Status.NOT_START.getCode();
            }
            result.put(evalProcessGov.getProcessName(), status);
        }

        return result;
    }

    /**
     * 推选组长 流程判断
     *
     * @param bidSection 标段数据
     * @return 对应 Status 0：未开始 1：进行中 2：已完成
     */
    private Integer selectLeaderStatus(BidSection bidSection) {
        BidApply bidApply = bidApplyService.getBidApplyByBidSectionId(bidSection.getId());
        if (CommonUtil.isEmpty(bidApply)) {
            //未开始评标
            return 0;
        } else if (CommonUtil.isEmpty(bidApply.getChairMan())) {
            //未推选组长
            return 1;
        } else {
            //推选完成
            return 2;
        }
    }

    /**
     * 关于grade评审方法的 流程判断
     *
     * @param bidSection 标段数据
     * @return 对应 Status 1：进行中 2：已完成
     */
    private Integer gradeStatus(BidSection bidSection, Integer evalProcess) {
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSection.getId());
        String[] gradeIds = null;
        if (BidProtype.CONSTRUCTION.getCode().equals(bidSection.getBidClassifyCode()) && EvalProcess.OTHER.getCode().equals(evalProcess)) {
            gradeIds = tenderDoc.getOtherGradeId().split(",");
        } else {
            gradeIds = tenderDoc.getGradeId().split(",");
        }
        List<Grade> grades = gradeService.listGrade(gradeIds, evalProcess);
        if (!CommonUtil.isEmpty(grades) && Enabled.YES.getCode().equals(grades.get(0).getGroupEnd())) {
            return 2;
        } else {
            if (EvalProcess.QUALIFICATION.getCode().equals(evalProcess)
                    || EvalProcess.PRELIMINARY.getCode().equals(evalProcess)
                    || EvalProcess.DETAILED.getCode().equals(evalProcess)) {
                if (Status.PROCESSING.getCode().equals(bidSection.getCancelStatus())) {
                    return Status.NOT_START.getCode();
                }
            }
            return 1;
        }
    }

    /**
     * 评审结果 流程判断
     *
     * @param bidSection 标段数据
     * @return 对应 Status 1：进行中 2：已完成
     */
    private Integer resultStatus(BidSection bidSection) {
        BidProtype bidProtype = BidProtype.getBidProtypeByCode(bidSection.getBidClassifyCode());
        Integer status = null;
        Object obj = null;
        switch (bidProtype) {
            case QUALIFICATION:
                //资格预审直接通过详细评审是否完成判断
                status = gradeStatus(bidSection, EvalProcess.DETAILED.getCode());
                break;
            case DESIGN:
            case INVESTIGATION:
            case ELEVATOR:
                obj = candidateSuccessService.listCandidateSuccess(bidSection.getId());
                break;
            case EPC:
                //总承包
                obj = evalResultEpcService.listRankingBidderByBsId(bidSection.getId());
                break;
            case CONSTRUCTION:
                //施工
                obj = evalResultSgService.listRankingBidderByBsId(bidSection.getId());
                break;
            case SUPERVISION:
                //监理
                obj = evalResultJlService.listRankingBidderByBsId(bidSection.getId());
                break;
            default:
                status = Status.PROCESSING.getCode();
        }
        if (CommonUtil.isEmpty(status)) {
            status = !CommonUtil.isEmpty(obj) ? Status.END.getCode() : Status.PROCESSING.getCode();
        }
        return status;
    }


    @Override
    public List<BidderReviewResult> getQualifyBiddersGradeResult(Integer gradeId) {
        List<BidderReviewResult> result = new ArrayList<>();
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        // 获取通过开标的投标人
        List<Bidder> bidders = bidderService.listPassBidOpenBidder(bidSectionId);
        // 对每个grade进行不通过统计，判断是否打分一致
        int size = gradeItemService.listGradeItem(gradeId).size();
        for (Bidder bidder : bidders) {
            List<BidderResultDTO> bidderResultDTOS = expertReviewSingleItemService.listGradeResult(gradeId, bidder.getId());
            BidderReviewResult newResult = BidderReviewResult.builder()
                    .bidderId(bidder.getId())
                    .isConsistent(true)
                    .gradeId(gradeId).build();
            if (size == bidderResultDTOS.size()) {
                for (BidderResultDTO bidderResultDTO : bidderResultDTOS) {
                    if (bidderResultDTO.getPassResult() == 0) {
                        newResult.setResult("0");
                        break;
                    }
                }
            } else {
                newResult.setIsConsistent(false);
            }
            result.add(newResult);
        }
        return result;
    }

    @Override
    public Map<String, Object> getBidderQualifiedData(Integer bidderId, Integer gradeId) {
        Map<String, Object> result = new HashMap<>();
        ExpertReviewSingleItem query = ExpertReviewSingleItem.builder()
                .bidderId(bidderId)
                .gradeId(gradeId)
                .build();
        List<ExpertReviewSingleItem> singleItems = expertReviewSingleItemService.listExpertReviewSingleItem(query);

        result.put("singleItems", singleItems);
        //汇总数据
        List<BidderResultDTO> bidderResultDTOS = expertReviewSingleItemService.listItemResult(gradeId, bidderId);
        result.put("bidderResultDTOS", bidderResultDTOS);
        return result;
    }

    @Override
    public List<BidderReviewResult> getPreBiddersGradeResult(Integer gradeId) {
        List<BidderReviewResult> result = new ArrayList<>();
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        List<Bidder> bidders;
        // 获取进入初步评审的投标人
        if (BidProtype.EPC.getCode().equals(bidSection.getBidClassifyCode())) {
            //施工总承包 获取通过资格审查的投标人
            bidders = bidderService.listBidderPassQualifyReview(bidSectionId);
        } else {
            //获取通过开标的投标人
            bidders = bidderService.listPassBidOpenBidder(bidSectionId);
        }
        // 对每个grade进行不通过统计，判断是否打分一致
        int size = gradeItemService.listGradeItem(gradeId).size();
        for (Bidder bidder : bidders) {
            List<BidderResultDTO> bidderResultDTOS = expertReviewSingleItemService.listGradeResult(gradeId, bidder.getId());
            BidderReviewResult newResult = BidderReviewResult.builder()
                    .bidderId(bidder.getId())
                    .isConsistent(true)
                    .gradeId(gradeId).build();
            if (size == bidderResultDTOS.size()) {
                for (BidderResultDTO bidderResultDTO : bidderResultDTOS) {
                    if (bidderResultDTO.getPassResult() == 0) {
                        newResult.setResult("0");
                        break;
                    }
                }
            } else {
                newResult.setIsConsistent(false);
            }
            result.add(newResult);
        }
        return result;
    }

    @Override
    public List<BidderResultDTO> getEpcDetailedGroupBiddersResult() {
        List<BidderResultDTO> result = new ArrayList<>();
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);

        //获取所有进入详细评审的投标人
        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);
        for (Bidder bidder : bidders) {
            BidderResultDTO bidderResultDTO = BidderResultDTO.builder().bidderId(bidder.getId()).isConsistent(true).build();
            String score = expertReviewSingleItemScoreService.getAvgScoreForReview(gradeIds, EvalProcess.DETAILED.getCode(), null, bidder.getId(), expertUsers.size());
            bidderResultDTO.setArithmeticScore(new BigDecimal(score).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            result.add(bidderResultDTO);
        }

        return result;
    }

    @Override
    public Map<String, Object> getEpcDetailedGroupBidderResult(Integer bidderId) {
        Map<String, Object> result = new HashMap<>();

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");

        List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);
        List<ExpertReviewSingleItemScore> scoreList = expertReviewSingleItemScoreService.listScoreBySth(gradeIds, bidderId, null, null, EvalProcess.DETAILED.getCode(), null);
        if (!CommonUtil.isEmpty(expertUsers) && expertUsers.size() != 0) {
            List<BidderResultDTO> bidderResultDTOS = new ArrayList<>();
            List<GradeItem> gradeItems = gradeItemService.listGradeItemBySth(gradeIds, EvalProcess.DETAILED.getCode(), null);
            for (GradeItem gradeItem : gradeItems) {
                List<ExpertReviewSingleItemScore> itemScoreList = expertReviewSingleItemScoreService.listScoreBySth(gradeIds, bidderId, null, gradeItem.getId(), EvalProcess.DETAILED.getCode(), null);
                BidderResultDTO newDto = BidderResultDTO.builder()
                        .bidderId(bidderId)
                        .gradeItemId(gradeItem.getId())
                        .build();
                double avgReslut = 0.0;
                for (ExpertReviewSingleItemScore score : itemScoreList) {
                    avgReslut += Double.parseDouble(score.getEvalScore());
                }
                avgReslut = avgReslut / expertUsers.size();
                newDto.setArithmeticScore(new BigDecimal(avgReslut).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                bidderResultDTOS.add(newDto);
            }
            result.put("bidderResultDTOS", bidderResultDTOS);
        }
        result.put("resultList", scoreList);
        return result;
    }

    @Override
    public List<BidderReviewResultDeduct> getConDetailedGroupBiddersResult(Integer gradeId) {
        List<BidderReviewResultDeduct> result = new ArrayList<>();
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        // 获取进入详细评审的投标人
        List<Bidder> bidders = bidderService.listDetailedBidder(user.getBidSectionId());
        //对每个grade进行扣分统计（少数服从多数）
        for (Bidder bidder : bidders) {
            List<BidderResultDTO> bidderResultDTOS = expertReviewSingleItemDeductService.listSumResult(gradeId, bidder.getId());
            BidderReviewResultDeduct newResult = BidderReviewResultDeduct.builder()
                    .bidderId(bidder.getId())
                    .isConsistent(true)
                    .gradeId(gradeId).build();
            double deductNum = 0.0;
            for (BidderResultDTO bidderResultDTO : bidderResultDTOS) {
                if (bidderResultDTO.getDeductSum() != 0 && bidderResultDTO.getNoDeductSum() != 0) {
                    newResult.setIsConsistent(false);
                }
                if (bidderResultDTO.getDeductSum() > bidderResultDTO.getNoDeductSum()) {
                    deductNum += Double.parseDouble(bidderResultDTO.getScore());
                }
            }
            newResult.setDeductScore(new BigDecimal(deductNum).setScale(2, BigDecimal.ROUND_HALF_UP).toString());

            result.add(newResult);
        }
        return result;
    }

    @Override
    public Map<String, Object> getConDetailedGroupBidderResult(Integer bidderId, Integer gradeId) {
        Map<String, Object> result = new HashMap<>();

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);
        ExpertReviewSingleItemDeduct query = ExpertReviewSingleItemDeduct.builder()
                .bidderId(bidderId)
                .gradeId(gradeId)
                .build();
        List<ExpertReviewSingleItemDeduct> deducts = expertReviewSingleItemDeductService.listExpertReviewSingleItemDeduct(query);
        result.put("deducts", deducts);
        //汇总数据
        List<BidderResultDTO> bidderResultDTOS = expertReviewSingleItemDeductService.listResultIsConsistent(gradeId, bidderId, expertUsers.size());
        result.put("bidderResultDTOS", bidderResultDTOS);
        return result;
    }

    @Override
    public List<BidderResultDTO> getSupDetailedGroupBiddersResult(Integer reviewType) {
        List<BidderResultDTO> result = new ArrayList<>();
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        List<GradeItem> gradeItems = gradeItemService.listGradeItemBySth(gradeIds, EvalProcess.DETAILED.getCode(), reviewType);
        List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);

        //获取所有进入详细评审的投标人
        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);
        for (Bidder bidder : bidders) {
            BidderResultDTO bidderResultDTO = BidderResultDTO.builder().bidderId(bidder.getId()).isConsistent(true).build();
            if (ReviewType.VIOLATION.getCode().equals(reviewType)) {//违章行为
                //判断是否打分一致
                bidderResultDTO.setIsConsistent(expertReviewSingleItemDeductScoreService.checkBidderResultConsistent(gradeIds, reviewType, bidder.getId(), gradeItems.size()));
                if (bidderResultDTO.getIsConsistent()) {
                    //打分一致则获取平均分
                    String deduct = expertReviewSingleItemDeductScoreService.getAvgDeductScoreForReview(gradeIds, reviewType, bidder.getId(), expertUsers.size());
                    bidderResultDTO.setArithmeticScore(new BigDecimal(deduct).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                }
            } else if (ReviewType.BUSINESS_STANDARD.getCode().equals(reviewType)) {
                //商务标
                //判断是否打分一致
                bidderResultDTO.setIsConsistent(expertReviewSingleItemScoreService.checkBidderResultConsistent(gradeIds, reviewType, bidder.getId(), gradeItems.size()));
                if (bidderResultDTO.getIsConsistent()) {
                    //打分一致则获取平均分
                    String score = expertReviewSingleItemScoreService.getAvgScoreForReview(gradeIds, EvalProcess.DETAILED.getCode(), reviewType, bidder.getId(), expertUsers.size());
                    bidderResultDTO.setArithmeticScore(new BigDecimal(score).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                }
            } else if (ReviewType.TECHNICAL_STANDARD.getCode().equals(reviewType)) {
                //技术标
                String score = expertReviewSingleItemScoreService.getAvgScoreForReview(gradeIds, EvalProcess.DETAILED.getCode(), reviewType, bidder.getId(), expertUsers.size());
                bidderResultDTO.setArithmeticScore(new BigDecimal(score).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }
            result.add(bidderResultDTO);
        }

        return result;
    }

    @Override
    public Map<String, Object> getSupDetailedGroupBidderResult(Integer bidderId, Integer reviewType) {
        Map<String, Object> result = new HashMap<>();

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");

        List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);
        if (ReviewType.VIOLATION.getCode().equals(reviewType)) {
            //违章行为
            List<ExpertReviewSingleItemDeductScore> deductScoreList = expertReviewSingleItemDeductScoreService.listDeductScoreBySth(gradeIds, bidderId, null, null, reviewType);
            List<BidderResultDTO> bidderResultDTOS = getBidderDeductResultAvg(gradeIds, reviewType, bidderId, expertUsers.size());
            result.put("bidderResultDTOS", bidderResultDTOS);
            result.put("resultList", deductScoreList);
        } else {
            List<ExpertReviewSingleItemScore> scoreList = expertReviewSingleItemScoreService.listScoreBySth(gradeIds, bidderId, null, null, EvalProcess.DETAILED.getCode(), reviewType);
            List<BidderResultDTO> bidderResultDTOS = getBidderScoreResultAvg(gradeIds, reviewType, bidderId, expertUsers.size());
            result.put("bidderResultDTOS", bidderResultDTOS);
            result.put("resultList", scoreList);
        }
        return result;
    }

    /**
     * 监理获取当前投标人 当前评审类型 每个gradeItem的平均分，并判断评审结果是否一致
     *
     * @param gradeIds   所有的gradeID
     * @param reviewType 评审类型
     * @param bidderId   投标人
     * @param expertSize 专家人数
     * @return 监理获取当前投标人 当前评审类型 每个gradeItem的平均分，并判断评审结果是否一致
     */
    private List<BidderResultDTO> getBidderDeductResultAvg(String[] gradeIds, Integer reviewType, Integer bidderId, Integer expertSize) {
        List<BidderResultDTO> result = new ArrayList<>();
        if (expertSize == 0) {
            return null;
        }
        List<GradeItem> gradeItems = gradeItemService.listGradeItemBySth(gradeIds, EvalProcess.DETAILED.getCode(), reviewType);
        for (GradeItem gradeItem : gradeItems) {
            List<ExpertReviewSingleItemDeductScore> deductScoreList = expertReviewSingleItemDeductScoreService.listDeductScoreBySth(gradeIds, bidderId, null, gradeItem.getId(), reviewType);
            BidderResultDTO newDto = BidderResultDTO.builder()
                    .bidderId(bidderId)
                    .gradeItemId(gradeItem.getId())
                    .isConsistent(true)
                    .build();
            String oldScore = null;
            double avgReslut = 0.0;
            for (ExpertReviewSingleItemDeductScore score : deductScoreList) {
                if (CommonUtil.isEmpty(oldScore)) {
                    oldScore = score.getEvalScore();
                }
                if (!oldScore.equals(score.getEvalScore())) {
                    newDto.setIsConsistent(false);
                }
                avgReslut += Double.parseDouble(score.getEvalScore());
            }
            avgReslut = avgReslut / expertSize;
            newDto.setArithmeticScore(new BigDecimal(avgReslut).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            result.add(newDto);
        }
        return result;
    }

    /**
     * 监理获取当前投标人 当前评审类型 每个gradeItem的平均分，并判断评审结果是否一致
     *
     * @param gradeIds   所有的gradeID
     * @param reviewType 评审类型
     * @param bidderId   投标人
     * @param expertSize 专家人数
     * @return 监理获取当前投标人 当前评审类型 每个gradeItem的平均分，并判断评审结果是否一致
     */
    private List<BidderResultDTO> getBidderScoreResultAvg(String[] gradeIds, Integer reviewType, Integer bidderId, Integer expertSize) {
        List<BidderResultDTO> result = new ArrayList<>();
        if (expertSize == 0) {
            return null;
        }
        List<GradeItem> gradeItems = gradeItemService.listGradeItemBySth(gradeIds, EvalProcess.DETAILED.getCode(), reviewType);
        for (GradeItem gradeItem : gradeItems) {
            List<ExpertReviewSingleItemScore> scoreList = expertReviewSingleItemScoreService.listScoreBySth(gradeIds, bidderId, null, gradeItem.getId(), EvalProcess.DETAILED.getCode(), reviewType);
            BidderResultDTO newDto = BidderResultDTO.builder()
                    .bidderId(bidderId)
                    .gradeItemId(gradeItem.getId())
                    .isConsistent(true)
                    .build();
            String oldScore = null;
            double avgReslut = 0.0;
            for (ExpertReviewSingleItemScore score : scoreList) {
                if (CommonUtil.isEmpty(oldScore)) {
                    oldScore = score.getEvalScore();
                }
                if (!oldScore.equals(score.getEvalScore())) {
                    newDto.setIsConsistent(false);
                }
                avgReslut += Double.parseDouble(score.getEvalScore());
            }
            avgReslut = avgReslut / expertSize;
            newDto.setArithmeticScore(new BigDecimal(avgReslut).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            result.add(newDto);
        }
        return result;
    }

    @Override
    public List<CandidateSuccess> getCanCandidatesResult() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        List<CandidateSuccess> result = candidateSuccessService.listCandidateSuccess(bidSectionId);

        for (CandidateSuccess candidate : result) {
            Bidder bidder = bidderService.getBidderById(candidate.getBidderId());
            BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfo(candidate.getBidderId(), bidSectionId);
            candidate.setBidderName(bidder.getBidderName());
            candidate.setBidderPrice(bidderOpenInfo.getBidPrice());
        }
        return result;
    }

    @Override
    public List<Bidder> listCanBidderVotes(Integer bidSectionId) {
        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);
        for (Bidder bidder : bidders) {
            List<Integer> voteNumList = new ArrayList<>();
            Integer bidderId = bidder.getId();
            voteNumList.add(getVoteNum(bidSectionId, bidderId, RankingConstant.FIRST_PLACE));
            voteNumList.add(getVoteNum(bidSectionId, bidderId, RankingConstant.SECOND_PLACE));
            voteNumList.add(getVoteNum(bidSectionId, bidderId, RankingConstant.THIRD_PLACE));
            bidder.setVoteNums(voteNumList);
        }
        return bidders;
    }


    /**
     * 获取某个投标人的某个排名的的票情况
     *
     * @param bidSectionId 标段id
     * @param bidderId     投标人id
     * @param ranking      排名
     * @return 投标人票数信息
     */
    private Integer getVoteNum(Integer bidSectionId, Integer bidderId, Integer ranking) {
        List<CandidateResults> candidates = candidateResultsService.listCandidate(CandidateResults.builder()
                .bidSectionId(bidSectionId)
                .ranking(ranking)
                .build());
        int voteNum = 0;
        for (CandidateResults candidate : candidates) {
            if (candidate.getBidderId().equals(bidderId)) {
                voteNum++;
            }
        }
        return voteNum;
    }

    @Override
    public List<Bidder> listEpcBidderQuoteScore(Integer bidSectionId) {
        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);
        for (Bidder bidder : bidders) {
            QuoteScoreResult quoteScoreResult = quoteScoreResultService.getQuoteScoreResultByBidderId(bidder.getId());
            bidder.setQuoteScoreResult(quoteScoreResult);
        }
        return bidders;
    }

    @Override
    public List<Bidder> listEpcBidderQuoteAppendixScore(Integer bidSectionId) {
        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);
        for (Bidder bidder : bidders) {
            QuoteScoreResultAppendix quoteScoreResultAppendix = quoteScoreResultAppendixService.getQuoteScoreResultAppendix(bidder.getId());
            QuoteScoreResult quoteScoreResult = quoteScoreResultService.getQuoteScoreResultByBidderId(bidder.getId());
            bidder.setQuoteScoreResult(quoteScoreResult);
            bidder.setQuoteScoreResultAppendix(quoteScoreResultAppendix);
        }
        return bidders;
    }

    @Override
    public List<Bidder> listQuaResultBidder(Integer bidSectionId) {
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

    @Override
    public boolean updateBackApply(Integer id, String checkStatus) {
        AuthUser user = CurrentUserHolder.getUser();
        // 获取当前回退申请
        FreeBackApply backApply = freeBackApplyService.getFreeBackApplyById(id);
        if (BackStatus.PASS.toString().equals(checkStatus)) {
            // 清除回退前数据
            boolean b = stepBack(backApply);
            if (!b) {
                log.error("回退数据清除失败");
                throw new CustomException("回退数据清除失败");
            }
            // 初始化推送信息
            Integer bidSectionId = user.getBidSectionId();
            BackPushStatus backPushStatus = BackPushStatus.builder()
                    .bidSectionId(bidSectionId)
                    .pushResult(0)
                    .backId(id)
                    .build();
            return backPushStatusService.initBackPush(backPushStatus) > 0;
        }
        return false;
    }

    @Override
    public boolean generateBackBeforeEvaluationData(FreeBackApply freeBackApply) throws Exception {
        AuthUser user = CurrentUserHolder.getUser();
        BidSection bidSection = bidSectionService.getBidSectionById(freeBackApply.getBidSectionId());
        BidProtype bidProtype = BidProtype.getBidProtypeByCode(bidSection.getBidClassifyCode());
        // 获取当前标段的所有专家
        List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSection.getId());
        if (CommonUtil.isEmpty(expertUsers)){
            return false;
        }
        List<TemplateNameEnum> listTemp = new ArrayList<>();
        // 按照项目类型，获取不同的回退数据模板
        switch (bidProtype) {
            case QUALIFICATION:
                listTemp = zgysService.listBackTempNameEnum(freeBackApply);
                break;
            case CONSTRUCTION:
                listTemp = sgService.listBackTempNameEnum(freeBackApply);
                break;
            case INVESTIGATION:
            case DESIGN:
            case ELEVATOR:
                // 勘察、设计、电梯
                listTemp = sjService.listBackTempNameEnum(freeBackApply);
                break;
            case SUPERVISION:
                listTemp = jlService.listBackTempNameEnum(freeBackApply);
                break;
            case EPC:
                listTemp = epcService.listBackTempNameEnum(freeBackApply);
                break;
        }
        // ================================用于初始化每个专家，合成pdf状态=========================================
        List<ExpertUserDto> expertUserDtos = new ArrayList<>();
        for (ExpertUser expertUser : expertUsers) {
            List<NowConvertPdf> convertPdfDtos = new ArrayList<>();
            for (TemplateNameEnum temp : listTemp) {
                NowConvertPdf nowConvertPdf = NowConvertPdf.builder()
                        .expertName(expertUser.getExpertName())
                        .templateName(temp.getTemplateChineseName())
                        .status(0)
                        .build();
                convertPdfDtos.add(nowConvertPdf);
            }
            ExpertUserDto expertUserDto = ExpertUserDto.builder()
                    .expertName(expertUser.getExpertName())
                    .nowConvertPdfs(convertPdfDtos)
                    .build();

            expertUserDtos.add(expertUserDto);
        }
        // 将当前合成的报告存入redis,用于前台进度展示
        RedisUtil.set(CacheName.ROLL_BACK_NOW_EXPERT + user.getUserId(), expertUserDtos);
        // =========================================================================
        // 生成回退前数据，上传至fdfs
        return generatePDFAndUpload(freeBackApply, expertUsers, bidProtype, listTemp);
    }

    @Override
    public boolean updateBackApplyById(Integer backApplyId, String checkStatus) {
        if (!CommonUtil.isEmpty(backApplyId)) {
            AuthUser user = CurrentUserHolder.getUser();
            // 获取当前回退申请
            FreeBackApply backApply = freeBackApplyService.getFreeBackApplyById(backApplyId);
            return freeBackApplyService.updateById(FreeBackApply.builder()
                    .id(backApply.getId())
                    .checkStatus(checkStatus)
                    .checkTime(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS))
                    .checkUser(user.getUserId())
                    .build());
        }
        return false;
    }

    @Override
    public List<BidderResultDTO> getGroupMutualResultData(Integer gradeId) {
        List<BidderResultDTO> result = new ArrayList<>();

        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();

        //筛选 通过初步评审的投标人
        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);

        for (Bidder bidder : bidders) {
            List<ExpertReviewMutual> groups = expertReviewMutualService.listMutualResultGroup(gradeId, bidder.getId());
            BidderResultDTO build = BidderResultDTO.builder()
                    .bidderId(bidder.getId()).build();
            if (!CommonUtil.isEmpty(groups) && groups.size() == 1) {
                build.setIsConsistent(true);
                build.setScore(groups.get(0).getEvalResult());
            } else {
                build.setIsConsistent(false);
            }
            result.add(build);
        }
        return result;
    }

    @Override
    public Map<String, Object> getBidderMutualResultData(Integer bidderId, Integer gradeId) {
        Map<String, Object> result = new HashMap<>();

        List<ExpertReviewMutual> expertReviewMutuals = expertReviewMutualService.listExpertReviewMutual(ExpertReviewMutual.builder()
                .gradeId(gradeId)
                .bidderId(bidderId).build(), false);

        result.put("expertReviewMutuals", expertReviewMutuals);

        List<ExpertReviewMutual> groups = expertReviewMutualService.listMutualResultGroup(gradeId, bidderId);
        result.put("groups", groups);
        return result;
    }

    @Override
    public Boolean reEvalThis(ReevalLog reevalLog) {
        try {
            AuthUser user = CurrentUserHolder.getUser();
            assert user != null;
            Integer bidSectionId = user.getBidSectionId();
            BidSectionRelate relate = bidSectionRelateService.getBidSectionRelateByBSId(bidSectionId);
            reevalLog.setBidSectionId(bidSectionId);
            reevalLog.setSubmitTime(DateUtil.formatLocalDateTime(LocalDateTime.now()));
            reevalLog.setUserId(user.getUserId());
            reevalLog.setUserName(user.getUsername());

            Fdfs fdfs = fdfsService.getFdfdById(relate.getEvaluationReportId());
            UUID uuid = UUID.randomUUID();
            //评标报告 拷贝
            Fdfs newFdfs = Fdfs.builder()
                    .mark("/reevalLog" + "/" + bidSectionId + "/" + uuid + ".pdf")
                    .name("reevalLog_report.pdf")
                    .dfsGroup(fdfs.getDfsGroup())
                    .dfsAddress(fdfs.getDfsAddress())
                    .url(fdfs.getUrl())
                    .suffix(fdfs.getSuffix())
                    .fileHash(fdfs.getFileHash())
                    .byteSize(fdfs.getByteSize())
                    .readSize(fdfs.getReadSize()).build();
            Integer reevalLogReportId = fdfsService.insertFdfs(newFdfs);
            reevalLog.setReEvalAnnexId(reevalLogReportId);

            bidSectionService.updateBidSectionById(BidSection.builder()
                    .id(bidSectionId)
                    .evalStatus(Status.PROCESSING.getCode())
                    .build());
            return reevalLogService.insertLog(reevalLog);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public List<EvalResultSg> listConRankingBidder(Integer bidSectionId) {
        //获取当前标段对应施工的排名表
        List<EvalResultSg> list = evalResultSgService.listRankingBidderByBsId(bidSectionId);
        //如果list为null则重新插入数据
        if (CommonUtil.isEmpty(list) || list.size() == 0) {
            //列表为空则重新进行数据封装
            list = evalResultSgService.addResultByBsId(bidSectionId);
        }
        return list;
    }


    /**
     * 进行环节回退
     *
     * @param freeBackApply 回退环节数据
     * @return
     */
    private boolean stepBack(FreeBackApply freeBackApply) {
        try {
            TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(freeBackApply.getBidSectionId());
            BidSection bidSection = bidSectionService.getBidSectionById(freeBackApply.getBidSectionId());
            BidProtype bidProtype = BidProtype.getBidProtypeByCode(bidSection.getBidClassifyCode());
            String[] gradeIds = tenderDoc.getGradeId().split(",");
            //回退环节判断
            Integer step = freeBackApply.getStep();
            Integer stepNow = freeBackApply.getStepNow();
            EvalProcess evalProcessNow = EvalProcess.getCode(stepNow);
            switch (bidProtype) {
                case QUALIFICATION:
                    switch (evalProcessNow) {
                        case RESULT:
                        case DETAILED:
                            deleteResultData(bidSection.getId());
                            if (step.equals(EvalProcess.DETAILED.getCode())) {
                                deleteDetailedForZGYSData(bidSection.getId(), gradeIds, false);
                                break;
                            }
                            deleteDetailedForZGYSData(bidSection.getId(), gradeIds, true);
                        case PRELIMINARY:
                            if (step.equals(EvalProcess.PRELIMINARY.getCode())) {
                                deletePreliminaryData(bidSection.getId(), gradeIds, false);
                                break;
                            }
                            deletePreliminaryData(bidSection.getId(), gradeIds, true);
                        case IS_CHAIR_MAN:
                            deleteIsChairManData(bidSection.getId());
                        default:
                    }
                    break;
                case SUPERVISION:
                    switch (evalProcessNow) {
                        case RESULT:
                        case DETAILED:
                            deleteResultData(bidSection.getId());
                            // 删除监理简易表
                            evalResultJlService.deleteByBsId(bidSection.getId());
                            if (step.equals(EvalProcess.DETAILED.getCode())) {
                                deleteDetailedForJLData(bidSection.getId(), gradeIds, false);
                                break;
                            }
                            deleteDetailedForJLData(bidSection.getId(), gradeIds, true);
                        case PRELIMINARY:
                            if (step.equals(EvalProcess.PRELIMINARY.getCode())) {
                                deletePreliminaryData(bidSection.getId(), gradeIds, false);
                                break;
                            }
                            deletePreliminaryData(bidSection.getId(), gradeIds, true);
                        case IS_CHAIR_MAN:
                            deleteIsChairManData(bidSection.getId());
                        default:
                    }
                    break;
                case DESIGN:
                case ELEVATOR:
                case INVESTIGATION:
                    switch (evalProcessNow) {
                        case RESULT:
                        case DETAILED:
                            deleteResultData(bidSection.getId());
                            if (step.equals(EvalProcess.DETAILED.getCode())) {
                                deleteDetailedForElectData(bidSection.getId(), gradeIds, false);
                                break;
                            }
                            deleteDetailedForElectData(bidSection.getId(), gradeIds, true);
                        case PRELIMINARY:
                            if (step.equals(EvalProcess.PRELIMINARY.getCode())) {
                                deletePreliminaryData(bidSection.getId(), gradeIds, false);
                                break;
                            }
                            deletePreliminaryData(bidSection.getId(), gradeIds, true);
                        case IS_CHAIR_MAN:
                            deleteIsChairManData(bidSection.getId());
                        default:
                    }
                    break;
                case EPC:
                    switch (evalProcessNow) {
                        case RESULT:
                        case CALC_PRICE_SCORE:
                            deleteResultData(bidSection.getId());
                            // 删除施工总承包简易表
                            evalResultEpcService.deleteByBsId(bidSection.getId());
                            if (step.equals(EvalProcess.CALC_PRICE_SCORE.getCode())) {
                                deleteCalcPriceScoreForEPCData(bidSection.getId(), gradeIds, false);
                                break;
                            }
                            deleteCalcPriceScoreForEPCData(bidSection.getId(), gradeIds, true);
                        case DETAILED:
                            if (step.equals(EvalProcess.DETAILED.getCode())) {
                                deleteDetailedForEPCData(bidSection.getId(), gradeIds, false);
                                break;
                            }
                            deleteDetailedForEPCData(bidSection.getId(), gradeIds, true);
                        case PRELIMINARY:
                            if (step.equals(EvalProcess.PRELIMINARY.getCode())) {
                                deletePreliminaryData(bidSection.getId(), gradeIds, false);
                                break;
                            }
                            deletePreliminaryData(bidSection.getId(), gradeIds, true);
                        case QUALIFICATION:
                            if (step.equals(EvalProcess.QUALIFICATION.getCode())) {
                                deleteQualificationData(bidSection.getId(), gradeIds, false);
                                break;
                            }
                            deleteQualificationData(bidSection.getId(), gradeIds, true);
                        case IS_CHAIR_MAN:
                            deleteIsChairManData(bidSection.getId());
                        default:
                    }
                    break;
                case CONSTRUCTION:
                    switch (evalProcessNow) {
                        case RESULT:
                        case OTHER:
                            if (Enabled.YES.getCode().equals(tenderDoc.getMutualSecurityStatus())) {
                                deleteResultData(bidSection.getId());
                                // 删除施工简易表
                                evalResultSgService.deleteByBsId(bidSection.getId());
                                String[] otherGradeIds = tenderDoc.getOtherGradeId().split(",");
                                if (step.equals(EvalProcess.OTHER.getCode())) {
                                    deleteMutualData(bidSection.getId(), otherGradeIds, false);
                                    break;
                                }
                                deleteMutualData(bidSection.getId(), otherGradeIds, true);
                            }
                        case CALC_PRICE_SCORE:
                            if (!Enabled.YES.getCode().equals(tenderDoc.getMutualSecurityStatus())) {
                                deleteResultData(bidSection.getId());
                                // 删除施工简易表
                                evalResultSgService.deleteByBsId(bidSection.getId());
                            }
                            // 更新标段信息
                            bidSectionService.updateBidSectionById(BidSection.builder()
                                    .id(bidSection.getId())
                                    .priceRecordStatus(0)
                                    .build());
                            bidSectionRelateService.updateRelateBySectionId(BidSectionRelate.builder()
                                    .bidSectionId(bidSection.getId())
                                    .priceScoreUid("")
                                    .calcPriceUid("")
                                    .build());
                            if (step.equals(EvalProcess.CALC_PRICE_SCORE.getCode())) {
                                break;
                            }
                        case DETAILED:
                            if (step.equals(EvalProcess.DETAILED.getCode())) {
                                deleteDetailedForSGData(bidSection.getId(), gradeIds, false);
                                break;
                            }
                            deleteDetailedForSGData(bidSection.getId(), gradeIds, true);
                        case PRELIMINARY:
                            if (step.equals(EvalProcess.PRELIMINARY.getCode())) {
                                deletePreliminaryData(bidSection.getId(), gradeIds, false);
                                break;
                            }
                            deletePreliminaryData(bidSection.getId(), gradeIds, true);
                        case IS_CHAIR_MAN:
                            deleteIsChairManData(bidSection.getId());
                            bidSectionRelateService.updateRelateBySectionId(BidSectionRelate.builder()
                                    .bidSectionId(bidSection.getId())
                                    .clearAnalysisUid("")
                                    .startClearTime("")
                                    .clearTotalTime("")
                                    .build());
                            tenderDocService.updateTenderDocById(TenderDoc.builder()
                                    .id(tenderDoc.getId())
                                    .structureStatus(0)
                                    .priceStatus(0)
                                    .fundBasisStatus(0)
                                    .mutualSecurityStatus(0)
                                    .build());

                        default:
                    }
                    break;
                default:
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 删除评标结果数据
     *
     * @param bidSectionId 标段id
     */
    private void deleteResultData(Integer bidSectionId) {
        // 更新标段信息
        bidSectionService.updateBidSectionById(BidSection.builder()
                .id(bidSectionId)
                .evalPdfGenerateStatus(0)
                .build());
        // 删除评标报告
        BidSectionRelate bidSectionRelate = bidSectionRelateService.getBidSectionRelateByBSId(bidSectionId);
        // 删除评标报告文件
        if (!CommonUtil.isEmpty(bidSectionRelate.getEvaluationReportId())) {
            Fdfs fdfs = fdfsService.getFdfdById(bidSectionRelate.getEvaluationReportId());
            fdfsService.delete(fdfs);
        }
        bidSectionRelateService.updateClearReportId(bidSectionRelate.getId());
    }

    /**
     * 删除专家推选数据
     *
     * @param bidSectionId 标段id
     */
    private void deleteIsChairManData(Integer bidSectionId) {
        // 更新标段信息
        bidSectionService.updateBidSectionById(BidSection.builder()
                .id(bidSectionId)
                .evalStatus(0)
                .evalStartTime("")
                .evalEndTime("")
                .evalPdfGenerateStatus(0)
                .evalReviewStatus("0")
                .reevalFlag(0)
                .cancelStatus(0)
                .cancelReason("")
                .scrapStatus(0)
                .scrapReason("")
                .build());
        // 清除评标报告记录
        BidSectionRelate bidSectionRelate = bidSectionRelateService.getBidSectionRelateByBSId(bidSectionId);
        // 删除评标报告文件
        if (!CommonUtil.isEmpty(bidSectionRelate.getEvaluationReportId())) {
            Fdfs fdfs = fdfsService.getFdfdById(bidSectionRelate.getEvaluationReportId());
            fdfsService.delete(fdfs);
        }
        bidSectionRelateService.updateClearReportId(bidSectionRelate.getId());
        // 专家状态置为最初状态
        List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);
        for (ExpertUser expertUser : expertUsers) {
            expertUserService.updateExpertById(ExpertUser.builder()
                    .id(expertUser.getId())
                    .avoid("2")
                    .leaderStatus("1")
                    .isChairman("")
                    .signar(0)
                    .build());
        }
        // 1、删除签名文件
        String expertSigarDir = ReportPath.REPORT_TEMPORARY_QM + bidSectionId;
        // 2、删除签名数据
        String signatureDir = ReportPath.REPORT_TEMPORARY_QZ + bidSectionId;
        FileUtil.removeDir(new File(expertSigarDir));
        FileUtil.removeDir(new File(signatureDir));

        // 更新评标申请记录数据
        BidApply bidApply = bidApplyService.getBidApplyByBidSectionId(bidSectionId);
        bidApplyService.updateClearChairManId(bidApply.getId());
        // 删除专家组长推选投票记录
        bidVoteService.deleteByBidApplyId(bidApply.getId());
    }

    /**
     * 资格审查数据清除
     *
     * @param bidSectionId         标段ID
     * @param gradeIds             所有的gradeId
     * @param isCurrentProcessData 是否删除当前环节数据
     */
    private void deleteQualificationData(Integer bidSectionId, String[] gradeIds, boolean isCurrentProcessData) {
        // 初步评审 删除打分制合格制的所有数据 以及结果
        // 初步评审部分
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.QUALIFICATION.getCode());
        List<Integer> idList = new ArrayList<>();
        for (Grade grade : grades) {
            idList.add(grade.getId());
        }
        Integer[] ids = new Integer[idList.size()];
        idList.toArray(ids);
        // 将小组评审结束置为未结束
        gradeService.updateGroupEndByIds(ids, Status.NOT_START.getCode());
        // 删除bidderReviewResult结果数据
        bidderReviewResultService.deleteByGradeIds(ids);
        // 是否需要删除本环节数据
        if (isCurrentProcessData) {
            // 删除expertReview
            expertReviewService.deleteByGradeIds(ids, bidSectionId);
            // 删除expertReviewSingleItem
            expertReviewSingleItemService.deleteByGradeIds(ids);
        } else {
            // 将个人评审结束置为未结束
            expertReviewService.updateAllExpertReview(ids, bidSectionId, Enabled.NO.getCode());
        }
    }

    /**
     * 初步评审数据清除
     *
     * @param bidSectionId         标段ID
     * @param gradeIds             所有的gradeId
     * @param isCurrentProcessData 是否删除当前环节数据
     */
    private void deletePreliminaryData(Integer bidSectionId, String[] gradeIds, boolean isCurrentProcessData) {
        // 初步评审 删除打分制合格制的所有数据 以及结果
        // 初步评审部分
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.PRELIMINARY.getCode());
        List<Integer> idList = new ArrayList<>();
        for (Grade grade : grades) {
            idList.add(grade.getId());
        }
        Integer[] ids = new Integer[idList.size()];
        idList.toArray(ids);
        //设置流标状态
        bidSectionService.updateBidSectionById(BidSection.builder()
                .id(bidSectionId)
                .cancelStatus(0)
                .cancelReason("")
                .build());

        // 将小组评审结束置为未结束
        gradeService.updateGroupEndByIds(ids, Status.NOT_START.getCode());
        // 删除bidderReviewResult结果数据
        bidderReviewResultService.deleteByGradeIds(ids);
        // 是否需要删除本环节数据
        if (isCurrentProcessData) {
            // 删除expertReview
            expertReviewService.deleteByGradeIds(ids, bidSectionId);
            // 删除expertReviewSingleItem
            expertReviewSingleItemService.deleteByGradeIds(ids);
        } else {
            // 将个人评审结束置为未结束
            expertReviewService.updateAllExpertReview(ids, bidSectionId, Enabled.NO.getCode());
        }
    }

    /**
     * 资格预审详细评审数据清除
     *
     * @param bidSectionId         标段ID
     * @param gradeIds             所有的gradeId
     * @param isCurrentProcessData 是否删除当前环节数据
     */
    private void deleteDetailedForZGYSData(Integer bidSectionId, String[] gradeIds, boolean isCurrentProcessData) {
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());
        List<Integer> idList = new ArrayList<>();
        for (Grade grade : grades) {
            idList.add(grade.getId());
        }
        Integer[] ids = new Integer[idList.size()];
        idList.toArray(ids);

        //设置流标状态
        bidSectionService.updateBidSectionById(BidSection.builder()
                .id(bidSectionId)
                .cancelStatus(0)
                .cancelReason("")
                .build());
        // 将小组评审结束置为未结束
        gradeService.updateGroupEndByIds(ids, Status.NOT_START.getCode());
        // 删除bidderReviewResult结果数据
        bidderReviewResultService.deleteByGradeIds(ids);
        // 是否需要删除本环节数据
        if (isCurrentProcessData) {
            // 删除expertReview
            expertReviewService.deleteByGradeIds(ids, bidSectionId);
            // 删除expertReviewSingleItem
            expertReviewSingleItemService.deleteByGradeIds(ids);
        } else {
            // 将个人评审结束置为未结束
            expertReviewService.updateAllExpertReview(ids, bidSectionId, Enabled.NO.getCode());
        }
    }

    /**
     * 施工详细评审数据清除
     *
     * @param bidSectionId         标段id
     * @param isCurrentProcessData 是否删除当前环节数据
     * @param gradeIds             所有的gradeId
     */
    private void deleteDetailedForSGData(Integer bidSectionId, String[] gradeIds, boolean isCurrentProcessData) {
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());
        List<Integer> idList = new ArrayList<>();
        for (Grade grade : grades) {
            idList.add(grade.getId());
        }
        Integer[] ids = new Integer[idList.size()];
        idList.toArray(ids);
        // 将小组评审结束置为未结束
        gradeService.updateGroupEndByIds(ids, Status.NOT_START.getCode());
        // 删除bidderReviewResultDeduct结果数据
        bidderReviewResultDeductService.deleteByGradeIds(ids);
        // 是否需要删除本环节数据
        if (isCurrentProcessData) {
            // 删除expertReview
            expertReviewService.deleteByGradeIds(ids, bidSectionId);
            // 删除expertReviewSingleItemDeduct
            expertReviewSingleItemDeductService.deleteByGradeIds(ids);
        } else {
            // 将个人评审结束置为未结束
            expertReviewService.updateAllExpertReview(ids, bidSectionId, Enabled.NO.getCode());
        }
    }

    /**
     * 监理详细评审数据清除
     *
     * @param bidSectionId         标段id
     * @param isCurrentProcessData 是否删除当前环节数据
     * @param gradeIds             所有的gradeId
     */
    private void deleteDetailedForJLData(Integer bidSectionId, String[] gradeIds, boolean isCurrentProcessData) {
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());
        List<Integer> idList = new ArrayList<>();
        for (Grade grade : grades) {
            idList.add(grade.getId());
        }
        Integer[] ids = new Integer[idList.size()];
        idList.toArray(ids);
        // 将小组评审结束置为未结束
        gradeService.updateGroupEndByIds(ids, Status.NOT_START.getCode());
        // 删除bidderReviewResultScore结果数据
        bidderReviewResultScoreService.deleteByGradeIds(ids);
        // 删除expertReviewSingleItemDeduct结果数据
        bidderReviewResultDeductService.deleteByGradeIds(ids);
        // 是否需要删除本环节数据
        if (isCurrentProcessData) {
            // 删除expertReview
            expertReviewService.deleteByGradeIds(ids, bidSectionId);
            // 删除expertReviewSingleItemDeductScore
            expertReviewSingleItemDeductScoreService.deleteByGradeIds(ids);
            // 删除expertReviewSingleItemScore
            expertReviewSingleItemScoreService.deleteByGradeIds(ids);
        } else {
            // 将个人评审结束置为未结束
            expertReviewService.updateAllExpertReview(ids, bidSectionId, Enabled.NO.getCode());
        }
    }

    /**
     * 施工总承包详细评审数据清除
     *
     * @param bidSectionId         标段id
     * @param isCurrentProcessData 是否删除当前环节数据
     * @param gradeIds             所有的gradeId
     */
    private void deleteDetailedForEPCData(Integer bidSectionId, String[] gradeIds, boolean isCurrentProcessData) {
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());
        List<Integer> idList = new ArrayList<>();
        for (Grade grade : grades) {
            idList.add(grade.getId());
        }
        Integer[] ids = new Integer[idList.size()];
        idList.toArray(ids);
        // 将小组评审结束置为未结束
        gradeService.updateGroupEndByIds(ids, Status.NOT_START.getCode());
        // 删除bidderReviewResult结果数据
        bidderReviewResultScoreService.deleteByGradeIds(ids);
        // 是否需要删除本环节数据
        if (isCurrentProcessData) {
            // 删除expertReview
            expertReviewService.deleteByGradeIds(ids, bidSectionId);
            // 删除 expertReviewSingleItemScore
            expertReviewSingleItemScoreService.deleteByGradeIds(ids);
        } else {
            // 将个人评审结束置为未结束
            expertReviewService.updateAllExpertReview(ids, bidSectionId, Enabled.NO.getCode());
        }
    }

    /**
     * 推选制详细评审数据清除（电梯，勘察、设计）
     *
     * @param bidSectionId         标段id
     * @param isCurrentProcessData 是否删除当前环节数据
     * @param gradeIds             所有的gradeId
     */
    private void deleteDetailedForElectData(Integer bidSectionId, String[] gradeIds, boolean isCurrentProcessData) {
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());
        List<Integer> idList = new ArrayList<>();
        for (Grade grade : grades) {
            idList.add(grade.getId());
        }
        Integer[] ids = new Integer[idList.size()];
        idList.toArray(ids);
        // 将小组评审结束置为未结束
        gradeService.updateGroupEndByIds(ids, Status.NOT_START.getCode());
        // 删除中标候选人数据
        candidateSuccessService.deleteByBidSectionId(bidSectionId);
        // 是否需要删除本环节数据
        if (isCurrentProcessData) {
            // 删除每个专家对本标段的推选候选人数据
            candidateResultsService.deleteByBidSectionId(bidSectionId);
        } else {
            // 将个人评审结束置为未结束
            candidateResultsService.updateAllCandidateResults(bidSectionId, Enabled.NO.getCode());
        }
    }

    /**
     * 互保共建 数据回退
     *
     * @param bidSectionId         标段ID
     * @param gradeIds             所有的gradeId
     * @param isCurrentProcessData 是否删除当前环节数据
     */
    private void deleteMutualData(Integer bidSectionId, String[] gradeIds, boolean isCurrentProcessData) {
        Grade grade = gradeService.listGrade(gradeIds, EvalProcess.OTHER.getCode()).get(0);

        // 将小组评审结束置为未结束
        gradeService.updateGrade(Grade.builder()
                .id(grade.getId())
                .groupEnd(Status.NOT_START.getCode())
                .build());
        Integer[] ids = new Integer[]{grade.getId()};

        // 是否需要删除本环节数据
        if (isCurrentProcessData) {
            // 删除expertReview
            expertReviewService.deleteByGradeIds(ids, bidSectionId);

            expertReviewMutualService.deleteByGradeIds(ids);
        } else {
            // 将个人评审结束置为未结束
            expertReviewService.updateAllExpertReview(ids, bidSectionId, Enabled.NO.getCode());
        }
    }

    /**
     * 施工总承包报价分计算数据清除
     *
     * @param bidSectionId         标段id
     * @param isCurrentProcessData 是否删除当前环节数据
     * @param gradeIds             所有的gradeId
     */
    private void deleteCalcPriceScoreForEPCData(Integer bidSectionId, String[] gradeIds, boolean isCurrentProcessData) {
        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.CALC_PRICE_SCORE.getCode());
        List<Integer> idList = new ArrayList<>();
        for (Grade grade : grades) {
            idList.add(grade.getId());
        }
        Integer[] ids = new Integer[idList.size()];
        idList.toArray(ids);
        // 将小组评审结束置为未结束
        gradeService.updateGroupEndByIds(ids, Status.NOT_START.getCode());
        // 是否需要删除本环节数据
        if (isCurrentProcessData) {
            // 删除expertReview
            expertReviewService.deleteByGradeIds(ids, bidSectionId);
            CalcScoreParam old = calcScoreParamService.getCalcScoreParamBySectionId(bidSectionId);
            calcScoreParamService.updateCalcScoreParam(CalcScoreParam.builder()
                    .id(old.getId())
                    .updateBasePrice("")
                    .basePrice("")
                    .build());
            quoteScoreResultAppendixService.deleteByBsId(bidSectionId);
            quoteScoreResultService.deleteByBsId(bidSectionId);
            BidSection build = BidSection.builder().id(bidSectionId).priceRecordStatus(0).updateScoreStatus(0).updateScoreReason("").build();
            bidSectionService.updateBidSectionById(build);
        } else {
            // 将个人评审结束置为未结束
            expertReviewService.updateAllExpertReview(ids, bidSectionId, Enabled.NO.getCode());
        }
    }

    /**
     * 生成每个专家的评审结果，最终以专家为单位合成
     * 回退报告
     *
     * @param freeBackApply     回退申请表
     * @param expertUsers       评审专家列表
     * @param bidProtype        项目类型
     * @param templateNameEnums 待生成的模板名称
     * @return
     */
    private boolean generatePDFAndUpload(FreeBackApply freeBackApply, List<ExpertUser> expertUsers, BidProtype bidProtype, List<TemplateNameEnum> templateNameEnums) throws Exception {
        AuthUser user = CurrentUserHolder.getUser();
        // 审核人员
        assert user != null;
        freeBackApply.setCheckUserName(user.getName());
        // 审核时间
        freeBackApply.setCheckTime(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS));
        BidSection bidSection = bidSectionService.getBidSectionById(freeBackApply.getBidSectionId());
        // 当前标段回退数据的目录
        String backOutPath = FileUtil.getBeforeRollbackDataFilePath(freeBackApply.getBidSectionId());
        RedisUtil.set("back_Apply:" + bidSection.getId(), 0);
        for (ExpertUser expertUser : expertUsers) {
            // 获取redis中的模板dto
            List<ExpertUserDto> expertUserDtos = (List<ExpertUserDto>) RedisUtil.get(CacheName.ROLL_BACK_NOW_EXPERT + user.getUserId());
            for (ExpertUserDto expertUserDto : expertUserDtos) {
                // 默认状态为加载中（0）
                List<NowConvertPdf> nowConvertPdfs = expertUserDto.getNowConvertPdfs();
                for (NowConvertPdf nowConvertPdf : nowConvertPdfs) {
                    if (expertUser.getExpertName().equals(expertUserDto.getExpertName())) {
                        nowConvertPdf.setStatus(0);
                    }
                }
                expertUserDto.setNowConvertPdfs(nowConvertPdfs);
            }
            // 将当前合成的报告存入redis,用于前台进度展示
            RedisUtil.set(CacheName.ROLL_BACK_NOW_EXPERT + user.getUserId(), expertUserDtos);

            ThreadUtlis.run(() -> {
                try {
                    // 生成当前专家所有的回退历史数据
                    // ================================用于显示每个专家，合成pdf状态=========================================
                    for (TemplateNameEnum templateNameEnum : templateNameEnums) {
                        // 模板的数据
                        Map<String, Object> map = evalReportService.getBackTempDataMap(expertUser, templateNameEnum, freeBackApply);
                        // 输出路径
                        String outPdfPath = FileUtil.getExpertUserBeforeDataPdfPath(expertUser) + File.separator + templateNameEnum.getName() + ".pdf";
                        // ftl转换pdf状态
                        boolean generateStatus = ReportPdfUtil.generateBackPdf(bidSection, map, templateNameEnum, outPdfPath);
                        // ================================用于初始化每个专家，合成pdf状态=========================================
                        RedissonUtil.lock(CacheName.EXPERT_USER_TEMPS + freeBackApply.getBidSectionId());
                        List<ExpertUserDto> expertUserDtosTemp= (List<ExpertUserDto>) RedisUtil.get(CacheName.ROLL_BACK_NOW_EXPERT + user.getUserId());

                        if(expertUserDtosTemp==null){
                            expertUserDtosTemp=expertUserDtos;
                        }

                        RedissonUtil.unlock(CacheName.EXPERT_USER_TEMPS + freeBackApply.getBidSectionId());

                        // 获取redis中的模板dto
                        for (ExpertUserDto expertUserDto : expertUserDtosTemp) {
                            if (expertUser.getExpertName().equals(expertUserDto.getExpertName())) {
                                // 默认状态为加载中（0）
                                List<NowConvertPdf> nowConvertPdfs = expertUserDto.getNowConvertPdfs();
                                for (NowConvertPdf nowConvertPdf : nowConvertPdfs) {
                                    if (nowConvertPdf.getTemplateName().equals(templateNameEnum.getTemplateChineseName())) {
                                        nowConvertPdf.setStatus(generateStatus ? 1 : 2);
                                    }
                                }
                                expertUserDto.setNowConvertPdfs(nowConvertPdfs);
                                break;
                            }
                        }
                        RedissonUtil.lock(CacheName.EXPERT_USER_TEMPS + freeBackApply.getBidSectionId());
                        // 将当前合成的报告存入redis,用于前台进度展示
                        RedisUtil.set(CacheName.ROLL_BACK_NOW_EXPERT + user.getUserId(), expertUserDtosTemp);
                        RedissonUtil.unlock(CacheName.EXPERT_USER_TEMPS + freeBackApply.getBidSectionId());
                        // =========================================================================
                    }
                    List<String> allExpertUserPdf = new ArrayList<>();
                    // 将每个pdf,按照顺序合成
                    for (TemplateNameEnum templateNameEnum : templateNameEnums) {
                        allExpertUserPdf.add(FileUtil.getExpertUserBeforeDataPdfPath(expertUser) + File.separator + templateNameEnum.getName() + ".pdf");
                    }
                    String outPath = backOutPath + File.separator + ProjectFileTypeConstant.BACK_DATA + File.separator  + expertUser.getId() + ".pdf";
                    // 当前专家，最终的评标数据
                    ReportPdfUtil.mergeBackReportPdf(allExpertUserPdf, outPath);
                    // 插入页码
                    PDFUtil.writePageNum(outPath, 15, 2);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    RedissonUtil.lock("generatePDFAndUpload_size" + bidSection.getId());
                    try {
                        Integer expertHaveFinsh = (Integer) RedisUtil.get("back_Apply:" + bidSection.getId());
                        RedisUtil.set("back_Apply:" + bidSection.getId(), expertHaveFinsh + 1);
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        RedissonUtil.unlock("generatePDFAndUpload_size" + bidSection.getId());
                    }
                }

            });
        }

        while (!RedisUtil.get("back_Apply:" + bidSection.getId()).equals(expertUsers.size())) {
            Thread.sleep(3000);
        }

        // 所有专家pdf存放路径
        String allExpertUserPdfPath = FileUtil.getBackAllEvalReportFilePath(bidSection.getId());
        // 获取所有专家的文件绝对路径，准备合成
        List<String> allExpertUserPdf = FileUtil.listAbsolutePath(allExpertUserPdfPath, "pdf");
        // 最终合成路径
        String outPath = FileUtil.getBeforeRollbackDataFilePath(bidSection.getId()) + File.separator + ProjectFileTypeConstant.BEFORE_ROLLBACK_DATA + ".pdf";
        // 合成评标报告
        PDFUtil.mergePdfs(allExpertUserPdf, outPath);
        //构造FastDfs 文件Mark
        String backMark = File.separator + ProjectFileTypeConstant.BEFORE_ROLLBACK_DATA +
                File.separator + bidSection.getId() + File.separator + ProjectFileTypeConstant.BEFORE_ROLLBACK_DATA + ".pdf";
        // 把本地回退数据，上传至文件服务器
        Fdfs fdfs = fdfsService.upload(new File(outPath), backMark);
        freeBackApplyService.updateById(FreeBackApply.builder()
                .id(freeBackApply.getId())
                .freeBackAnnexId(fdfs.getId())
                .build());
        // 删除本地数据
        FileUtil.deleteFile(new File(FileUtil.getBeforeRollbackDataFilePath(bidSection.getId())));
        return true;
    }

}
