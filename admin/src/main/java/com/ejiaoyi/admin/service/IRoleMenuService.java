package com.ejiaoyi.admin.service;

import com.ejiaoyi.common.entity.Menu;
import com.ejiaoyi.common.entity.Role;

import java.util.List;

/**
 * <p>
 * 角色菜单关系表 服务类
 * </p>
 *
 * @author fengjunhong
 * @since 2020-03-25
 */
public interface IRoleMenuService {

    /**
     * 根据角色获取菜单list
     *
     * @param role 角色对象
     * @return 菜单列表
     */
    List<Menu> listTreeMenuByRole(Role role);


//    void updateRoleMenu(Role role);

    /**
     * 删除菜单与角色的关系
     *
     * @param menuIds 菜单id数组
     */
    void delRoleMenuByMenuId(Integer[] menuIds);


}
