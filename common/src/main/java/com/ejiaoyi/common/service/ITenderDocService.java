package com.ejiaoyi.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ejiaoyi.common.entity.TenderDoc;

/**
 * 招标文件信息服务类
 *
 * @author Make
 * @since 2020-07-13
 */
public interface ITenderDocService extends IService<TenderDoc> {

    /**
     * 通过主键id获取招标文件信息
     *
     * @param id 标段主键id
     * @return 招标文件信息
     */
    TenderDoc getTenderDocById(Integer id);

    /**
     * 通过标段id获取招标文件信息
     *
     * @param bidSectionId 标段id
     * @return 招标文件信息
     */
    TenderDoc getTenderDocBySectionId(Integer bidSectionId);

    /**
     * 修改招标文件信息
     * @param tenderDoc 条件
     * @return
     */
    boolean updateTenderDoc(TenderDoc tenderDoc);

    /**
     * 修改招标文件信息
     * @param tenderDoc 条件
     * @return
     */
    boolean updateTenderDocById(TenderDoc tenderDoc);
}
