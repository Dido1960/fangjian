package com.ejiaoyi.supervise.service;

import com.ejiaoyi.common.dto.BidOpenFlowSituationDTO;
import com.ejiaoyi.common.entity.BidSection;
import com.ejiaoyi.common.entity.Bidder;

import java.util.List;

/**
 * 主管部门服务层
 *
 * @author Make
 * @date 2020/10/8 15:24
 */
public interface IGovService {

    /**
     * 分页查询标段列表
     *
     * @param bidSection
     * @return
     */
    String listBidSection(BidSection bidSection);

    /**
     * 获取当前筛选的总条数
     *
     * @param bidSection
     * @return
     */
    Integer getProjectTotal(BidSection bidSection);

    /**
     * 获取投标人并分析开评标否决情况
     *
     * @param bidSectionId 标段id
     * @return
     */
    List<Bidder> listBiddersWithVeto(Integer bidSectionId);

    /**
     * 获取开标流程完成情况
     *
     * @param bidSectionId 标段id
     * @return
     */
    List<BidOpenFlowSituationDTO> listBidOpenFlowSituation(Integer bidSectionId);

    /**
     * 获取当前评标环节
     * @param bidSectionId 标段id
     * @return
     */
    String getNowEvalFlow(Integer bidSectionId);
}
