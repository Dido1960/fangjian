package com.ejiaoyi.common.service;

import com.ejiaoyi.common.dto.BidderResultDTO;
import com.ejiaoyi.common.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 专家对企业评审单项评审结果 扣分制 服务类
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
public interface IExpertReviewSingleItemDeductService extends IService<ExpertReviewSingleItemDeduct> {

    /**
     * 条件获取专家对企业评审单项评审结果集合
     *
     * @param expertReviewSingleItemDeduct 条件
     * @return 专家对企业评审单项评审结果
     */
    List<ExpertReviewSingleItemDeduct> listExpertReviewSingleItemDeduct(ExpertReviewSingleItemDeduct expertReviewSingleItemDeduct);

    /**
     * 初始化专家评审信息(打分项)
     *
     * @param bidSectionId 标段id
     * @param expertId     专家id
     * @param currentGrade 当前评审环节
     * @param bidders      包含的投标人
     * @return
     */
    ExpertReview initReviewItemDeduct(Integer bidSectionId, Integer expertId, Grade currentGrade, List<Bidder> bidders);

    /**
     * 获取已经打了评分的列表
     *
     * @param expertId 专家ID
     * @param gradeId  gradeID
     * @param bidderId 投标人ID
     * @return 获取已经打了评分的列表
     */
    List<ExpertReviewSingleItemDeduct> listHasScore(Integer expertId, Integer gradeId, Integer bidderId);

    /**
     * 通过ID更新扣分项
     *
     * @param expertReviewSingleItemDeduct 更新内容包含ID
     * @return 更新条数
     */
    Integer updateExpertReviewSingleItemDeduct(ExpertReviewSingleItemDeduct expertReviewSingleItemDeduct);

    /**
     * 一键不扣分
     *
     * @param userId  专家ID
     * @param gradeId gradeId
     * @return 更新数量
     */
    Integer updateListDeduct(Integer userId, Integer gradeId);

    /**
     * 通过专家id，gradeId 获取所有已经检查过的结果列表
     *
     * @param expertId 专家id
     * @param gradeId  gradeId
     * @return 获取所有已经检查过的结果列表
     */
    List<ExpertReviewSingleItemDeduct> listAllHasScore(Integer expertId, Integer gradeId);

    /**
     * 检查施工详细评审中 安全质量事故扣分和建筑市场不良记录扣分 意见是否一致
     *
     * @param gradeIds    所有的详细评审gradeID
     * @param methodName1 方法名1
     * @param methodName2 方法名2
     * @return 安全质量事故扣分和建筑市场不良记录扣分 意见是否一致
     */
    Boolean checkDeductUnanimous(Integer[] gradeIds, String methodName1, String methodName2);

    /**
     * 获取结果统计 按照投标人 item 分组 统计每项 扣分与不扣分总和
     *
     * @param gradeId  gradeId
     * @param bidderId bidderId
     * @return 按照投标人 item 分组 统计每项 扣分与不扣分总和
     */
    List<BidderResultDTO> listSumResult(Integer gradeId, Integer bidderId);

    /**
     * 通过ID获取结果 并分钟item
     *
     * @param id id
     * @return 获取结果
     */
    ExpertReviewSingleItemDeduct getDeductById(Integer id);

    /**
     * 通过投标人以及评审表查询评审结果
     *
     * @param expertReviewId 专家评审表id
     * @param bidderId       bidderId
     * @return 通过投标人以及评审表查询评审结果
     */
    List<ExpertReviewSingleItemDeduct> listDeductByBidderIdAndExpertId(Integer expertReviewId, Integer bidderId);

    /**
     * 获取投标人结果数据并对 数据判断是否一致，不一致采取少数服从多数的方式统计结果
     *
     * @param gradeId  gradeId
     * @param bidderId 投标人ID
     * @param expertSize 专家人数
     * @return 投标人结果数据
     */
    List<BidderResultDTO> listResultIsConsistent(Integer gradeId, Integer bidderId, Integer expertSize);

    /**
     * 通过gradeIDs批量删除
     * @param gradeIds gradeIds
     * @return
     */
    Integer deleteByGradeIds(Integer[] gradeIds);
}
