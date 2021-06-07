package com.ejiaoyi.expert.service;

import com.ejiaoyi.common.dto.BidderResultDTO;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.entity.Bidder;
import com.ejiaoyi.expert.dto.ClearBidProcessDTO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 施工评标 服务类
 * </p>
 *
 * @author Make
 * @since 2020-12-01
 */
public interface IConBidEvalService {
//    /**
//     * 计算报价得分
//     * @param bidSectionId 标段信息
//     * @return
//     */
//    void calcPriceScore(Integer bidSectionId);

    /**
     * 校验报价得分计算服务完成情况
     * @param bidSectionId 标段id
     * @return
     */
    JsonData validPriceScore(Integer bidSectionId);

    /**
     * 通过标段id获取清标进度数据
     * @param bidSectionId 标段id
     * @return
     */
    ClearBidProcessDTO initClearBidProcessData(Integer bidSectionId);

    /**
     * 互保共建 评标办法 初始化
     * @return 互保共建的 gradeID
     */
    Integer mutualGradeInit();

    /**
     * 其他评审  互保共建 判断专家对投标人 评审 是否完成
     * @param bidders 投标人
     * @param gradeId gradeId
     *
     * @return 封装后的投标人
     */
    List<Bidder> otherMutualGradeIsEnd(List<Bidder> bidders, Integer gradeId, Integer expertId);

    /**
     * 判断专家 是否满足个人结束条件
     * @param gradeId gradeId
     * @return 是否满足个人结束条件
     */
    JsonData checkPersonalOtherMutualEnd(Integer gradeId);

    /**
     * 互保共建 个人评审结束
     * @return 个人评审结束
     */
    Boolean personalOtherMutualEnd();

    /**
     * 互保共建 环节重评
     */
    void restartOtherMutualReview();

    /**
     * 互保共建 小组结束检查
     *
     * @return 小组结束检查
     */
    JsonData validOtherMutualGroupEnd();

    /**
     * 互保共建 获取专家的结果数据
     *
     * @param bidderId       投标人ID
     * @param gradeId        gradeId
     * @param isAllExpertEnd 是否所有专家都个人评审结束
     */
    Map<String, Object> getBidderMutualResultData(Integer bidderId, Integer gradeId, Integer isAllExpertEnd);

    /**
     * 互保共建 获取所有投标人的结果数据
     * @param gradeId gradeId
     * @return 获取所有投标人的结果数据
     */
    List<BidderResultDTO> getGroupMutualResultData(Integer gradeId);

    /**
     * 互保共建 小组结束
     *
     * @return 小组结束
     */
    Boolean endGroupMutualResult(Integer gradeId);

}
