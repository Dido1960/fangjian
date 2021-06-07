package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.QuoteScoreResult;

import java.util.List;

/**
 * <p>
 * 投标人报价得分结果 服务类
 * </p>
 *
 * @author liuguoqiang
 * @since 2020-12-01
 */
public interface IQuoteScoreResultService {
    /**
     * 新增或者更新投标人报价得分结果
     * @param quoteScoreResult 需要操作的数据
     * @return 操作后对象的id
     */
    Integer addOrUpdateQuoteScore(QuoteScoreResult quoteScoreResult);

    /**
     * 根据条件获取投标人报价得分结果
     * @param quoteScoreResult 条件
     * @return 结果对象
     */
    List<QuoteScoreResult> listQuoteScoreResult(QuoteScoreResult quoteScoreResult);

    /**
     * 获取所有投标人报价得分结果
     * @param bidSectionId 标段id
     * @return 结果对象
     */
    List<QuoteScoreResult> listQuoteScoreResult(Integer bidSectionId);

    /**
     * 根据投标人id获取投标人报价得分结果
     * @param bidderId 条件
     * @return 结果对象
     */
    QuoteScoreResult getQuoteScoreResultByBidderId(Integer bidderId);

    /**
     * 通过标段ID批量删除
     * @param bidSectionId 标段ID
     * @return
     */
    Integer deleteByBsId(Integer bidSectionId);
}
