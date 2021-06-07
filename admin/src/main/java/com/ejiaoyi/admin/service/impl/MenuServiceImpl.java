package com.ejiaoyi.admin.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.admin.service.IMenuService;
import com.ejiaoyi.common.constant.CacheName;
import com.ejiaoyi.common.entity.Menu;
import com.ejiaoyi.common.entity.RoleUser;
import com.ejiaoyi.common.mapper.MenuMapper;
import com.ejiaoyi.common.service.impl.BaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author fengjunhong
 * @since 2020-03-25
 */
@Service
public class MenuServiceImpl extends BaseServiceImpl implements IMenuService {

    @Autowired
    MenuMapper menuMapper;

    @Autowired
    RoleMenuServiceImpl roleMenuService;

    @Autowired
    RoleUserServiceImpl roleUserService;

    @Override
    public List<Menu> initMenuList() {
        // 系统启动，默认将菜单加载到缓存
        return this.listMenuByRoleIdS(null);
    }

    /**
     * 通过系统菜单主键获取菜单信息
     *
     * @param id 菜单主键
     * @return 菜单信息
     */
    @Override
    @Cacheable(value = CacheName.MENU, key = "#id")
    public Menu getMenuById(Integer id) {
        Assert.notNull("param id can not be null");
        return menuMapper.selectById(id);
    }

    @Override
    @Cacheable(value = CacheName.MENU, key = "#parentId+'_'+#enabled")
    public List<Menu> listTreeMenu(Integer parentId, Integer enabled) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<Menu>();
        if (parentId != null) {
            queryWrapper.eq("PARENT_ID", parentId);
        }
        if (enabled != null) {
            queryWrapper.eq("ENABLED", enabled);
        }
        queryWrapper.orderByAsc("ORDER_NO");
        return menuMapper.selectList(queryWrapper);
    }

    @Override
    @CacheEvict(value = CacheName.MENU, allEntries = true)
    public boolean updateMenuEnabled(Integer[] ids, Integer enabled) {
        int result = 0;

        Menu menu = Menu.builder().enabled(enabled).build();
        for (Integer id : ids) {
            QueryWrapper<Menu> queryWrapper = new QueryWrapper<Menu>().eq("id", id);
            result += menuMapper.update(menu, queryWrapper);
        }
        return result == ids.length;
    }

    /**
     * 批量删除菜单信息
     *
     * @param ids 菜单主键数组
     */
    @Override
    @CacheEvict(value = CacheName.MENU, allEntries = true)
    public boolean delMenu(Integer[] ids) {
        try {
            Assert.notNull(ids, "param ids can not be null");

            // 删除角色与菜单的关系
            roleMenuService.delRoleMenuByMenuId(ids);

            // 删除菜单
            menuMapper.deleteBatchIds(Arrays.asList(ids));

            // 菜单重新排序

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public String pagedMenu(String menuName, Integer id) {
        Page page = this.getPageForLayUI();
        List<Menu> list = menuMapper.pagedMenu(page, menuName, id);
        return this.initJsonForLayUI(list, (int) page.getTotal());
    }

    @Override
    @Cacheable(value = CacheName.MENU, key = "#pId")
    public Integer maxOrderNo(Integer pId) {
        return menuMapper.maxOrderNo(pId) == null ? 0 : menuMapper.maxOrderNo(pId);
    }

    @Override
    @CacheEvict(value = CacheName.MENU, allEntries = true)
    public boolean addMenu(Menu menu) {
        //启用状态
        menu.setEnabled(1);
        // 为一级菜单，设置父级id
        if (menu.getParentId() == null) {
            menu.setParentId(-1);
        }
        // 设置排序号
        menu.setOrderNo(maxOrderNo(menu.getParentId()) + 1);
        return 1 == menuMapper.insert(menu);
    }

    @Override
    @CacheEvict(value = CacheName.MENU, allEntries = true)
    public boolean updateMenu(Menu menu) {
        return 1 == menuMapper.updateById(menu);
    }

    @Override
    @CacheEvict(value = CacheName.MENU, allEntries = true)
    public boolean dragMenu(Integer id, Integer parentId, Integer orderNo) {
        Assert.notNull(id, "param id can not be null");
        Assert.notNull(parentId, "param parentId can not be null");
        Assert.notNull(orderNo, "param orderNo can not be null");
        // 查询需要位移的菜单列表
        List<Menu> listMenu = menuMapper.listMenuByPidAndOrderNo(parentId, orderNo);

        if (!CollectionUtils.isEmpty(listMenu)) {
            Integer[] ids = new Integer[listMenu.size()];
            for (int i = 0; i < listMenu.size(); i++) {
                ids[i] = listMenu.get(i).getId();
            }
            menuMapper.moveMenu(ids);
        }
        // 修改当前菜单
        Menu menu = Menu.builder()
                .id(id)
                .parentId(parentId)
                .orderNo(orderNo)
                .build();

        return 1 == menuMapper.updateById(menu);
    }

    @Override
    public List<Menu> listMenuByRoleIdS(List<Integer> roleIds) {
        return menuMapper.listMenuByRoleIdS(roleIds);
    }

    @Override
    @Cacheable(value = CacheName.MENU, key = "#uId", unless = "#result==null")
    public List<Menu> listMenuByUserId(Integer uId) {
        // 获取当前用户角色列表
        List<RoleUser> roleUserList = roleUserService.listRoleUser(uId);
        // 获取用户所有权限id
        List<Integer> roleIds = new ArrayList<Integer>();
        for (RoleUser roleUser : roleUserList) {
            if (roleUser.getRoleId() != null) {
                roleIds.add(roleUser.getRoleId());
            }
        }
        List<Menu> menuList = this.listMenuByRoleIdS(roleIds);
        // 去掉重复的菜单（多个角色之间，存在相同菜单）
        List<Menu> result = new ArrayList<>();
        for (Menu menu : menuList) {
            if (menu != null && menu.getParentId() == -1) {
                menu.setSubMenuList(sortMenu(menuList, menu.getId()));
                result.add(menu);
            }
        }
        return result;
    }


    //对菜单排序
    private ArrayList<Menu> sortMenu(List<Menu> list, Integer parentId) {
        ArrayList<Menu> subList = new ArrayList<>();
        for (Menu menu : list) {
            if (menu != null && menu.getParentId() - parentId == 0) {
                subList.add(menu);
            }
        }
        return subList;
    }
}
