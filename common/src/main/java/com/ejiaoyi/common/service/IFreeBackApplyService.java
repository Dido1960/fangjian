package com.ejiaoyi.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ejiaoyi.common.entity.FreeBackApply;

import java.util.List;

import java.util.List;

/**
 * <p>
 * 自由回退申请 服务类
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
public interface IFreeBackApplyService extends IService<FreeBackApply> {

    /**
     * 获取当前待审核的回退申请
     * @param id 回退表主键
     * @return
     */
    FreeBackApply getFreeBackApplyById(Integer id);

    /**
     * 获取当前待审核的回退申请
     * @param bidSectionId 标段主键
     * @return
     */
    FreeBackApply getFreeBackApplyByBidSectionId(Integer bidSectionId);

    /**
     * 回退历史记录（审核状态不为0）
     * @param bidSectionId 标段主键
     * @return
     */
    List<FreeBackApply> listFreeBackApply(Integer bidSectionId);

    /**
     * 用来美化对象，给自定义属性赋值
     * @param freeBackApplies 自由回退申请
     * @return
     */
    FreeBackApply getFreeBackAppliesBeautify(FreeBackApply freeBackApplies);

    /**
     * 用来美化对象，给自定义属性赋值
     * @param freeBackApplies 自由回退申请
     * @return
     */
    List<FreeBackApply> listFreeBackApplyBeautify(List<FreeBackApply> freeBackApplies);

    /**
     * 添加申请
     * @param freeBackApply 申请数据
     * @return
     */
    Integer addFreeBackApply(FreeBackApply freeBackApply);

    /**
     * 通过标段id获取正在申请的数据
     * @param bidSectionId 标段ID
     * @return
     */
    List<FreeBackApply> getApplyingByBsId(Integer bidSectionId);

    /**
     * 通过标段id获取数据
     * @param bidSectionId 标段ID
     * @return
     */
    List<FreeBackApply> getApplyedByBsId(Integer bidSectionId);

    /**
     * 通过ID获取
     * @param id
     * @return
     */
    FreeBackApply getApplyById(Integer id);

    /**
     * 获取所有的回退历史记录
     * @param bidSectionId 标段主键
     * @return
     */
    List<FreeBackApply> listAllFreeBackApply(Integer bidSectionId);
}
