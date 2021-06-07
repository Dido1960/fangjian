package com.ejiaoyi.admin.service;

import com.ejiaoyi.common.entity.RoleUser;

import java.util.List;

/**
 * <p>
 * 角色用户关系表 服务类
 * </p>
 *
 * @author fengjunhong
 * @since 2020-03-25
 */
public interface IRoleUserService {

    /**
     * 批量添加用户角色
     *
     * @param roleUsers 用户角色列表
     * @return 操作成功，返回true 操作失败，返回false
     */
    boolean addRoleUser(List<RoleUser> roleUsers);

    /**
     * 删除用户对应的权限
     *
     * @param userId 用户id
     * @return
     */
    boolean delRoleUser(Integer[] userId);

    /**
     * 根据用户id,获取用户角色名称
     *
     * @param uId 用户id
     * @return 用户列表
     */
    List<RoleUser> listRoleUser(Integer uId);

    /**
     * 通过用户id获取用户角色id字符串
     *
     * @return
     * @author 刘国强
     * @date 2020-7-1
     */
    String getRoleIds(Integer uId);

}
