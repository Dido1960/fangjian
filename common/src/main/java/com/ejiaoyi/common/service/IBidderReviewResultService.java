package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.Bidder;
import com.ejiaoyi.common.entity.BidderReviewResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ejiaoyi.common.entity.Grade;

import java.util.List;

/**
 * <p>
 * 企业评审grade单项的评审结果表 合格制 服务类
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
public interface IBidderReviewResultService extends IService<BidderReviewResult> {

    /**
     * 获取某评审点投标人结果
     * @param bidSectionId  标段id
     * @param gradeId 评分标准主键
     * @param evalProcess 环节
     * @return
     */
    List<Bidder> listBidderGradeResult(Integer bidSectionId, Integer gradeId, Integer evalProcess);

    /**
     * 条件获取企业评审标准的评审结果表
     * @param bidderReviewResult 条件
     * @return 企业评审标准的评审结果表
     */
    BidderReviewResult getBidderReviewResult(BidderReviewResult bidderReviewResult);

    /**
     * 保存或者更新投标人评审结果信息
     * @param bidderReviewResult 投标人评审结果信息
     * @return 企业评审标准的评审结果表id
     */
    Integer saveOrUpdateResult(BidderReviewResult bidderReviewResult);

    /**
     * 获取某投标人的某个环节的评审结果
     * @param bidSectionId  标段id
     * @param bidderId 投标人id
     * @param grades 评审项集合
     * @return
     */
    Boolean listBidderProcessResult(Integer bidSectionId, Integer bidderId, List<Grade> grades);

    /**
     * 通过gradeIDs批量删除
     * @param gradeIds gradeIds
     * @return
     */
    Integer deleteByGradeIds(Integer[] gradeIds);
}
