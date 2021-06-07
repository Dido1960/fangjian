package com.ejiaoyi.admin.controller;


import com.ejiaoyi.admin.service.IUserInfoService;
import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.entity.UserCert;
import com.ejiaoyi.common.enums.DMLType;
import com.ejiaoyi.common.enums.SysUserType;
import com.ejiaoyi.common.service.IGovUserService;
import com.ejiaoyi.common.service.IUserCertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 用户CA绑定表 前端控制器
 * </p>
 *
 * @author lesgod
 * @since 2020-05-12
 */
@RestController
@RequestMapping("/admin/userCert")
public class UserCertController {

    @Autowired
    IUserInfoService userInfoService;


    @Autowired
    IGovUserService govUserService;


    @Autowired
    IUserCertService userCertService;


    /**
     * 用户的所有绑定CA信息
     *
     * @param type         用户类型 参照 userType枚举
     * @param userId       用户ID
     * @param redirectPage 传过来的页面
     * @return
     * @author lesgod
     * @date 2020/5/12 11:11
     */
    @RequestMapping("/listUserCertPage")
    public ModelAndView listUserCertPage(Integer userId, Integer type, String redirectPage) {
        ModelAndView modelAndView = new ModelAndView("/userCert/listUserCert");
        SysUserType sysUserType = SysUserType.getSysUserType(type);
        switch (sysUserType) {
            case USER:
                modelAndView.addObject("user", userInfoService.getUserInfo(userId));
                break;
            case GOVUSER:
                modelAndView.addObject("user", govUserService.getGovUser(null, userId));
                break;
            case COMPANYUSER:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        modelAndView.addObject("certs", userCertService.listUserCert(userId, SysUserType.getSysUserType(type)));
        modelAndView.addObject("userId", userId);
        modelAndView.addObject("type", type);
        modelAndView.addObject("redirectPage", redirectPage);
        return modelAndView;
    }


    /**
     * @return
     * @author lesgod
     * @date 2020/5/12 15:11
     */
    @RequestMapping("/addUserCertPage")
    public ModelAndView addUserCertPage(Integer userId, Integer type) {
        ModelAndView mav = new ModelAndView("/userCert/addUserCert");
        mav.addObject("userId", userId);
        mav.addObject("type", type);
        return mav;
    }

    /**
     * 用户与锁信息绑定
     *
     * @param
     * @return
     * @author lesgod
     * @date 2020/5/12 14:18
     */
    @UserLog(value = "'新增CA绑定 userId='+#userCert.userId+' SysUserType='+#userCert.userType",dmlType = DMLType.INSERT)
    @RequestMapping("/addUserCert")
    public void addUserCert(UserCert userCert) {
        userCertService.insertUser(userCert);
    }

    /**
     * 解开绑定
     *
     * @return
     * @author lesgod
     * @date 2020/5/12 14:22
     */
    @UserLog("'解开CA绑定 UserCertId='+#userCert.id")
    @RequestMapping("/removeUserCert")
    public void deleteUserCert(UserCert userCert) {
        userCertService.removeUserCert(userCert);
    }

    /**
     * 验证锁信息是否存在
     *
     * @return
     * @author lesgod
     * @date 2020/5/12 14:24
     */
    @RequestMapping("/bindValid")
    public JsonData bindValid(UserCert userCert) {
        JsonData msg = new JsonData();
        userCert = userCertService.bindValidUser(userCert);
        if (userCert != null) {
            SysUserType sysUserType = SysUserType.getSysUserType(userCert.getUserType());
            switch (sysUserType) {
                case USER:
                    msg.setMsg(userInfoService.getUserInfo(userCert.getUserId()).getName());
                    break;
                case GOVUSER:
                    msg.setMsg(govUserService.getGovUser(null,userCert.getUserId()).getName());
                    break;

            }
        } else {
            msg.setMsg("");
        }
        return msg;
    }


    /**
     * 解开绑定
     *
     * @return
     * @author lesgod
     * @date 2020/5/12 14:22
     */
    @UserLog("'解开CA登录绑定 UserCertId='+#userCert.id")
    @RequestMapping("/unLoginCert")
    public void unLoginCert(UserCert userCert) {
        userCert.setLoginFlag(0);
        userCertService.updateUserCert(userCert);
    }

    /**
     * 解开绑定
     *
     * @return
     * @author lesgod
     * @date 2020/5/12 14:22
     */
    @UserLog("'允许CA登录绑定 UserCertId='+#userCert.id")
    @RequestMapping("/loginCert")
    public void loginCert(UserCert userCert) {
        userCert.setLoginFlag(1);
        userCertService.updateUserCert(userCert);
    }

}
