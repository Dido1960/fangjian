package com.ejiaoyi.admin.service;

import com.ejiaoyi.common.dto.ProjectInfoTemp;

/**
 * <p>
 * 项目信息 服务类
 * </p>
 *
 * @author Mike
 * @since 2021-04-13
 */
public interface IProjectInfoService {

    /**
     * 修改项目信息
     * @param projectInfoTemp
     * @return
     */
    Boolean updateProjectInfo(ProjectInfoTemp projectInfoTemp);
}
