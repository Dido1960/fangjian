package com.ejiaoyi.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ejiaoyi.common.entity.Project;
import com.ejiaoyi.common.mapper.ProjectMapper;
import com.ejiaoyi.common.service.IProjectService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 项目信息 服务实现类
 * </p>
 *
 * @author Mike
 * @since 2020-12-28
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {
    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public Integer countProjectByCode(String projectCode, Integer regId) {
        QueryWrapper<Project> projectQuery = new QueryWrapper<>();
        projectQuery.eq(StringUtils.isNotEmpty(projectCode), "PROJECT_CODE", projectCode)
                .eq(regId != null, "REG_ID", regId)
                .eq("DELETE_FLAG", "0");
        return projectMapper.selectCount(projectQuery);
    }
    @Override
    public Project getProjectByCode(String projectCode, Integer regId){
        QueryWrapper<Project> projectQuery = new QueryWrapper<>();
        projectQuery.eq(StringUtils.isNotEmpty(projectCode), "PROJECT_CODE", projectCode)
                .eq(regId != null, "REG_ID", regId)
                .eq("DELETE_FLAG", "0");
        return projectMapper.selectOne(projectQuery);
    }
}
