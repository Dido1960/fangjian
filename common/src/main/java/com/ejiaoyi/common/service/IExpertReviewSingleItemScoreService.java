package com.ejiaoyi.common.service;

import com.ejiaoyi.common.dto.BidderResultDTO;
import com.ejiaoyi.common.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 专家对企业评审单项评审结果 打分制 服务类
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
public interface IExpertReviewSingleItemScoreService extends IService<ExpertReviewSingleItemScore> {

    /**
     * 初始化专家评审信息(打分项)
     * @param bidSectionId 标段id
     * @param expertId 专家id
     * @param grades 当前的所以评审环节
     * @param bidders 包含的投标人
     * @return 初始化数据 获取第一个grade的评审信息作为代表
     */
    ExpertReview initReviewItemScore(Integer bidSectionId, Integer expertId, List<Grade> grades, List<Bidder> bidders);

    /**
     * 获取已经打了评分的列表
     * @param expertId 专家ID
     * @param gradeId gradeID
     * @param bidderId 投标人ID
     * @return
     */
    List<ExpertReviewSingleItemScore> listHasScore(Integer expertId, Integer gradeId, Integer bidderId);

    /**
     * 对grades封装具体的评审结果
     * @param grades grades
     * @param bidderId 当前投标人
     * @param expertId 专家Id
     * @return 对grades封装具体的评审结果
     */
    List<Grade> listGradeForBidderResult(List<Grade> grades, Integer bidderId, Integer expertId);

    /**
     * 按照条件查询结果列表
     * @param expertReviewSingleItemScore 查询条件
     * @return 按照条件查询结果列表
     */
    List<ExpertReviewSingleItemScore> listScoreResult(ExpertReviewSingleItemScore expertReviewSingleItemScore);

    /**
     * 通过ID获取ExpertReviewSingleItemScore 分装Item
     * @param id id
     * @return 通过ID获取ExpertReviewSingleItemScore
     */
    ExpertReviewSingleItemScore getScoreById(Integer id);

    /**
     * 通过条件 获取当前专家对于当前评审类型已经打分的评审项
     * @param gradeIds 所有的gradeID
     * @param bidderId 投标人id
     * @param expertId 专家id
     * @param gradeItemId gradeItemId
     * @param evalProcess 评审环节
     * @param reviewType 评审类型
     * @return 获取当前专家对于当前评审类型已经打分的评审项
     */
    List<ExpertReviewSingleItemScore> listHasScoreBySth(String[] gradeIds, Integer bidderId, Integer expertId, Integer gradeItemId, Integer evalProcess, Integer reviewType);

    /**
     * 通过专家ID 以及gradeId 获取已经评审的评审项
     * @param expertId 专家ID
     * @param gradeId gradeId
     * @return 获取已经评审的评审项
     */
    List<ExpertReviewSingleItemScore> listAllHasScore(Integer expertId, Integer gradeId);

    /**
     * 通过条件 获取所有的评审结果
     * @param gradeIds 所有的gradeID
     * @param bidderId 投标人ID
     * @param expertId 专家id
     * @param gradeItemId gradeItemId
     * @param evalProcess 评审环节
     * @param reviewType 评审类型
     * @return 通过bidderId获取所有的评审结果
     */
    List<ExpertReviewSingleItemScore> listScoreBySth(String[] gradeIds, Integer bidderId, Integer expertId, Integer gradeItemId, Integer evalProcess, Integer reviewType);

    /**
     * 检查打分是否一致
     * @param gradeIds 所有的gradeID
     * @param reviewType 评审类型
     * @param bidderNum 投标人数量
     * @return 检查打分是否一致
     */
    boolean checkScoreConsistent(String[] gradeIds, Integer reviewType, Integer bidderNum);

    /**
     * 获取平均打分
     * @param gradeId gradeID
     * @param bidderId 投标人ID
     * @param size 专家人数
     * @return 获取平均打分
     */
    String getAvgScore(Integer gradeId, Integer bidderId, Integer size);

    /**
     * 判断当前投标人当前reviewType是否打分一致
     * @param gradeIds 所有的gradeids
     * @param reviewType 评审类型
     * @param bidderId 投标人ID
     * @param itemNum gradeItem数量
     * @return 判断当前投标人当前reviewType是否打分一致
     */
    Boolean checkBidderResultConsistent(String[] gradeIds, Integer reviewType, Integer bidderId, Integer itemNum);

    /**
     * 获取当前投标人当前 查询类型的 的平均分
     * @param gradeIds 所有的gradeids
     * @param evalProcess 评审环节
     * @param reviewType 评审类型
     * @param bidderId 投标人ID
     * @param expertNum 专家人数
     * @return 获取当前投标人 当前查询类型的 的平均分
     */
    String getAvgScoreForReview(String[] gradeIds, Integer evalProcess , Integer reviewType, Integer bidderId, Integer expertNum);

    /**
     * 通过条件获取平均分，当专家人数大于5时采用去除一个最低分去除一个最高分进行计算。
     * 当 bidderId 为null时 返回数据为当前标段所有的投标人打分数据！
     * @param gradeIds 需要查询的gradeIDs
     * @param gradeId gradeId
     * @param bidSectionId 标段ID
     * @param bidderId 投标人id
     * @param gradeItemId itemID
     * @return 结果数据
     */
    List<BidderResultDTO> getAvgScoreBySth(String[] gradeIds, Integer gradeId, Integer bidSectionId, Integer bidderId, Integer gradeItemId);

    /**
     * 通过gradeIDs批量删除
     * @param gradeIds gradeIds
     * @return
     */
    Integer deleteByGradeIds(Integer[] gradeIds);
}
