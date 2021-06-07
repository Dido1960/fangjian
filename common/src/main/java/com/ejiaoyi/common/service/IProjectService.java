package com.ejiaoyi.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ejiaoyi.common.entity.Project;

/**
 * <p>
 * 项目信息 服务类
 * </p>
 *
 * @author Mike
 * @since 2020-12-28
 */
public interface IProjectService extends IService<Project> {

    /**
     * 通过项目编号和区划id获取项目数量
     * @param projectCode 项目编号
     * @param regId 区划id
     * @return 项目数
     */
    Integer countProjectByCode(String projectCode, Integer regId);

    /**
     * 通过项目编号和区划id获取项目信息
     * @param projectCode 项目编号
     * @param regId 区划id
     * @return 项目信息
     */
    Project getProjectByCode(String projectCode, Integer regId);

}
