package com.ejiaoyi.common.service;

import com.ejiaoyi.common.dto.statistical.BidTypeDetailDTO;
import com.ejiaoyi.common.dto.statistical.RegDetailDTO;
import com.ejiaoyi.common.entity.BidSection;

import java.util.List;
import java.util.Map;

/**
 * 标段信息服务类
 *
 * @author Make
 * @since 2020-07-13
 */
public interface IBidSectionService {

    /**
     * 通过主键id获取标段信息
     *
     * @param id 标段主键id
     * @return 标段信息
     */
    BidSection getBidSectionById(Integer id);

    /**
     * 分页获取标段列表
     * @param bidSection 标段信息
     * @return
     */
    List<BidSection> listBidSection(BidSection bidSection);

    /**
     * 获取标段列表 不分页 (不见面开标大厅)
     *
     * @param bidSection 标段信息
     * @return
     */
    List<BidSection> listBidSectionNoPage(BidSection bidSection);

    /**
     * 获取三个最近的开标项目
     *
     * @return
     */
    List<BidSection> listThreeBidSection();

    /**
     * 获取标段列表
     * @param bidSection 标段信息
     * @return
     */
    Map<String,Object> mapBidSection(BidSection bidSection);

    /**
     * 通过id更新标段信息
     *
     * @param bidSection 标段信息(id和需要修改的值)
     * @return
     */
    int updateBidSectionById(BidSection bidSection);

    /**
     * 获取投标人参标标段列表
     * @param bidSection 标段信息
     * @param currentUserOrgCode 当前投标人组织机构代码
     * @return
     */
    String listJoinBidSection(BidSection bidSection, String currentUserOrgCode);

    /**
     * 获取 项目标段数目
     * @return
     */
    Integer getProjectTotal(BidSection bidSection, String currentUserOrgCode);


    /**
     * 获取标段信息列表
     *
     * @param bidSection 标段信息
     * @return
     */
    String getBidSection(BidSection bidSection);

    /**
     * 同意评标
     *
     * @param bidSectionId 标段id
     * @return
     */
    Boolean agreeEval(Integer bidSectionId);

    /**
     * 根据条件查询标段数量
     *
     * @param bidSection 条件信息
     * @return 标段信息
     */
    Integer countBidSection(BidSection bidSection);

    /**
     * 保存标段信息
     * @param bidSection
     * @return
     */
    Integer save(BidSection bidSection);

    /**
     * 通过区划id、标段分类代码和标段编号获取标段信息
     * @param bidSectionCode 标段编号
     * @param bidClassifyCode 标段分类代码
     * @param regId 区划id
     * @return 标段信息
     */
    BidSection getBidSectionByCode(String bidSectionCode, String bidClassifyCode, Integer regId);

    /**
     * 多条件查询列表
     * @param bidSection
     * @return
     */
    List<BidSection> listBidSectionBySth(BidSection bidSection);

    /**
     * 分页查询标段信息
     * @param bidSection 查询条件封装
     * @return
     */
    String pagedBidSectionInfo(BidSection bidSection);

    /**
     * 根据条件统计标段信息
     *
     * @param bidSection 条件信息
     * @return 统计标段信息
     */
    List<BidSection> listStatisticalBidSection(BidSection bidSection);

    /**
     * 按照标段类型分类统计标段数量
     *
     * @param bidSection 条件信息
     * @return 统计标段信息
     */
    List<BidTypeDetailDTO> statisticalBidSectionByBidType(BidSection bidSection);

    /**
     * 按照区划分类统计标段数量
     *
     * @param bidSection 条件信息
     * @return 统计标段信息
     */
    List<RegDetailDTO> statisticalBidSectionByReg(BidSection bidSection);
}
