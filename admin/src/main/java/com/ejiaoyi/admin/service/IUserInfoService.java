package com.ejiaoyi.admin.service;

import com.ejiaoyi.common.entity.UserInfo;

import java.util.List;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author fengjunhong
 * @since 2020-03-25
 */
public interface IUserInfoService {

    /**
     * 用户登陆验证 获取人员信息
     *
     * @param loginName 　登录名
     * @return 人员信息
     */
    UserInfo userAuth(String loginName);


    /**
     * 获取用户信息
     *
     * @param regId   区划id
     * @param name    人员名称
     * @param enabled 启用状态
     * @return
     */
    String pagedUser(Integer regId, String name, Integer enabled);

    /**
     * 修改用户信息
     *
     * @param userInfo 用户信息
     * @param roleIds  用户角色列表
     * @return 操作成功：true，操作失败：false
     */
    boolean updateUser(UserInfo userInfo, String roleIds);

    /**
     * 批量删除用户
     *
     * @param ids 用户id数组
     * @return 操作成功：true，操作失败：false
     */
    boolean delUser(Integer[] ids);

    /**
     * 添加用户
     *
     * @param userInfo 用户实体
     * @return
     */
    boolean addUser(UserInfo userInfo);

    /**
     * 批量删除用户
     *
     * @param ids 批量用户id
     * @return
     */
    boolean deleteUser(Integer[] ids);

    /**
     * 获取userInfo对象
     *
     * @param userInfo 用户实体
     * @return userInfo对象
     */
    List<UserInfo> listUserInfo(UserInfo userInfo);

    /**
     * ID查找用户
     *
     * @return
     * @author lesgod
     * @date 2020/5/12 11:38
     */
    UserInfo getUserInfo(Integer userId);

    /**
     * 修改密码
     *
     * @return
     * @author lesgod
     * @date 2020/5/12 11:38
     */
    boolean updatePass(Integer id, String password);

    boolean updateUserData(UserInfo userInfo);

    /**
     * 获取用户绑定信息
     *
     * @return
     * @author lesgod
     * @date 2020-6-22 13:39
     */
    UserInfo userCasn(String casn);

    /**
     * 通过用户名获取用户
     *
     * @return
     * @author 刘国强
     * @date 2020-7-1
     */
    UserInfo getUserByLoginName(String loginName);


    /**
     * 密码重置
     * @param
     */
    void resetUserPassword(UserInfo userInfo);
}
