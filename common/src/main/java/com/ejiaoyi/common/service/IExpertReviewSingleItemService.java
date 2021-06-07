package com.ejiaoyi.common.service;

import com.ejiaoyi.common.dto.BidderResultDTO;
import com.ejiaoyi.common.entity.Bidder;
import com.ejiaoyi.common.entity.ExpertReview;
import com.ejiaoyi.common.entity.ExpertReviewSingleItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ejiaoyi.common.entity.Grade;

import java.util.List;

/**
 * <p>
 * 专家对企业评审单项评审结果 合格制 服务类
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
public interface IExpertReviewSingleItemService extends IService<ExpertReviewSingleItem> {
    /**
     * 初始化专家评审信息
     * @param bidSectionId  标段id
     * @param expertId  专家id
     * @param currentGrade 当前评审环节
     * @param bidders 包含的投标人
     */
    ExpertReview initExpertSingleItem(Integer bidSectionId, Integer expertId, Grade currentGrade, List<Bidder> bidders);


    /**
     * 条件获取专家对企业评审单项评审结果集合
     * @param expertReviewSingleItem 条件
     * @return 专家对企业评审单项评审结果
     */
    List<ExpertReviewSingleItem> listExpertReviewSingleItem(ExpertReviewSingleItem expertReviewSingleItem);

    /**
     * 获取指定评审项目结果集合
     * @param gradeIds 评审项id集合
     * @return
     */
    List<ExpertReviewSingleItem> listExpertReviewSingleItem(String[] gradeIds);

    /**
     * 更新专家对企业评审单项评审结果
     * @param expertReviewSingleItem 条件
     * @return 专家对企业评审单项评审结果
     */
    Integer updateSingleItem(ExpertReviewSingleItem expertReviewSingleItem);

    /**
     * 获取当前专家对grade中已经打分了的评审项列表
     * @param expertId 专家ID
     * @param gradeId gradeID
     * @param bidderId 投标人ID
     * @return
     */
    List<ExpertReviewSingleItem> listHasScore(Integer expertId, Integer gradeId, Integer bidderId);

    /**
     * 通过 评审id和专家id更新专家评审表
     * @param gradeId 评审id
     * @param userId 专家id
     * return  更新条数
     */
    Integer updateListItem(Integer gradeId, Integer userId);


    /**
     * 条件获取专家对企业评审单项评审结果
     * @param expertReviewSingleItem 条件
     * @return 专家对企业评审单项评审结果
     */
    ExpertReviewSingleItem getExpertReviewSingleItem(ExpertReviewSingleItem expertReviewSingleItem);

    /**
     * 当前grandID以经打分的结果汇总 用于判断当前结果是否一致
     * @param gradeId id
     * @return
     */
    List<BidderResultDTO> listGradeConsistent(Integer gradeId);

    /**
     * 通过投标人id和专家评审id获取单项打分列表
     * @param expertReviewId
     * @param bidderId
     * @return
     */
    List<ExpertReviewSingleItem> listItemByBidderIdAndExpertId(Integer expertReviewId, Integer bidderId);


    /**
     * 当前graed已经打分的列表 用于个人结束检查
     * @param expertId 专家ID
     * @param gradeId gradeID
     * @return
     */
    List<ExpertReviewSingleItem> listAllHasScore(Integer expertId, Integer gradeId);

    /**
     * 更新专家对企业评审单项评审结果
     * @param expertReviewSingleItem 条件
     * @return 专家对企业评审单项评审结果
     */
    Integer update(ExpertReviewSingleItem expertReviewSingleItem);

    /**
     * 检测所有专家的各详细评审项的 结果是否一致
     *
     * @param gradeIds 评审标准ids
     * @return 是否一致
     */
    boolean listCountByEvalResult(Integer[] gradeIds);

    /**
     * 获取投标人评审项的结果（合格制）
     *
     * @param gradeId 评审标准id
     * @param bidSectionId 标段id
     * @return
     */
    List<BidderResultDTO> listQualifiedInfo(Integer gradeId, Integer bidSectionId);

    /**
     * 通过id获取 分装item
     * @param singId id
     * @return ExpertReviewSingleItem
     */
    ExpertReviewSingleItem getSingleById(Integer singId);

    /**
     * 判断投标人当前评审项结果
     * @param gradeId gradeId
     * @param bidderId 投标人Id
     * @return 判断投标人当前评审项结果
     */
    List<BidderResultDTO> listGradeResult(Integer gradeId, Integer bidderId);

    /**
     * 汇总单项item得分结果
     * @param gradeId gradeId
     * @param bidderId 投标人ID
     * @return 汇总单项item得分结果
     */
    List<BidderResultDTO> listItemResult(Integer gradeId, Integer bidderId);

    /**
     * 通过gradeIDs批量删除
     * @param gradeIds gradeIds
     * @return
     */
    Integer deleteByGradeIds(Integer[] gradeIds);

    /**
     * 将评审意见，按照评审步骤排序
     * @param bidSectionId 标段主键
     * @param expertReviewSingleItems 所有专家评审意见
     * @return
     */
    List<ExpertReviewSingleItem> sortExpertReviewSingleByStep(Integer bidSectionId,List<ExpertReviewSingleItem> expertReviewSingleItems);

    /**
     * 废标投标人
     * @param bidSectionId 标段主键
     * @param expertReviewSingleItems 所有专家评审意见
     * @return
     */
    List<ExpertReviewSingleItem> scrapBiddersResult(Integer bidSectionId,List<ExpertReviewSingleItem> expertReviewSingleItems);
}
