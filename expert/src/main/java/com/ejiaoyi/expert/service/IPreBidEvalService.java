package com.ejiaoyi.expert.service;

import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.entity.Bidder;
import com.ejiaoyi.common.entity.ExpertReviewSingleItem;

import java.util.List;

/**
 * <p>
 * 资格预审评标 服务类
 * </p>
 *
 * @author yyb
 * @since 2020-11-05
 */
public interface IPreBidEvalService {


    /**
     * 校验详细评审是否符合结束标志
     *
     * @return
     */
    JsonData validPersonReviewEnd();

    /**
     * 获取不合格项的专家评审情况
     * @return
     */
    List<ExpertReviewSingleItem> listUnqualifiedItem();

    /**
     * 获取资格预审参与评标的投标人
     * 包括初步评审和详细评审评审结果
     *
     * @param bidSectionId 标段id
     * @return
     */
    List<Bidder> listPrePassSortBidder(Integer bidSectionId);


}
