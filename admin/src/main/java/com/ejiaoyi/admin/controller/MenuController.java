package com.ejiaoyi.admin.controller;

import com.ejiaoyi.admin.service.impl.MenuServiceImpl;
import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.entity.Menu;
import com.ejiaoyi.common.enums.DMLType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author fengjunhong
 * @since 2020-03-25
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    MenuServiceImpl menuService;

    /**
     * 菜单管理框架页面
     *
     * @return
     */
    @RequestMapping("/frameMenuPage")
    public ModelAndView frameMenuPage() {
        return new ModelAndView("/menu/frameMenu");
    }

    /**
     * 菜单树页面
     *
     * @return
     */
    @RequestMapping("/treeMenuPage")
    public ModelAndView treeMenuPage() {
        return new ModelAndView("/menu/treeMenu");
    }

    /**
     * 通过父级ID 获取子级菜单
     *
     * @param parentId 父级菜单主键
     * @return
     */
    @RequestMapping("/listTreeMenu")
    public List<Menu> listTreeMenu(Integer parentId, Integer enabled) {
        return menuService.listTreeMenu(parentId, enabled);
    }

    /**
     * 启用或者禁用菜单
     *
     * @param ids     菜单主键数组
     * @param enabled 启用状态
     * @return
     */
    @PostMapping("/updateMenuEnabled")
    @UserLog(value = "'启用或禁用菜单: ids '+#ids+' enabled '+#enabled", dmlType = DMLType.UPDATE)
    public boolean updateMenuEnabled(@RequestParam Integer[] ids, @RequestParam Integer enabled) {
        return menuService.updateMenuEnabled(ids, enabled);
    }

    /**
     * 批量删除菜单
     *
     * @param ids 菜单主键数组
     * @return
     */
    @PostMapping("/delMenu")
    @UserLog(value = "'删除菜单: '+#ids", dmlType = DMLType.DELETE)
    public boolean delMenu(@RequestParam Integer[] ids) {
        return menuService.delMenu(ids);
    }

    /**
     * 新增菜单页面
     *
     * @return
     */
    @RequestMapping("/addMenuPage")
    public ModelAndView addMenuPage(Integer id, String menuName) {
        ModelAndView mav = new ModelAndView("/menu/addMenu");
        mav.addObject("id", id);
        mav.addObject("menuName", menuName);
        return mav;
    }

    /**
     * 修改菜单页面
     *
     * @param id 菜单主键
     * @return
     */
    @RequestMapping("/updateMenuPage")
    public ModelAndView updateMenuPage(Integer id) {
        Menu menu = menuService.getMenuById(id);
        ModelAndView mav = new ModelAndView("/menu/updateMenu");
        mav.addObject("menu", menu);
        return mav;
    }

    /**
     * 修改菜单
     *
     * @param menu 菜单对象
     * @return 操作成功返回true, 否则返回false
     */
    @RequestMapping("/updateMenu")
    @UserLog(value = "'修改菜单:'+ #menu+'-'+#menu", dmlType = DMLType.UPDATE)
    public Boolean updateMenu(Menu menu) {
        return menuService.updateMenu(menu);
    }

    /**
     * 返回菜单JSON
     *
     * @param menuName 菜单名称
     * @return 菜单JSON
     */
    @RequestMapping("/pagedMenu")
    @UserLog(value = "'查询区划下部门信息: menuName='+#menuName ", dmlType = DMLType.SELECT)
    public String pagedMenu(@RequestParam("menuName") String menuName, @RequestParam("id") Integer id) {
        return menuService.pagedMenu(menuName, id);
    }

    /**
     * 新增菜单
     *
     * @param menu 菜单对象
     * @return 操作成功返回true, 否则返回false
     */
    @RequestMapping("/addMenu")
    @UserLog(value = "'新增菜单:'+ #menu", dmlType = DMLType.INSERT)
    public Boolean addMenu(Menu menu) {
        return menuService.addMenu(menu);
    }

    /**
     * 拖拽菜单
     *
     * @param id       菜单主键
     * @param parentId 父级主键
     * @param orderNo  排序号
     * @return
     */
    @PostMapping("/dragMenu")
    @UserLog(value = "'拖拽菜单: id '+#id+' parentId '+#parentId+' orderNo '+#orderNo", dmlType = DMLType.UPDATE)
    public boolean dragMenu(Integer id, Integer parentId, Integer orderNo) {
        return menuService.dragMenu(id, parentId, orderNo);
    }

}
