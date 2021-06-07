package com.ejiaoyi.admin.controller;

import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.entity.CompanyUser;
import com.ejiaoyi.common.entity.Reg;
import com.ejiaoyi.common.enums.DMLType;
import com.ejiaoyi.common.service.IRegService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @Description:
 * @Auther: liuguoqiang
 * @Date: 2020/7/14 09:45
 */
@RestController
@RequestMapping("reg")
public class RegController {
    @Autowired
    private IRegService regService;

    /**
     * 区划框架页 用于区划维护
     *
     * @return
     */
    @RequestMapping("/frameRegPage")
    public ModelAndView frameRegPage() {
        return new ModelAndView("/reg/frameReg");
    }

    /**
     * 区划数 页面
     *
     * @return
     */
    @RequestMapping("/treeRegPage")
    public ModelAndView treeRegPage() {
        return new ModelAndView("/reg/treeReg");
    }

    /**
     * 用户设置区划页面
     *
     * @return
     */
    @RequestMapping("/userTreeRegPage")
    public ModelAndView userTreeRegPage(@Param("id") String id) {
        ModelAndView mv = new ModelAndView("/reg/userTreeReg");
        mv.addObject("userId",id);
        return mv;
    }

    /**
     * 查询部门区划list
     *
     * @param parentRegId 父级区划id
     * @return 查询父级区划id下的所有子集区划list
     */
    @RequestMapping("/listReg")
    public List<Reg> listReg(Integer parentRegId) {
        return regService.listAdminReg(parentRegId);
    }

    /**
     * 更新区划状态
     *
     * @param checked_ids 选择的区划id列表
     * @param state       区划状态
     * @return 更新结果
     */
    @PostMapping("/updateRegState")
    @UserLog(value = "'更新区划状态:'+ #ids+' '+#state", dmlType = DMLType.UPDATE)
    public Boolean updateRegEnabled(Integer[] checked_ids, Integer state) {
        return regService.updateRegEnabled(checked_ids, state);
    }

    /**
     * 添加区划数 页面
     *
     * @return
     */
    @RequestMapping("/addRegPage")
    public ModelAndView addRegPage() {
        return new ModelAndView("/reg/addReg");
    }


    /**
     * 添加行政区划
     *
     * @param provinceName 省级名称
     * @param cityName     市级名称
     * @param districtName 县级名称
     * @param provinceCode 省级代码
     * @param cityCode     市级代码
     * @param districtCode 县级代码
     * @param state        状态
     * @return
     */
    @RequestMapping("/addReg")
    @UserLog(value = "'新增行政区划:'+ #provinceName+'-'+#cityName+'-'+#districtName", dmlType = DMLType.INSERT)
    public Boolean addReg(String provinceName, String cityName, String districtName, String provinceCode, String cityCode, String districtCode, Integer state) {

        return regService.addReg(provinceName, cityName, districtName, provinceCode, cityCode, districtCode, state);
    }

    /**
     * 跳转项目权限分配页面
     * @return
     */
    @RequestMapping("/frameProjectPage")
    public ModelAndView frameProjectPage(){
        return new ModelAndView("/reg/frameProject");
    }

    /**
     * 跳转 区划设置页面
     *
     * @param user 角色信息
     * @return
     */
    @RequestMapping("/userRegPage")
    public ModelAndView userRegPage(CompanyUser user) {
        ModelAndView mav = new ModelAndView("/reg/userReg");
        mav.addObject("userId",user.getId());
        return mav;
    }

}
