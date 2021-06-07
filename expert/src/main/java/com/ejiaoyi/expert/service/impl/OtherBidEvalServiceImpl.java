package com.ejiaoyi.expert.service.impl;

import com.ejiaoyi.common.annotation.RedissonLock;
import com.ejiaoyi.common.constant.RankingConstant;
import com.ejiaoyi.common.dto.BidderRankingDTO;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.EvalProcess;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.service.impl.BaseServiceImpl;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.expert.service.IBidEvalService;
import com.ejiaoyi.expert.service.IOtherBidEvalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 勘察设计电梯专家评标 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-09-07
 */
@Service
public class OtherBidEvalServiceImpl extends BaseServiceImpl implements IOtherBidEvalService {

    @Autowired
    private IBidderService bidderService;

    @Autowired
    private IExpertUserService expertUserService;

    @Autowired
    private ICandidateResultsService candidateResultsService;

    @Autowired
    private ICandidateSuccessService candidateSuccessService;

    @Autowired
    private ITenderDocService tenderDocService;

    @Autowired
    private IGradeService gradeService;

    @Autowired
    private IBidEvalService bidEvalService;

    @Override
    public Map<String, Object> validGroupReview(Integer bidSectionId, Integer evalProcess) {
        Map<String, Object> map = new HashMap<>(2);
        if (bidEvalService.isFreeBackApplying(bidSectionId)){
            map.put("isFreeBackApplying", true);
            return map;
        } else {
            map.put("isFreeBackApplying", false);
        }

        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);
        List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);
        boolean allEvalStatus = true;
        StringBuilder noEndExpertNames = new StringBuilder();
        int j = 1;
        for (ExpertUser expertUser : expertUsers) {
            CandidateResults candidate = candidateResultsService.getCandidateByRanking(bidSectionId, expertUser.getId(), RankingConstant.FIRST_PLACE);
            if (candidate == null || CommonUtil.isEmpty(candidate.getIsEnd()) || candidate.getIsEnd() != 1) {
                noEndExpertNames.append(++j).append("、").append(expertUser.getExpertName()).append("<br>");
                allEvalStatus = false;
            }
        }
        noEndExpertNames.insert(0, "<b>以下专家未结束:</b><br>");

        boolean isHasResult = false;
        // 个人评审结束后统计投票结果
        if (allEvalStatus) {
            // 校验中标候选人是否存在和是否重复,原因是否为空
            CandidateSuccess firstCs = this.getSuccessCandidate(bidSectionId, RankingConstant.FIRST_PLACE, null);
            CandidateSuccess secondCs = this.getSuccessCandidate(bidSectionId, RankingConstant.SECOND_PLACE, firstCs);
            if (bidders.size() > 2) {
                CandidateSuccess thirdCs = this.getSuccessCandidate(bidSectionId, RankingConstant.THIRD_PLACE, secondCs);
                isHasResult = firstCs != null && secondCs != null && thirdCs != null
                        && !firstCs.getBidderId().equals(secondCs.getBidderId())
                        && !secondCs.getBidderId().equals(thirdCs.getBidderId())
                        && !firstCs.getBidderId().equals(thirdCs.getBidderId())
                        && !CommonUtil.isEmpty(firstCs.getReason())
                        && !CommonUtil.isEmpty(secondCs.getReason())
                        && !CommonUtil.isEmpty(thirdCs.getReason());
            }
            if (bidders.size() == 2) {
                isHasResult = firstCs != null && secondCs != null
                        && !firstCs.getBidderId().equals(secondCs.getBidderId())
                        && !CommonUtil.isEmpty(firstCs.getReason())
                        && !CommonUtil.isEmpty(secondCs.getReason());
            }
        }

        map.put("allEvalStatus", allEvalStatus);
        map.put("isHasResult", isHasResult);
        map.put("noEndExpertNames", noEndExpertNames.toString());
        return map;
    }

    @Override
    public CandidateSuccess getSuccessCandidate(Integer bidSectionId, Integer ranking, CandidateSuccess lastBidder) {
        // 查看中标候选人表是否有数据
        CandidateSuccess candidateSuccess = candidateSuccessService.getCandidateSuccess(bidSectionId, ranking);
        if (candidateSuccess != null && !CommonUtil.isEmpty(candidateSuccess.getBidderId())) {
            Bidder bidder = bidderService.getBidderById(candidateSuccess.getBidderId());
            bidder.setCandidateSuccess(candidateSuccess);
            return candidateSuccess;
        }
        if (ranking != 1 && (lastBidder == null || lastBidder.getBidderFrom() == 2)) {
            return null;
        }

        // 统计投标人相应名次的票数  排数排序
        List<BidderRankingDTO> bidderRankingDtoList = candidateResultsService.listBidderRanking(bidSectionId, ranking);

        //最大票数是否唯一
        boolean isMaxOnlyOne = true;
        if (bidderRankingDtoList.size() > 1 && bidderRankingDtoList.get(0).getPoll() - bidderRankingDtoList.get(1).getPoll() == 0) {
            isMaxOnlyOne = false;
        }
        // 校验系统推选的候选人是否重复
        CandidateSuccess candidate = null;
        if (bidderRankingDtoList.size() > 0){
            candidate = candidateSuccessService.getCandidate(bidSectionId, bidderRankingDtoList.get(0).getBidderId());
        }
        // 投标人票数最高唯一，且未被推选过其它名次
        if (isMaxOnlyOne && candidate == null) {
            CandidateSuccess cs = CandidateSuccess.builder()
                    .bidSectionId(bidSectionId)
                    .bidderId(bidderRankingDtoList.get(0).getBidderId())
                    .ranking(ranking)
                    .reason("评标委员会成员推选结果")
                    .bidderFrom(1)
                    .build();
            if (candidateSuccessService.addCandidateSuccess(cs)) {
                return cs;
            }
        }

        return null;
    }

    @Override
    public List<Bidder> getBidderVoteNum(Integer bidSectionId) {
//        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
//        String[] gradeIds = tenderDoc.getGradeId().split(",");
//        List<Grade> grades = gradeService.listGrade(gradeIds, EvalProcess.DETAILED.getCode());
//        Integer gradeId = grades.get(0).getId();
//        Grade grade = bidEvalService.getGradeDetailItem(bidSectionId, gradeId, EvalProcess.DETAILED.getCode());

        List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);
        boolean allEvalStatus = true;
        for (ExpertUser expertUser : expertUsers) {
            CandidateResults candidate = candidateResultsService.getCandidateByRanking(bidSectionId, expertUser.getId(), RankingConstant.FIRST_PLACE);
            if (candidate == null || CommonUtil.isEmpty(candidate.getIsEnd()) || candidate.getIsEnd() != 1) {
                allEvalStatus = false;
            }
        }

        //所有专家个人结束后统计票数
        if (allEvalStatus) {
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
        return null;
    }

    @Override
    public Boolean validRecommend(Integer expertId, Integer bidSectionId) {
        List<CandidateResults> candidateResults = candidateResultsService.listCandidate(expertId, bidSectionId);
        Set<Integer> bidderIdSet = new HashSet<>();
        for (CandidateResults candidateResult : candidateResults) {
            bidderIdSet.add(candidateResult.getBidderId());
        }
        return bidderIdSet.size() == candidateResults.size();
    }

    @Override
    public String validLeaderRecommend(Integer bidSectionId) {
        List<CandidateSuccess> candidateSuccesses = candidateSuccessService.listCandidateSuccess(bidSectionId);
        List<Bidder> bidders = bidderService.listDetailedBidder(bidSectionId);
        if (bidders.size() > 2 && candidateSuccesses.size() != 3) {
            return "请完善候选人推选!";
        }
        if (bidders.size() <= 2 && candidateSuccesses.size() != bidders.size()) {
            return "请完善候选人推选!";
        }
        Set<Integer> bidderIdSet = new HashSet<>();
        for (CandidateSuccess candidateSuccess : candidateSuccesses) {
            bidderIdSet.add(candidateSuccess.getBidderId());
        }
        if (bidderIdSet.size() != candidateSuccesses.size()) {
            return "候选人推荐存在重复!";
        }
        return null;
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


}
