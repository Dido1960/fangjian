package com.ejiaoyi.worker.service;

import com.ejiaoyi.common.entity.*;

import java.util.List;

/**
 * @Description:
 * @Auther: liuguoqiang
 * @Date: 2020-8-24 13:45
 */
public interface IExpertInputService {
    /**
     * 分页查询标段列表
     * @param bidSection
     * @return
     */
    String listBidSection(BidSection bidSection);

    /**
     * 获取当前筛选的总条数
     * @param bidSection
     * @return
     */
    Integer getProjectTotal(BidSection bidSection);

    /**
     * 初始化评标申请记录
     * @param bidSectionId
     */
    BidApply bidApplyInfo(Integer bidSectionId);

    /**
     * 查询当前标段信息，专家人数
     * @param bidSectionId
     * @return
     */
    BidSection getBidSection(Integer bidSectionId);

    /**
     * 专家列表
     * @param bidSectionId
     * @return
     */
    List<ExpertUser> listExpertByBidSectionId(Integer bidSectionId);

    /**
     * 专家分类列表
     * @return
     */
    List<Wordbook> getExpertCategoryList();

    /**
     * 添加expert
     * @param expertUser
     * @return
     */
    String addExpert(ExpertUser expertUser);

    List<ExpertUser> getRepresentativeList(Integer bidSectionId);

    /**
     * 禁用专家
     * @param id
     * @return
     */
    Boolean deleteExpert(Integer id);

    /**
     * 通过标段获取项目信息
     * @param bidSection
     * @return
     */
    TenderProject getTenderProject(BidSection bidSection);

    /**
     * 获取当前项目下的标段列表  用于专家复用
     * 筛选条件 项目下标段 当前用户的区划与标段主客场一致 排除当前需要复用的标段
     * @param bidSection
     * @return
     */
    List<BidSection> getBidSectionListForExpert(BidSection bidSection);

    /**
     * 专家复用
     * @param bidSectionId
     * @param ids
     * @param representativeCount
     * @return
     */
    Boolean addExpertList(Integer bidSectionId, Integer[] ids, Integer representativeCount);

    /**
     * 人名模糊查询
     * @param expertName
     * @return
     */
    List<ExpertUser> searchExpert(String expertName);

    /**
     * 查询当前标段的是否有当前的身份证号
     * @param idCard
     * @param bidSectionId
     * @return
     */
    Boolean isIdCardRepeat(String idCard, Integer bidSectionId);

    /**
     * 查询当前标段的是否有当前的Phone
     * @param phone 电话号
     * @param bidSectionId 标段Id
     * @return 是否重复
     */
    Boolean isPhoneRepeat(String phone, Integer bidSectionId);
}
