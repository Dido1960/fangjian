package com.ejiaoyi.common.service.impl;

import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.EvalProcess;
import com.ejiaoyi.common.enums.TemplateNameEnum;
import com.ejiaoyi.common.service.IBidderOpenInfoService;
import com.ejiaoyi.common.service.ICandidateResultsService;
import com.ejiaoyi.common.service.ICandidateSuccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 【设计】获取评标报告模板数据
 * @author fengjunhong
 * @since 2020-11-26
 */
@Service
public class SjServiceImpl extends ReportServiceImpl {

    @Autowired
    IBidderOpenInfoService bidderOpenInfoService;
    @Autowired
    ICandidateResultsService candidateResultsService;
    @Autowired
    ICandidateSuccessService candidateSuccessService;

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
        // 推荐中标候选人名单
        list.add(TemplateNameEnum.PUSH_WIN_BIDDERS);
        // 评委推荐表
        list.add(TemplateNameEnum.PUSH_WIN_BIDDERS_SINGLE);
        // 评委投票统计表
        list.add(TemplateNameEnum.VOTE_COUNT_FORM);
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
    public List<TemplateNameEnum> listBackTempNameEnum(FreeBackApply freeBackApply) {
        List<TemplateNameEnum> list = new ArrayList<>();
        //回退前环节
        Integer stepNow = freeBackApply.getStepNow();
        // 要回退的环节
        Integer step = freeBackApply.getStep();
        EvalProcess evalProcessNow = EvalProcess.getCode(stepNow);
        EvalProcess evalProcess = EvalProcess.getCode(step);
        switch (evalProcessNow){
            case DETAILED:
                list.add(TemplateNameEnum.VOTE_COUNT_FORM);
                list.add(TemplateNameEnum.PUSH_WIN_BIDDERS_SINGLE);
                list.add(TemplateNameEnum.QUALIFIED_BIDDERS);
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
    public  Map<String, Object> getSjReportData(Integer bidSectionId,TemplateNameEnum templateNameEnum){
        // 获取当前标段，报告所需的所有模板
        switch (templateNameEnum){
            case COVER_REPORT:
                return this.getCoverReportData(bidSectionId);
            case PRELIMINARY_EVALUATION:
                return this.getFirstStepDataMap(bidSectionId);
            case QUALIFIED_BIDDERS:
                return this.getQuoteScoreDataMap(bidSectionId);
            case PUSH_WIN_BIDDERS:
                return this.getDetailReviewDataMap(bidSectionId);
            case PUSH_WIN_BIDDERS_SINGLE:
                return this.getExpertPersonTuiJiForm(bidSectionId);
            case VOTE_COUNT_FORM:
                return this.getVoteCountForm(bidSectionId);
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
        Map<String, Object> map = super.getCoverReportData(bidSectionId);
        // 中标候选人
        List<CandidateSuccess> listCandidateSuccesses = candidateSuccessService.listCandidateSuccess(bidSectionId);
        for (CandidateSuccess success : listCandidateSuccesses) {
            if (success.getBidderId() != null) {
                // 投标人开标信息
                BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfo(success.getBidderId(), success.getBidSectionId());
                success.setBidderName(bidderService.getBidderById(success.getBidderId()).getBidderName());
                success.setBidderPrice(bidderOpenInfo.getBidPrice());
                success.setBidderPriceType(bidderOpenInfo.getBidPriceType());
            }

        }
        // 按照排名依次排序
        listCandidateSuccesses = listCandidateSuccesses.stream().sorted(Comparator.comparing(CandidateSuccess::getRanking)).collect(Collectors.toList());
        map.put("listCandidateSuccesses",listCandidateSuccesses);
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
        Map<String,Object> map = new HashMap<>();
        // 合格投标人名单 => 通过初步评审名单
        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);
        map.put("qualifiedBidders",bidders);
        // 小组推荐中标候选人名单
        List<CandidateSuccess> listCandidateSuccesses = candidateSuccessService.listCandidateSuccess(bidSectionId);
        for (CandidateSuccess success : listCandidateSuccesses) {
            if (success.getBidderId() != null) {
                // 投标人开标信息
                BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfo(success.getBidderId(), success.getBidSectionId());
                success.setBidderName(bidderService.getBidderById(success.getBidderId()).getBidderName());
                success.setBidderPrice(bidderOpenInfo.getBidPrice());
                success.setBidderPriceType(bidderOpenInfo.getBidPriceType());
            }
        }
        // 按照推荐名次进行排序
        Collections.sort(listCandidateSuccesses, new Comparator<CandidateSuccess>() {
            @Override
            public int compare(CandidateSuccess arg0, CandidateSuccess arg1) {
                return arg0.getRanking().compareTo(arg1.getRanking());
            }
        });
        map.put("listCandidateSuccess",listCandidateSuccesses);
        return map;
    }

    /**
     * 评委推荐表
     * @param bidSectionId 标段主键
     * @return
     */
    public Map<String,Object> getExpertPersonTuiJiForm(Integer bidSectionId){
        Map<String,Object> map = new HashMap<>();
        // 推荐候选人结果表
        List<CandidateResults> candidateResults = candidateResultsService.listCandidate(new CandidateResults().setBidSectionId(bidSectionId));
        for (CandidateResults results : candidateResults) {
            // 投标人开标信息
            BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfo(results.getBidderId(), results.getBidSectionId());
            results.setBidderName(bidderService.getBidderById(results.getBidderId()).getBidderName());
            results.setBidderPrice(bidderOpenInfo.getBidPrice());
            results.setBidderPriceType(bidderOpenInfo.getBidPriceType());
        }
        // 按照专家id,排序方便前台数据展示
        candidateResults = candidateResults.stream().sorted(Comparator.comparing(CandidateResults::getExpertId)).collect(Collectors.toList());
        // 合格投标人名单 => 通过初步评审名单
        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);
        map.put("listCandidateResults",candidateResults);
        map.put("bidders",bidders);
        return map;
    }


    /**
     * 评委投票统计表
     * @param bidSectionId 标段主键
     * @return
     */
    public Map<String,Object> getVoteCountForm(Integer bidSectionId){
        Map<String,Object> map = new HashMap<>();
        // 推荐候选人结果表
        List<CandidateResults> candidateResults = candidateResultsService.listCandidate(new CandidateResults().setBidSectionId(bidSectionId));
        // 按照专家id,排序方便前台数据展示
        candidateResults = candidateResults.stream().sorted(Comparator.comparing(CandidateResults::getExpertId)).collect(Collectors.toList());
        // 合格投标人名单 => 通过初步评审名单
        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);
        for (Bidder bidder : bidders) {
            // 推荐候选人结果表
            CandidateResults candidateResult = CandidateResults.builder()
                    .bidSectionId(bidSectionId)
                    .bidderId(bidder.getId())
                    .build();
            bidder.setOneBidderVotes(candidateResultsService.listCandidate(candidateResult.setRanking(1)).size());
            bidder.setTwoBidderVotes(candidateResultsService.listCandidate(candidateResult.setRanking(2)).size());
            bidder.setThreeBidderVotes(candidateResultsService.listCandidate(candidateResult.setRanking(3)).size());
        }
        map.put("listCandidateResults",candidateResults);
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
        return super.getQuoteScoreDataMap(bidSectionId);
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
        return getDetailReviewDataMap(bidSectionId);
    }

}
