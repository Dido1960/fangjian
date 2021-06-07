package com.ejiaoyi.admin.service;

import com.ejiaoyi.common.entity.Role;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author fengjunhong
 * @since 2020-03-25
 */
public interface IRoleService {

    /**
     * 获取角色JSON
     *
     * @return 角色JSON
     */
    String pagedRole();

    /**
     * 新增角色
     *
     * @param role 角色实体
     * @return 操作成功返回true, 否则返回 false
     */
    boolean addRole(Role role);

    /**
     * 批量删除角色
     *
     * @param ids 角色id数组
     * @return 操作成功返回true, 否则返回 false
     */
    boolean deleteRole(Integer[] ids);

    /**
     * 修改角色
     *
     * @param role 角色实体
     * @return 操作成功返回true, 否则返回 false
     */
    boolean updateRole(Role role);

    /**
     * 获取角色对象
     *
     * @param role 角色实体
     * @return 角色对象
     */
    Role getRole(Role role);

    /**
     * 根据Id获取角色对象
     *
     * @param role 角色信息
     * @return 角色信息
     */
    public Role getRoleById(Role role);

    /**
     * 菜单权限设置
     *
     * @param roleId 角色ID
     * @param ids    菜单id
     * @return 菜单权限设置结果
     */
    @PostMapping("/menuPermission")
    public boolean menuPermission(Integer roleId, Integer[] ids);

}
