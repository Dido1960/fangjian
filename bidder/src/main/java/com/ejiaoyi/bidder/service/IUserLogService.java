package com.ejiaoyi.bidder.service;

import com.ejiaoyi.common.entity.UserLog;
import com.ejiaoyi.common.enums.DMLType;

/**
 * <p>
 * 用户日志 服务类
 * </p>
 *
 * @author fengjunhong
 * @date 2020/4/16
 */
public interface IUserLogService {

    /**
     * 添加用户日志
     *
     * @param userLog 用户日志
     */
    void addLog(UserLog userLog);

    /**
     * 自定义插入用户日志
     *
     * @param dmlType 数据库操作类型
     * @param content 操作内容
     */
    void addUserLog(DMLType dmlType, String content);

    /**
     * 获取用户日志列表
     *
     * @param username    用户名
     * @param dmlType     数据库操纵类型
     * @param content     日志内容
     * @param searchTime  搜索时间
     * @param dmlTypeName 数据库操作类型名称
     * @return 用户日志列表
     */
    String pagedUser(String username, String dmlType, String content, String searchTime, String dmlTypeName);
}
