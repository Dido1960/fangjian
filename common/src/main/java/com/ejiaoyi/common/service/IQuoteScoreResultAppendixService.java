package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.QuoteScoreResultAppendix;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 投标人报价得分结果附录 服务类
 * </p>
 *
 * @author Make
 * @since 2020-12-05
 */
public interface IQuoteScoreResultAppendixService extends IService<QuoteScoreResultAppendix> {

    /**
     * 通过标段id获取所有投标人报价得分结果附录
     * @param bidSectionId 标段id
     * @return
     */
    List<QuoteScoreResultAppendix> listQuoteScoreResultAppendix(Integer bidSectionId);

    /**
     * 通过投标人id获取该投标人报价得分结果附录
     * @param bidderId 投标人id
     * @return
     */
    QuoteScoreResultAppendix getQuoteScoreResultAppendix(Integer bidderId);

    /**
     * 根据标段id删除所有投标人报价得分结果附录
     * @param bidSectionId 标段id
     * @return
     */
    Integer deleteByBsId(Integer bidSectionId);
}
