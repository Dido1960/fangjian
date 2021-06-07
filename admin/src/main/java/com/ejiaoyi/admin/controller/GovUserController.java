package com.ejiaoyi.admin.controller;


import com.ejiaoyi.admin.support.AuthUser;
import com.ejiaoyi.admin.support.CurrentUserHolder;
import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.crypto.SM2Util;
import com.ejiaoyi.common.entity.GovUser;
import com.ejiaoyi.common.enums.DMLType;
import com.ejiaoyi.common.service.impl.DepServiceImpl;
import com.ejiaoyi.common.service.impl.GovUserServiceImpl;
import com.ejiaoyi.common.service.impl.RegServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 政府用户 前端控制器
 * </p>
 *
 * @author samzqr
 * @since 2020-07-02
 */
@RestController
@RequestMapping("/govUser")
public class GovUserController {

    @Autowired
    private GovUserServiceImpl govUserService;

    @Autowired
    private RegServiceImpl regService;

    @Autowired
    private DepServiceImpl depService;

    /**
     * 人员框架页 用于人员维护
     *
     * @return
     */
    @RequestMapping("/frameUserPage")
    public ModelAndView frameUserPage() {
        return new ModelAndView("/govUser/frameUserInfo");
    }

    /**
     * 跳转区划树页面
     *
     * @return
     */
    @RequestMapping("/treeRegPage")
    public ModelAndView treeRegPage() {
        AuthUser user = CurrentUserHolder.getUser();
        ModelAndView mav = new ModelAndView("/govUser/treeReg");
        mav.addObject("regId", user.getRegId());
        return mav;
    }

    /**
     * 分页查询政府用户
     * @param depId 部门id
     * @param name
     * @param enabled 启用状态
     * @return
     */
    @RequestMapping("/pagedUser")
    public String pagedUser(Integer depId, String name, Integer enabled) {
        return govUserService.pagedUser(depId, name, enabled);
    }

    /**
     * 跳转到新增政府用户页面
     * @param depId 部门id
     * @param regId 区划id
     * @return
     */
    @RequestMapping("/addGovUserPage")
    public ModelAndView addGovUserPage(Integer depId, Integer regId) {
        ModelAndView mav = new ModelAndView("/gov/addGovUser");
        mav.addObject("dep", depService.getDepById(depId));
        mav.addObject("regId", regId);
        return mav;
    }

    /**
     *新增政府用户
     * @param govUser
     * @return
     */
    @RequestMapping("/addGovUser")
    @UserLog(value = "'新增政府用户'+#govUser",dmlType = DMLType.INSERT)
    public Boolean addGovUser(GovUser govUser) throws Exception {
        return govUserService.addGovUser(govUser);

    }

    /**
     *根据id删除用户
     * @param id
     * @return
     */
    @RequestMapping("/deleteById")
    public Boolean deleteById(Integer id){
        return govUserService.deleteById(id);
    }


    /**
     * 跳转修改政府人员页面
     *
     * @param id 用户id
     * @param regId 区划id
     * @return 政府人员
     */
    @RequestMapping("/updateGovUserPage")
    public ModelAndView updateGovUserPage(Integer id, Integer regId) throws Exception {
        ModelAndView mav = new ModelAndView("/gov/updateGovUser");
        GovUser govUser = govUserService.getGovUserById(id);
        govUser.setPassword(SM2Util.decrypt(govUser.getPassword()));
        mav.addObject("govUser", govUser);
        mav.addObject("dep", depService.getDepById(govUser.getDepId()));
        mav.addObject("regId",regId);
        return mav;
    }

    /**
     * 跳转选择部门页面
     *
     * @param regId 区划id
     * @return
     */
    @RequestMapping("/chooseDepPage")
    public ModelAndView chooseDepPage(Integer regId) {
        ModelAndView mav = new ModelAndView("/gov/chooseDep");
        mav.addObject("regId", regId);
        return mav;
    }

    /**
     * 修改政府用户
     *
     * @param govUser 政府用户
     * @return 受影响的行数
     */
    @UserLog(value = "'更新政府用户'+#govUser.name", dmlType = DMLType.UPDATE)
    @RequestMapping("/updateGovUser")
    public boolean updateGovUser(GovUser govUser) throws Exception {
        return govUserService.updateGovUser(govUser);
    }


    /**
     * 跳转查看政府人员页面
     *
     * @param regNo 区划id
     *
     * @return 政府人员
     */
    @RequestMapping("/showGovUserPage")
    public ModelAndView showGovUserPage(String regNo, Integer id) {
        ModelAndView mav = new ModelAndView("/gov/chooseUser");
        mav.addObject("regNo", regNo);
        mav.addObject("id", id);
        return mav;
    }

    /**
     * 根据区划删除
     * @param regNo 区划no
     * @param govUser 用户实体
     * @return
     */
    @RequestMapping("/pageUserByRegId")
    public String pageUserByRegId(String regNo, GovUser govUser){
        return  govUserService.pageUserByRegId(regNo,govUser);
    }

    /**
     * 查询全部政府用户
     * @return
     */
    @RequestMapping("/listAllGovUser")
    public String listAllGovUser(){
        return govUserService.listAllGovUser();
    }








}
