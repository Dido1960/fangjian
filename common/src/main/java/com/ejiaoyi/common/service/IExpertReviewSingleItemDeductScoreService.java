package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.*;

import java.util.List;

/**
 * 扣分打分接口
 * @Auther: liuguoqiang
 * @Date: 2020-11-18 15:46
 */
public interface IExpertReviewSingleItemDeductScoreService {

    /**
     * 初始化专家评审信息(打分扣分项)
     * @param bidSectionId 标段id
     * @param expertId 专家id
     * @param grades 当前的所以评审环节
     * @param bidders 包含的投标人
     * @return 初始化数据 获取第一个grade的评审信息作为代表
     */
    ExpertReview initReviewItemDeductScore(Integer bidSectionId, Integer expertId, List<Grade> grades, List<Bidder> bidders);

    /**
     * 获取已经打了评分的列表
     * @param expertId 专家ID
     * @param gradeId gradeID
     * @param bidderId 投标人ID
     * @return 获取已经打了评分的列表
     */
    List<ExpertReviewSingleItemDeductScore> listHasScore(Integer expertId, Integer gradeId, Integer bidderId);

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
     * @param expertReviewSingleItemDeductScore 查询条件
     * @return 按照条件查询结果列表
     */
    List<ExpertReviewSingleItemDeductScore> listDeductScore(ExpertReviewSingleItemDeductScore expertReviewSingleItemDeductScore);

    /**
     * 通过ID 获取结果并分装Item
     * @param id id
     * @return ExpertReviewSingleItemScore
     */
    ExpertReviewSingleItemDeductScore getDeductScoreById(Integer id);

    /**
     * 通过Id更新
     * @param updateDeductScore 更新数据
     * @return 更新条数
     */
    Integer updateById(ExpertReviewSingleItemDeductScore updateDeductScore);

    /**
     * 通过条件 获取当前专家对于当前评审类型已经打分的评审项
     * @param gradeIds 所有的gradeID
     * @param bidderId 投标人id
     * @param expertId 专家id
     * @param gradeItemId gradeItemId
     * @param reviewType 评审类型
     * @return 获取当前专家对于当前评审类型已经打分的评审项
     */
    List<ExpertReviewSingleItemDeductScore> listHasDeductScoreBySth(String[] gradeIds, Integer bidderId, Integer expertId, Integer gradeItemId, Integer reviewType);

    /**
     * 通过专家ID 以及gradeId 获取已经评审的评审项
     * @param expertId 专家ID
     * @param gradeId gradeId
     * @return 获取已经评审的评审项
     */
    List<ExpertReviewSingleItemDeductScore> listAllHasDeductScore(Integer expertId, Integer gradeId);

    /**
     * 对所有的投标人进行一键不扣分
     * @param gradeIds 所有的gradeID
     * @param reviewType 评审类型
     * @param expertId 专家id
     * @return 对所有的投标人进行一键不扣分
     */
    boolean updateOneKeyNoDeduct(String[] gradeIds, Integer reviewType, Integer expertId);

    /**
     * 通过条件  获取所有的评审结果
     * @param gradeIds 所有的gradeID
     * @param bidderId 投标人ID
     * @param expertId 专家id
     * @param gradeItemId gradeItemId
     * @param reviewType 评审类型
     * @return 获取所有的评审结果
     */
    List<ExpertReviewSingleItemDeductScore> listDeductScoreBySth(String[] gradeIds, Integer bidderId, Integer expertId, Integer gradeItemId, Integer reviewType);

    /**
     * 检查打分是否一致
     * @param gradeIds 所有的gradeID
     * @param reviewType 评审类型
     * @param bidderNum 投标人数量
     * @return 检查打分是否一致
     */
    boolean checkDeductScoreConsistent(String[] gradeIds, Integer reviewType, Integer bidderNum);

    /**
     * 获取平均扣分
     * @param gradeId gradeID
     * @param bidderId 投标人ID
     * @param expertNum 专家人数
     * @return 获取平均扣分
     */
    String getAvgDeductScore(Integer gradeId, Integer bidderId, Integer expertNum);

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
     * 获取当前投标人当前reviewType 的平均分
     * @param gradeIds 所有的gradeids
     * @param reviewType 评审类型
     * @param bidderId 投标人ID
     * @param expertNum 专家人数
     * @return 获取当前投标人当前reviewType 的平均分
     */
    String getAvgDeductScoreForReview(String[] gradeIds, Integer reviewType, Integer bidderId, Integer expertNum);

    /**
     * 通过gradeIDs批量删除
     * @param gradeIds gradeIds
     * @return
     */
    Integer deleteByGradeIds(Integer[] gradeIds);
}
