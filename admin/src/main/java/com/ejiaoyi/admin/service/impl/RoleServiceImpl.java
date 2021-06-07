package com.ejiaoyi.admin.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.admin.mapper.RoleMapper;
import com.ejiaoyi.admin.mapper.RoleMenuMapper;
import com.ejiaoyi.admin.service.IRoleService;
import com.ejiaoyi.common.entity.Role;
import com.ejiaoyi.common.entity.RoleMenu;
import com.ejiaoyi.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author fengjunhong
 * @since 2020-03-25
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl implements IRoleService {

    @Autowired
    RoleMapper roleMapper;
    @Autowired
    RoleMenuMapper roleMenuMapper;

    @Override
    public String pagedRole() {
        Page page = this.getPageForLayUI();
        List list = roleMapper.selectPage(page, null).getRecords();
        return this.initJsonForLayUI(list, (int) page.getTotal());
    }

    @Override
    public boolean addRole(Role role) {
        return 1 == roleMapper.insert(role);
    }

    @Override
    public boolean deleteRole(Integer[] ids) {
        Assert.notEmpty(ids, "param ids can not be empty!");
        return roleMapper.deleteBatchIds(Arrays.asList(ids)) == ids.length;
    }

    @Override
    public boolean updateRole(Role role) {
        return 1 == roleMapper.updateById(role);
    }

    @Override
    public Role getRole(Role role) {
        if (role != null) {
            QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>();
            //1、按照id查询
            queryWrapper.eq("id", role.getId());
            return roleMapper.selectOne(queryWrapper);
        }
        return null;
    }

    @Override
    public Role getRoleById(Role role) {
        role = roleMapper.selectById(role.getId());
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<RoleMenu>().eq("ROLE_ID", role.getId());
        List<RoleMenu> list = roleMenuMapper.selectList(queryWrapper);
        if (list.size() > 0) {
            String menuId = "";
            for (RoleMenu roleMenu : list) {
                menuId += "/" + roleMenu.getMenuId();
            }
            role.setMenuId(menuId);
        }
        return role;
    }

    // 更新用户角色时，删除菜单缓存
    @CacheEvict(value = "Menu", allEntries = true)
    @Override
    public boolean menuPermission(Integer roleId, Integer[] ids) {
        Assert.notNull(roleId, "param roleId can not be null");
        Assert.notNull(ids, "param ids can not be null");

        // 清除旧的角色菜单关系
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<RoleMenu>().eq("ROLE_ID", roleId);
        roleMenuMapper.delete(queryWrapper);

        int result = 0;
        RoleMenu roleMenu = new RoleMenu();
        roleMenu.setRoleId(roleId);
        for (Integer id : ids) {
            roleMenu.setMenuId(id);
            if (1 == roleMenuMapper.insert(roleMenu)) {
                result += 1;
            }
        }
        return result == ids.length;
    }
}
