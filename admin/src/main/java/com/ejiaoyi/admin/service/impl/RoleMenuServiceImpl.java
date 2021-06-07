package com.ejiaoyi.admin.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.admin.mapper.RoleMenuMapper;
import com.ejiaoyi.admin.service.IRoleMenuService;
import com.ejiaoyi.common.entity.Menu;
import com.ejiaoyi.common.entity.Role;
import com.ejiaoyi.common.entity.RoleMenu;
import com.ejiaoyi.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色菜单关系表 服务实现类
 * </p>
 *
 * @author fengjunhong
 * @since 2020-03-25
 */
@Service
public class RoleMenuServiceImpl extends BaseServiceImpl implements IRoleMenuService {

    @Autowired
    RoleMenuMapper roleMenuMapper;

    @Override
    public List<Menu> listTreeMenuByRole(Role role) {
        return roleMenuMapper.listTreeMenuByRole(role);
    }

    @Override
    public void delRoleMenuByMenuId(Integer[] menuIds) {
        Assert.notEmpty(menuIds, "param menuId can not be empty");
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<RoleMenu>();
        queryWrapper.in("MENU_ID", menuIds);
        roleMenuMapper.delete(queryWrapper);
    }
}
