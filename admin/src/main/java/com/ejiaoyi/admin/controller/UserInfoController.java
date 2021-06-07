package com.ejiaoyi.admin.controller;


import com.ejiaoyi.admin.service.impl.RoleUserServiceImpl;
import com.ejiaoyi.admin.service.impl.UserInfoServiceImpl;
import com.ejiaoyi.admin.support.AuthUser;
import com.ejiaoyi.admin.support.CurrentUserHolder;
import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.entity.Reg;
import com.ejiaoyi.common.entity.RoleUser;
import com.ejiaoyi.common.entity.UserInfo;
import com.ejiaoyi.common.enums.DMLType;
import com.ejiaoyi.common.service.impl.DepServiceImpl;
import com.ejiaoyi.common.service.impl.FDFSServiceImpl;
import com.ejiaoyi.common.service.impl.RegServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author fengjunhong
 * @since 2020-03-25
 */
@RestController
@RequestMapping("/user")
public class UserInfoController {

    @Autowired
    UserInfoServiceImpl userInfoService;

    @Autowired
    RegServiceImpl regService;

    @Autowired
    DepServiceImpl depService;

    @Autowired
    RoleUserServiceImpl roleUserService;

    @Autowired
    FDFSServiceImpl fdfsService;

    /**
     * 人员框架页 用于人员维护
     *
     * @return
     */
    @RequestMapping("/frameUserPage")
    public ModelAndView frameUserPage() {
        return new ModelAndView("/user/frameUserInfo");
    }


    /**
     * 跳转区划树页面
     *
     * @return
     */
    @RequestMapping("/treeRegPage")
    public ModelAndView treeRegPage() {
        AuthUser user = CurrentUserHolder.getUser();
        ModelAndView mav = new ModelAndView("/user/treeReg");
        mav.addObject("regId", user.getRegId());
        return mav;
    }

    /**
     * 返回区划下用户页面
     *
     * @return
     */
    @RequestMapping("/userInfoPage")
    public ModelAndView userInfoPage(Integer regId) {
        ModelAndView mav = new ModelAndView("/user/listPagedUserInfo");
        mav.addObject("regId", regId);
        return mav;
    }

    /**
     * 获取用户信息JSON
     *
     * @param regId   区划id
     * @param name    人员名称
     * @param enabled 启用状态
     * @return
     */
    @RequestMapping("/pagedUser")
    public String pagedUser(Integer regId, String name, Integer enabled) {
        return userInfoService.pagedUser(regId, name, enabled);
    }

    /**
     * 获取系统用户信息JSON
     *
     * @param name 人员名称
     * @return
     */
    @RequestMapping("/pagedUserLikeName")
    public String pagedUser(String name) {
        return userInfoService.pagedUser(null, name, null);
    }

    /**
     * 跳转人员修改页面
     *
     * @param userInfo 人员信息
     * @return
     */
    @RequestMapping("/updateUserPage")
    public ModelAndView updateUserPage(UserInfo userInfo) {
        ModelAndView mav = new ModelAndView("/user/updateUserPage");
        //按照id,只能获取一条
        userInfo = userInfoService.listUserInfo(userInfo).get(0);
        //按照用户对应区划id,只能获取一条
        Reg reg = regService.listReg(userInfo.getRegId()).get(0);
        userInfo.setRoleId(roleUserService.getRoleIds(userInfo.getId()));
        List<RoleUser> roleUsers = roleUserService.listRoleUser(userInfo.getId());
        String rolesNmaes = "";
        for (RoleUser roleUser : roleUsers) {
            rolesNmaes += "/" + roleUser.getroleName();
        }
        mav.addObject("userInfo", userInfo);
        mav.addObject("reg", reg);
        mav.addObject("rolesNmaes",rolesNmaes);
        return mav;
    }

    /**
     * 修改用户信息
     *
     * @param userInfo 用户信息
     * @return 修改成功：true，修改失败：false
     */
    @RequestMapping("/updateUser")
    @UserLog(value = "'修改人员信息: userInfo='+#userInfo.toString()", dmlType = DMLType.UPDATE)
    public boolean updateUser(UserInfo userInfo, String roleIds) {
        return userInfoService.updateUser(userInfo, roleIds);
    }


    /**
     * 跳转添加人员页面
     *
     * @param regId 区划ID
     * @return
     */
    @RequestMapping("/addUserPage")
    public ModelAndView addUserPage(Integer regId) {
        ModelAndView mav = new ModelAndView("/user/addUser");
        Reg reg = regService.listReg(regId).get(0);
        mav.addObject("reg", reg);
        return mav;
    }

    /**
     * 跳转选择角色页面
     *
     * @param roleId 菜单权限id
     * @return
     */
    @RequestMapping("/chooseRolePage")
    public ModelAndView chooseRolePage(String roleId) {
        ModelAndView mav = new ModelAndView("/user/chooseRole");
        mav.addObject("roleId", roleId);
        return mav;
    }

    /**
     * 添加用户
     *
     * @param userInfo 用户实体
     * @return
     */
    @RequestMapping("/addUser")
    @UserLog(value = "'添加用户信息: userInfo='+#userInfo.toString()", dmlType = DMLType.INSERT)
    public boolean addUser(UserInfo userInfo) {
        return userInfoService.addUser(userInfo);
    }

    /**
     * 批量删除用户
     *
     * @param ids 用户id列表
     * @return 操作成功 返回true
     */
    @RequestMapping("/deleteUser")
    @UserLog(value = "'批量删除用户信息: ids='+#ids", dmlType = DMLType.DELETE)
    public boolean deleteUser(Integer[] ids) {
        return userInfoService.deleteUser(ids);
    }

    /**
     * 跳转查看人员页面
     *
     * @param
     * @return
     */
    @RequestMapping("/showUserPage")
    public ModelAndView showUserPage(UserInfo userInfo) {
        ModelAndView mav = new ModelAndView("/user/showUserPage");
        userInfo = userInfoService.listUserInfo(userInfo).get(0);
        Reg reg = regService.listReg(userInfo.getRegId()).get(0);
        List<RoleUser> roleUsers = roleUserService.listRoleUser(userInfo.getId());
        String rolesNmaes = "";
        for (RoleUser roleUser : roleUsers) {
            rolesNmaes += "/" + roleUser.getroleName();
        }
        mav.addObject("regionalism", reg);
        mav.addObject("userInfo", userInfo);
        mav.addObject("roleName", rolesNmaes);
        return mav;
    }

    /**
     * 跳转到修改用户密码页面
     *
     * @return
     */

    @RequestMapping("/updatePassPage")
    public ModelAndView updatePassPage() {
        AuthUser user = CurrentUserHolder.getUser();
        Integer userId = user.getUserId();
        UserInfo userInfo = userInfoService.getUserInfo(userId);
        ModelAndView mav = new ModelAndView("/user/updatePass");
        //按照id,只能获取一条
        userInfo = userInfoService.listUserInfo(userInfo).get(0);
        //按照用户对应区划id,只能获取一条
        mav.addObject("userInfo", userInfo);
        return mav;
    }


    /**
     * 修改用户的密码
     *
     * @param password 新密码
     * @return
     */
    @RequestMapping("/updatePass")
    @UserLog(value = "'用户修改密码'", dmlType = DMLType.UPDATE)
    public boolean updatePass(String password) {
        AuthUser user = CurrentUserHolder.getUser();
        Integer userId = user.getUserId();
        System.out.println("userID--" + userId);
        return userInfoService.updatePass(userId, password);
    }


    /**
     * 跳转到个人资料页面
     *
     * @return
     */
    @RequestMapping("/updateUserDataPage")
    public ModelAndView updateUserDataPage() {
        AuthUser user = CurrentUserHolder.getUser();
        Integer userId = user.getUserId();
        UserInfo userInfo = userInfoService.getUserInfo(userId);
        ModelAndView mav = new ModelAndView("/user/updateUserData");
        Reg reg = regService.listReg(userInfo.getRegId()).get(0);
        List<RoleUser> roleUsers = roleUserService.listRoleUser(userInfo.getId());
        String rolesNmaes = "";
        for (RoleUser roleUser : roleUsers) {
            rolesNmaes += "/" + roleUser.getroleName();
        }
//        存入用户头像的url
        String url = fdfsService.getUrlByUpload(userInfo.getUserFileId());
        userInfo.setUrl(url);
        mav.addObject("regionalism", reg);
        mav.addObject("userInfo", userInfo);
        mav.addObject("roleName", rolesNmaes);

        System.out.println("url-----" + userInfo.getUrl());
        return mav;
    }

    /**
     * 修改用户的基本资料
     *
     * @param userInfo 更新后的资料
     * @return
     */
    @RequestMapping("/updateUserData")
    @UserLog(value = "'用户修改信息'", dmlType = DMLType.UPDATE)
    public boolean updateUserData(UserInfo userInfo) {
        return userInfoService.updateUserData(userInfo);
    }

    @RequestMapping("/showUserData")
    public ModelAndView showUserData() {
        AuthUser user = CurrentUserHolder.getUser();
        Integer userId = user.getUserId();
        UserInfo userInfo = userInfoService.getUserInfo(userId);
        ModelAndView mav = new ModelAndView("/user/showUserData");
//        存入用户头像的url
        String url = fdfsService.getUrlByUpload(userInfo.getUserFileId());
        userInfo.setUrl(url);

        mav.addObject("userInfo", userInfo);


        return mav;
    }


    /**
     * 判断登录名是否重复
     * @param loginName 登录名
     * @author liuguoqiang
     * @return
     */
    @RequestMapping("/isLoginNameRepeat")
    public boolean isLoginNameRepeat(String loginName) {
        if (loginName != null && !"".equals(loginName)) {
            UserInfo user = userInfoService.getUserByLoginName(loginName);
            if (user != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 修改密码
     *
     * @return
     * @author  yyb
     * @date 2020/07/02 17:39
     */
    @RequestMapping("/resetUserPassword")
    public void resetUserPassword(UserInfo userInfo) {
        userInfoService.resetUserPassword(userInfo);
    }


}
