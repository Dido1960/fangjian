package com.ejiaoyi.admin.service;

import com.ejiaoyi.common.entity.Menu;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author fengjunhong
 * @since 2020-03-25
 */
public interface IMenuService {

    /**
     * 初始化Menu列表
     */
    List<Menu> initMenuList();

    /**
     * 通过系统菜单主键获取菜单信息
     *
     * @param id 菜单主键
     * @return 菜单信息
     */
    Menu getMenuById(Integer id);

    /**
     * 根据父级ID 启用状态查询菜单
     *
     * @param parentId 父级菜单主键
     * @param enabled  启用状态
     * @return 菜单信息集合
     */
    List<Menu> listTreeMenu(Integer parentId, Integer enabled);

    /**
     * 启用或者禁用菜单
     *
     * @param ids     菜单主键数组
     * @param enabled 启用状态
     * @return
     */
    boolean updateMenuEnabled(@RequestParam Integer[] ids, @RequestParam Integer enabled);

    /**
     * 批量删除菜单信息
     *
     * @param ids 菜单主键数组
     */
    boolean delMenu(Integer[] ids);

    /**
     * 返回菜单JSON
     *
     * @param menuName 菜单名称
     * @return 菜单JSON
     */
    String pagedMenu(String menuName, Integer id);

    /**
     * 根据父级id返回菜单最大排序号
     *
     * @param pId 父级id
     * @return 菜单最大排序号
     */
    Integer maxOrderNo(Integer pId);

    /**
     * 添加菜单
     *
     * @param menu 菜单对象
     * @return
     */
    boolean addMenu(Menu menu);

    /**
     * 修改菜单
     *
     * @param menu 菜单对象
     * @return
     */
    boolean updateMenu(Menu menu);

    /**
     * 拖拽菜单
     *
     * @param id       菜单主键
     * @param parentId 父级主键
     * @param orderNo  排序号
     */
    boolean dragMenu(@RequestParam Integer id, @RequestParam Integer parentId, @RequestParam Integer orderNo);

    /**
     * 根据角色id数组，获取菜单列表
     *
     * @param roleIds 角色id数组
     * @return 获取菜单列表
     */
    List<Menu> listMenuByRoleIdS(List<Integer> roleIds);

    /**
     * 获取当前用户菜单列表
     *
     * @param uId 用户id
     * @return 用户菜单列表
     */
    List<Menu> listMenuByUserId(Integer uId);
}
