package com.ejiaoyi.admin.controller;


import com.ejiaoyi.admin.service.impl.RoleMenuServiceImpl;
import com.ejiaoyi.admin.service.impl.RoleServiceImpl;
import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.entity.Menu;
import com.ejiaoyi.common.entity.Role;
import com.ejiaoyi.common.enums.DMLType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author fengjunhong
 * @since 2020-03-25
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    RoleServiceImpl roleService;
    @Autowired
    RoleMenuServiceImpl roleMenuService;

    /**
     * 跳转角色管理页面
     *
     * @return 角色管理页面
     */
    @RequestMapping("/frameRolePage")
    public ModelAndView frameRolePage() {
        return new ModelAndView("/role/frameRolePage");
    }

    /**
     * 获取角色JSON
     *
     * @return 角色JSON
     */
    @RequestMapping("/pagedRole")
    public String pagedRole() {
        return roleService.pagedRole();
    }

    /**
     * 跳转新增角色页面
     *
     * @return
     */
    @RequestMapping("/addRolePage")
    public ModelAndView addRolePage() {
        return new ModelAndView("/role/addRole");
    }

    /**
     * 新增角色
     *
     * @param role 新增角色信息
     * @return 返回新增结果
     */
    @RequestMapping("/addRole")
    @UserLog(value = "'添加角色信息: role='+#role.toString()", dmlType = DMLType.INSERT)
    public Boolean addRole(Role role) {
        return roleService.addRole(role);
    }

    /**
     * 批量删除角色
     *
     * @param ids 角色信息
     * @return 删除结果
     */
    @RequestMapping("/deleteRole")
    @UserLog(value = "'批量删除角色信息: ids='+#ids", dmlType = DMLType.DELETE)
    public Boolean deleteRole(Integer[] ids) {
        return roleService.deleteRole(ids);
    }

    /**
     * 跳转角色修改页面
     *
     * @param role 角色信息
     * @return
     */
    @RequestMapping("/updateRolePage")
    public ModelAndView updateRolePage(Role role) {
        ModelAndView mav = new ModelAndView("/role/updateRole");
        role = roleService.getRole(role);
        mav.addObject("role", role);
        return mav;
    }

    /**
     * 修改角色信息
     *
     * @param role 角色信息
     * @return 修改结果
     */
    @RequestMapping("/updateRole")
    @UserLog(value = "'更新角色信息: role='+#role.toString()", dmlType = DMLType.UPDATE)
    public boolean updateRole(Role role) {
        return roleService.updateRole(role);
    }

    /**
     * 跳转菜单权限页面
     *
     * @param role 角色信息
     * @return
     */
    @RequestMapping("/menuPermissionPage")
    public ModelAndView menuPermissionPage(Role role) {
        ModelAndView mav = new ModelAndView("/role/menuPermission");
        role = roleService.getRoleById(role);
        mav.addObject("role", role);
        return mav;
    }

    /**
     * 根据角色获取菜单list
     *
     * @param role 角色对象
     * @return 菜单列表
     */
    @RequestMapping("/listTreeMenuByRole")
    public List<Menu> listTreeMenuByRole(Role role) {
        return roleMenuService.listTreeMenuByRole(role);
    }

    /**
     * 菜单权限设置
     *
     * @param roleId 角色ID
     * @param ids    菜单id
     * @return 菜单权限设置结果
     */
    @RequestMapping("/menuPermission")
    @UserLog(value = "'菜单权限设置：roleId='+#roleId+',ids= '+#ids", dmlType = DMLType.UPDATE)
    public boolean menuPermission(Integer roleId, Integer[] ids) {
        return roleService.menuPermission(roleId, ids);
    }

}
