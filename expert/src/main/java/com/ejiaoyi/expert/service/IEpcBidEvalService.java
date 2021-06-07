package com.ejiaoyi.expert.service;

import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.entity.Bidder;
import com.ejiaoyi.common.entity.ExpertReview;
import com.ejiaoyi.common.entity.Grade;
import com.ejiaoyi.common.entity.QuoteScoreResultAppendix;
import com.ejiaoyi.common.exception.CustomException;

import java.util.List;

/**
 * <p>
 * 施工总承包评标 服务类
 * </p>
 *
 * @author Make
 * @since 2020-12-01
 */
public interface IEpcBidEvalService {
    /**
     * 计算报价得分
     * @param bidSectionId 标段信息
     * @return
     */
    boolean addQuotationScore(Integer bidSectionId) throws CustomException;

    /**
     * 初始化专家评审信息(报价得分计算)
     * @param bidSectionId 标段id
     * @param expertId 专家id
     * @param grades 当前的所以评审环节
     * @param bidders 包含的投标人
     * @return
     */
    ExpertReview initExpertPriceReview(Integer bidSectionId, Integer expertId, List<Grade> grades, List<Bidder> bidders);

    /**
     * 价格分计算小组评审结束
     * @param bidSectionId 标段名称
     * @return
     */
    Boolean epcPriceGroupEnd(Integer bidSectionId);

    /**
     * 初始化价格分修改结果附表
     * @param bidSectionId 需要初始化的标段id
     * @return 初始化结果集
     */
    List<QuoteScoreResultAppendix> initQuoteScoreResultAppendix(Integer bidSectionId);

    /**
     * 价格分计算 小组评审结束检查
     *
     * @return 小组评审结束检查
     */
    JsonData checkEpcPriceGroupEnd();

}
