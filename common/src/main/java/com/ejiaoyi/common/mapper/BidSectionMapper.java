package com.ejiaoyi.common.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.common.dto.statistical.BidNewsDTO;
import com.ejiaoyi.common.dto.statistical.BidTypeDetailDTO;
import com.ejiaoyi.common.dto.statistical.RegDetailDTO;
import com.ejiaoyi.common.entity.BidSection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 标段信息 Mapper 接口
 *
 * @author Z0001
 * @since 2020-07-03
 */
@Component
public interface BidSectionMapper extends BaseMapper<BidSection> {

    /**
     * 根据条件查询标段数量
     *
     * @param bidSection 条件信息
     * @return 标段信息
     */
    Integer countBidSection(@Param("bidSection") BidSection bidSection);

    /**
     * 获取标段列表
     *
     * @param bidSection 标段信息
     * @param page       分页参数
     * @param ids        标段id集合
     * @return
     */
    List<BidSection> listBidSection(@Param("page") Page page, @Param("bidSection") BidSection bidSection, @Param("bidSectionIds") List<Integer> ids);

    /**
     * 获取标段列表 不分页
     *
     * @param bidSection 标段信息
     * @param ids        标段id集合
     * @return
     */
    List<BidSection> listBidSection(@Param("bidSection") BidSection bidSection,@Param("bidSectionIds") List<Integer> ids);

    /**
     * 获取当前项目下的标段列表  用于专家复用
     * 筛选条件 项目下标段 当前用户的区划与标段主客场一致 排除当前需要复用的标段
     * @param bidSection 标段信息
     * @return
     */
    List<BidSection> getBidSectionListForExpert(@Param("bidSection") BidSection bidSection);

    /**
     * 获取今日开标项目
     * @param bidSection
     * @return
     */
    List<BidSection> listBidSectionByToDay(@Param("bidSection") BidSection bidSection);

    /**
     * 获取三个最近的开标项目
     * @return
     */
    List<BidSection> listThreeBidSection();

    List<BidSection> pagedBidSectionInfo(@Param("page") Page page, @Param("bidSection") BidSection bidSection);

    /**
     * 根据条件统计标段信息
     *
     * @param bidSection 条件信息
     * @return 统计标段信息
     */
    List<BidSection> listStatisticalBidSection(@Param("bidSection") BidSection bidSection);

    /**
     * 按照区划分类统计标段数量
     *
     * @param bidSection 条件信息
     * @return 统计标段信息
     */
    List<RegDetailDTO> statisticalBidSectionByReg(@Param("bidSection") BidSection bidSection);

    /**
     * 按照标段类型分类统计标段数量
     *
     * @param bidSection 条件信息
     * @return 统计标段信息
     */
    List<BidTypeDetailDTO> statisticalBidSectionByBidType(@Param("bidSection") BidSection bidSection);

    /**
     * 分页查询标讯信息
     * @param page
     * @param bidSection
     * @return
     */
    List<BidNewsDTO> pageBidNewsByDate(@Param("page") Page<BidNewsDTO> page, @Param("bidSection") BidSection bidSection);
}
