package com.ejiaoyi.admin.controller;


import com.ejiaoyi.admin.support.AuthUser;
import com.ejiaoyi.admin.support.CurrentUserHolder;
import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.entity.Dep;
import com.ejiaoyi.common.enums.DMLType;
import com.ejiaoyi.common.service.impl.DepServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 部门 前端控制器
 * </p>
 *
 * @author samzqr
 * @since 2020-07-02
 */
@RestController
@RequestMapping("/dep")
public class DepController {

    @Autowired
    DepServiceImpl depService;

    /**
     * 跳转区划树页面
     *
     * @return
     */
    @RequestMapping("/treeDepPage")
    public ModelAndView treeDepPage() {
        AuthUser user = CurrentUserHolder.getUser();
        ModelAndView mav = new ModelAndView("/dep/treeDep");
        mav.addObject("regId", user.getRegId());
        return mav;
    }

    /**
     * 返回行政区划对应页面
     *
     * @return
     */
    @RequestMapping("/listDepPage")
    public ModelAndView listDepPage(Dep dep) {
        ModelAndView mav = new ModelAndView("/dep/listDep");
        mav.addObject("dep", dep);
        return mav;
    }


    /**
     * 返回区划下部门JSON
     *
     * @param dep 部门对象
     * @return 区划下的部门JSON
     */
    @RequestMapping("/pagedDep")
    @UserLog(value = "'查询区划下部门信息: dep='+#dep.toString() ", dmlType = DMLType.SELECT)
    public String pagedDep(Dep dep) {
        return depService.pagedDep(dep);
    }


    /**
     * 跳转新增部门页面
     *
     * @param dep 部门对象
     * @return
     */
    @RequestMapping("/addDepPage")
    public ModelAndView addDepPage(Dep dep) {
        ModelAndView mav = new ModelAndView("/dep/addDep");
        mav.addObject("dep", dep);

        return mav;
    }

    /**
     * 添加部门
     *
     * @param dep 部门对象
     * @return 成功返回true, 失败返回false
     */
    @RequestMapping("/addDep")
    public Boolean addDep(Dep dep) {
        return depService.addDep(dep);
    }


    /**
     * 修改部门启用状态
     *
     * @param dep 部门对象
     * @return 修改成功：true，修改失败：false
     */
    @RequestMapping("/updateDepStatus")
    @UserLog(value = "'修改部门信息: dep='+#dep.toString()", dmlType = DMLType.UPDATE)
    public Boolean updateDepStatus(Dep dep) {
        System.out.println(dep);
        return depService.updateDepStatus(dep);
    }

    /**
     * 修改部门信息
     *
     * @param dep 部门对象
     * @return 修改成功：true，修改失败：false
     */
    @RequestMapping("/updateDep")
    @UserLog(value = "'修改部门信息: dep='+#dep.toString()", dmlType = DMLType.UPDATE)
    public Boolean updateDep(Dep dep) {
        System.out.println(dep);
        return depService.updateDep(dep);
    }
    /**
     * 跳转修改部门页面
     *
     * @param id 部门对象id
     * @return
     */
    @RequestMapping("/updateDepPage")
    public ModelAndView updateDepPage(Integer id) {
        ModelAndView mav = new ModelAndView("dep/updateDep");
        //构建新的部门信息
        Dep dep = depService.getDepById(id);
        //回显数据
        mav.addObject("dep", dep);
        return mav;

    }

    /**
     * 跳转查看部门页面
     *
     * @param id 部门对象id
     * @return
     */
    @RequestMapping("/showDepPage")
    public ModelAndView showDepPage(Integer id) {
        ModelAndView mav = new ModelAndView("dep/showDep");
        //构建新的部门信息
        Dep dep = depService.getDepById(id);
        //放入区划名称
        dep.setRegName(dep.getRegName());
        //回显数据
        mav.addObject("dep", dep);
        return mav;
    }

    /**
     * 返回区划下部门JSON
     *
     * @param regId 部门对象
     * @return 区划下的部门JSON
     */
    @RequestMapping("/pagedDepByRegId")
    @UserLog(value = "'查询区划下所有部门信息: regId='+#regId ", dmlType = DMLType.SELECT)
    public String pagedDepByRegId(Integer regId) {
        Dep dep = new Dep();
        return depService.pagedDep(dep);
    }


    /**
     * 批量部门
     *
     * @param ids 需要批量部门删除的id
     * @return 成功返回true, 失败返回false
     */
    @RequestMapping("/delDep")
    @UserLog(value = "'删除部门信息: ids='+#ids", dmlType = DMLType.DELETE)
    public JsonData delDep(Integer[] ids) {

        return depService.delDep(ids);
    }

}
