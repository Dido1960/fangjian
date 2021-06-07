package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.LineStatus;

/**
 * 网上开标状态信息服务类
 *
 * @author Make
 * @since 2020-07-22
 */
public interface ILineStatusService {
    /**
     * 通过主键id获取网上开标状态信息
     *
     * @param id 网上开标状态主键id
     * @return 网上开标状态信息
     */
    LineStatus getLineStatusById(Integer id);


    /**
     * 通过标段主键id获取网上开标状态信息
     *
     * @param bidSectionId 标段主键id
     * @return 网上开标状态信息
     */
    LineStatus getLineStatusBySectionId(Integer bidSectionId);

    /**
     * 通过id更新网上开标状态信息
     *
     * @param lineStatus 网上开标状态信息(id和需要修改的值)
     * @return
     */
    boolean updateLineStatus(LineStatus lineStatus);

    /**
     * 修改网上开标状态
     * @param lineStatus
     * @return
     */
    Integer updateLineStatusById(LineStatus lineStatus);

    /**
     * 修改文件上传和签到状态
     * @param bidSectionId
     */
    void updateFileUploadOrsigninStatus(Integer bidSectionId);

    /**
     * 初始开标状态
     * @param bidSectionId
     */
    void insertLineStatusByBidSectionId(Integer bidSectionId);
}
