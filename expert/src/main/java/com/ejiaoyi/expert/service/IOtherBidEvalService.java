package com.ejiaoyi.expert.service;

import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.exception.CustomException;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 勘察设计电梯评标 服务类
 * </p>
 *
 * @author yyb
 * @since 2020-11-05
 */
public interface IOtherBidEvalService {

    /**
     * 验证评标委员会评审结束的相关条件
     *
     * @param bidSectionId 标段id
     * @param evalProcess  评审环节
     * @return
     */
    Map<String, Object> validGroupReview(Integer bidSectionId, Integer evalProcess);

    /**
     * 获取前三名的中标候选人信息
     *
     * @param bidSectionId 标段id
     * @param ranking      排名
     * @param cs 上一名候选人
     * @return
     */
    CandidateSuccess getSuccessCandidate(Integer bidSectionId, Integer ranking,CandidateSuccess cs);

    /**
     * 获取详细评审的所有投标人1、2、3名得票数
     *
     * @param bidSectionId 标段id
     * @return 投标人票数信息
     */
    List<Bidder> getBidderVoteNum(Integer bidSectionId);

    /**
     * 校验候选人是否存在，重复等
     *
     * @param expertId 专家id
     * @param bidSectionId 标段id
     * @return 投标人票数信息
     */
    Boolean validRecommend(Integer expertId, Integer bidSectionId);

    /**
     * 校验中标候选人信息
     *
     * @param bidSectionId 标段id
     * @return 错误信息
     */
    String validLeaderRecommend(Integer bidSectionId);

}
