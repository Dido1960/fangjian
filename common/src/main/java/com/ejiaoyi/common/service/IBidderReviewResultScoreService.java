package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.BidderReviewResultDeduct;
import com.ejiaoyi.common.entity.BidderReviewResultScore;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 企业评审单项的评审结果表 打分制 服务类
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
public interface IBidderReviewResultScoreService extends IService<BidderReviewResultScore> {

    /**
     * 保存或者更新投标人评审结果信息
     * @param newResult 数据
     * @return 插入后的ID
     */
    Integer saveOrUpdateResultScore(BidderReviewResultScore newResult);

    /**
     * 条件获取企业评审标准的评审结果表
     * @param bidderReviewResultScore 条件
     * @return 企业评审标准的评审结果表
     */
    BidderReviewResultScore getBidderReviewResultScore(BidderReviewResultScore bidderReviewResultScore);

    /**
     * 获取投标人总得分
     * @param gradeIds 所有的gradeID
     * @param reviewType 评审类型
     * @param bidderId 投标人ID
     * @return 获取投标人总得分
     */
    String getTotalScore(String[] gradeIds, Integer reviewType, Integer bidderId);

    /**
     * 通过gradeIDs批量删除
     * @param gradeIds gradeIds
     * @return
     */
    Integer deleteByGradeIds(Integer[] gradeIds);
}
