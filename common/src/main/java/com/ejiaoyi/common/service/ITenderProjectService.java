package com.ejiaoyi.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ejiaoyi.common.entity.TenderProject;

/**
 * 招标项目信息服务类
 *
 * @author Make
 * @since 2020-07-13
 */
public interface ITenderProjectService extends IService<TenderProject> {

    /**
     * 通过主键id获取招标项目信息
     *
     * @param id 招标项目主键id
     * @return 标段信息
     */
    TenderProject getTenderProjectById(Integer id);

    /**
     * 通过招标项目编号和区划id获取招标项目数量
     * @param tenderProjectCode 招标项目编号
     * @param regId 区划id
     * @return 招标项目数
     */
    Integer countTenderProjectByCode(String tenderProjectCode, Integer regId);

    /**
     * 通过项目编号和区划id获取招标项目信息
     * @param tenderProjectCode 招标项目编号
     * @param regId 区划id
     * @return 招标项目信息
     */
    TenderProject getTenderProjectByCode(String tenderProjectCode, Integer regId);
}
