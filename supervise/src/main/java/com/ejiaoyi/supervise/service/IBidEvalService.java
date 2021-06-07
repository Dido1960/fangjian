package com.ejiaoyi.supervise.service;

import com.ejiaoyi.common.dto.BidderResultDTO;
import com.ejiaoyi.common.entity.*;

import java.util.List;
import java.util.Map;

/**
 * 监管 评标流程接口
 * @auther: liuguoqiang
 * @Date: 2020-12-7 16:39
 */
public interface IBidEvalService {

    /**
     * 获取当前评标流程进行情况
     *
     * @param bidSectionId 标段ID
     * @return 流程状态 key 流程名称：(对应 EvalProcessGov：processName); value 进行状态（对应 Status 0：未开始 1：进行中 2：已完成）
     */
    Map<String, Integer> listProcessStatus(Integer bidSectionId);

    /**
     * 获取资格审查 每个投标人当前grade的最后结果
     *
     * @param gradeId gradeId
     * @return 获取每个投标人当前grade的最后结果
     */
    List<BidderReviewResult> getQualifyBiddersGradeResult(Integer gradeId);

    /**
     * 获取合格制 当前投标人 小组评审结果数据
     *
     * @param bidderId 投标人ID
     * @param gradeId  gradeID
     * @return 小组评审结果数据
     */
    Map<String, Object> getBidderQualifiedData(Integer bidderId, Integer gradeId);

    /**
     * 获取初步评审 每个投标人当前grade的最后结果
     *
     * @param gradeId gradeId
     * @return 获取每个投标人当前grade的最后结果
     */
    List<BidderReviewResult> getPreBiddersGradeResult(Integer gradeId);

    /**
     * 总承包 详细评审 获取小组 投标人结果数据
     *
     * @return 获取小组 投标人 结果数据
     */
    List<BidderResultDTO> getEpcDetailedGroupBiddersResult();

    /**
     * 总承包 详细评审 小组汇总 当前投标人 结果数据
     *
     * @param bidderId 投标人ID
     * @return 获取小组 当前投标人 结果数据
     */
    Map<String, Object> getEpcDetailedGroupBidderResult(Integer bidderId);

    /**
     * 获取施工 每个投标人当前grade的最后结果
     *
     * @param gradeId gradeId
     * @return 获取每个投标人当前grade的最后结果
     */
    List<BidderReviewResultDeduct> getConDetailedGroupBiddersResult(Integer gradeId);

    /**
     * 获取施工详细评审小组评审结果数据
     *
     * @param bidderId 投标人ID
     * @param gradeId  gradeID
     * @return 细评审小组评审结果数据
     */
    Map<String, Object> getConDetailedGroupBidderResult(Integer bidderId, Integer gradeId);

    /**
     * 监理 获取每个投标人当前reviewType的最后结果
     *
     * @param reviewType reviewType
     * @return 获取每个投标人当前reviewType的最后结果
     */
    List<BidderResultDTO> getSupDetailedGroupBiddersResult(Integer reviewType);

    /**
     * 监理 小组汇总 获取投标人结果数据
     *
     * @param bidderId   投标人ID
     * @param reviewType 评审类型
     * @return 获取投标人结果数据
     */
    Map<String, Object> getSupDetailedGroupBidderResult(Integer bidderId, Integer reviewType);

    /**
     * 推荐投标人类型 推荐投标人流程 获取推荐排名
     *
     * @return 推荐投标人类型 详细评审 获取推荐排名
     */
    List<CandidateSuccess> getCanCandidatesResult();

    /**
     * 详细评审 为推荐投标人 获取投标人 排名得票数
     *
     * @param bidSectionId 标段Id
     * @return 排名得票数
     */
    List<Bidder> listCanBidderVotes(Integer bidSectionId);

    /**
     * 总承包 获取投标人报价得分
     *
     * @param bidSectionId 标段信息
     * @return 获取投标人报价得分
     */
    List<Bidder> listEpcBidderQuoteScore(Integer bidSectionId);

    /**
     * 总承包 获取投标人修正得报价得分
     *
     * @param bidSectionId 标段信息
     * @return 获取投标人修正得报价得分
     */
    List<Bidder> listEpcBidderQuoteAppendixScore(Integer bidSectionId);

    /**
     * 资格预审参 初步评审和详细评审评审结果
     *
     * @param bidSectionId 标段id
     * @return 初步评审和详细评审评审结果
     */
    List<Bidder> listQuaResultBidder(Integer bidSectionId);

    /**
     * 更新回退审核状态，并清除相应环节的数据内容
     *
     * @param id          回退审核id
     * @param checkStatus 审核状态
     * @return 操作情况
     */
    boolean updateBackApply(Integer id, String checkStatus);

    /**
     * 保存回退前的历史数据，生成评标记录
     *
     * @param freeBackApply 回退申请表
     * @return
     */
    boolean generateBackBeforeEvaluationData(FreeBackApply freeBackApply) throws Exception;

    /**
     * 更新回退申请
     *
     * @param backApplyId 回退申请表主键
     * @param checkStatus 审核状态
     * @return
     */
    boolean updateBackApplyById(Integer backApplyId, String checkStatus);

    /**
     * 互保共建 获取所有投标人的结果数据
     * @param gradeId gradeId
     * @return 获取所有投标人的结果数据
     */
    List<BidderResultDTO> getGroupMutualResultData(Integer gradeId);

    /**
     * 互保共建 获取专家的结果数据
     *
     * @param bidderId       投标人ID
     * @param gradeId        gradeId
     */
    Map<String, Object> getBidderMutualResultData(Integer bidderId, Integer gradeId);

    /**
     * 复议开启
     * @param reevalLog 数据
     * @return
     */
    Boolean reEvalThis(ReevalLog reevalLog);


    List<EvalResultSg> listConRankingBidder(Integer bidSectionId);
}
