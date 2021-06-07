package com.ejiaoyi.admin.service;

import com.ejiaoyi.admin.entity.ReviewAuthorityLog;

/**
 * <p>
 * 离线审核权限写入日志 服务类
 * </p>
 *
 * @author Mike
 * @since 2021-01-13
 */
public interface IReviewAuthorityLogService {

    /**
     * 记录离线审核权限写入日志
     *
     * @param reviewAuthorityLog 离线审核权限写入日志
     * @return
     */
    boolean saveReviewAuthorityLog(ReviewAuthorityLog reviewAuthorityLog);

    String pagedReviewAuthorityLog(String username, Integer dmlType, String content, String searchTime);
}
