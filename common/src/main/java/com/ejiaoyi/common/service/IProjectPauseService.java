package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.ProjectPause;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 项目暂停记录表 服务类
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
public interface IProjectPauseService extends IService<ProjectPause> {

    /**
     * 通过标段id获取一条数据
     *
     * @param bidSectionId 标段id
     * @return 记录
     */
    ProjectPause getProjectPauseByBidSectionId(Integer bidSectionId);
}
