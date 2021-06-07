package com.ejiaoyi.admin.service;


import com.ejiaoyi.common.dto.JsonData;

/**
 * <p>
 * 老系统项目 服务类
 * </p>
 *
 * @author Mike
 * @since 2021-04-13
 */
public interface IOldProjectService {
    /**
     * 新增老系统项目
     * @param fileArchive 规定当文件路径
     * @param regId 区划id
     * @return
     */
    JsonData addOldProject(String fileArchive, Integer regId);
}
