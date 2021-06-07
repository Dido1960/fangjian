package com.ejiaoyi.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ejiaoyi.common.entity.ProjectPause;
import com.ejiaoyi.common.mapper.ProjectPauseMapper;
import com.ejiaoyi.common.service.IProjectPauseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 项目暂停记录表 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
@Service
public class ProjectPauseServiceImpl extends ServiceImpl<ProjectPauseMapper, ProjectPause> implements IProjectPauseService {

    @Autowired
    private ProjectPauseMapper projectPauseMapper;

    @Override
    public ProjectPause getProjectPauseByBidSectionId(Integer bidSectionId) {
        QueryWrapper<ProjectPause> wrapper = new QueryWrapper<>();
        wrapper.eq("BID_SECTION_ID", bidSectionId);
        return projectPauseMapper.selectOne(wrapper);
    }
}
