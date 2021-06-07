package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.BidderReviewResultDeduct;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ejiaoyi.common.entity.BidderReviewResultScore;

/**
 * <p>
 * 企业评审grade单项的评审结果表 扣分制 服务类
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
public interface IBidderReviewResultDeductService extends IService<BidderReviewResultDeduct> {

    /**
     * 保存或者更新投标人评审结果信息
     * @param newResult 数据
     * @return 插入后的ID
     */
    Integer saveOrUpdateResultDeduct(BidderReviewResultDeduct newResult);

    /**
     * 条件获取企业评审标准的评审结果表
     * @param bidderReviewResultDeduct 条件
     * @return 企业评审标准的评审结果表
     */
    BidderReviewResultDeduct getBidderReviewResultDeduct(BidderReviewResultDeduct bidderReviewResultDeduct);

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
