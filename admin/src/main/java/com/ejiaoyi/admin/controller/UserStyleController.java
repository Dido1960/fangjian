package com.ejiaoyi.admin.controller;


import com.ejiaoyi.admin.service.impl.UserStyleServiceImpl;
import com.ejiaoyi.admin.support.AuthUser;
import com.ejiaoyi.admin.support.CurrentUserHolder;
import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.entity.UserStyle;
import com.ejiaoyi.common.enums.DMLType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户界面 前端控制器
 * </p>
 *
 * @author samzqr
 * @since 2020-05-22
 */
@RestController
@RequestMapping("/userStyle")
public class UserStyleController {

    @Autowired
    UserStyleServiceImpl userStyleService;


    /**
     * 用户便签内容的存储
     *
     * @param note
     * @return
     */
    @RequestMapping("/addUserNote")
    @UserLog(value = "'用户修改便签'", dmlType = DMLType.UPDATE)
    public boolean addUserNote(String note) {
        AuthUser user = CurrentUserHolder.getUser();
        Integer uid = user.getUserId();
        Boolean b = userStyleService.addUserNote(uid, note);

        return b;

    }


    /**
     * 获取用户便签的内容
     *
     * @return
     */
    @RequestMapping("/getUserNote")

    public UserStyle getUserNote() {
        AuthUser user = CurrentUserHolder.getUser();
        Integer uid = user.getUserId();
        return userStyleService.getNoteByUid(uid);
    }


    /**
     * 用户界面风格的存储
     *
     * @param theme
     * @return
     */
    @RequestMapping("/addUserStyle")
    @UserLog(value = "'用户修改界面'", dmlType = DMLType.UPDATE)
    public boolean addUserStyle(String theme) {
        AuthUser user = CurrentUserHolder.getUser();
        Integer uid = user.getUserId();
        Boolean b = userStyleService.addUserStyle(uid, theme);

        return b;

    }

    /**
     * 读取用户的界面风格
     *
     * @return
     */
    @RequestMapping("/setUserStyle")
    public UserStyle setUserStyle() {
        AuthUser user = CurrentUserHolder.getUser();
        Integer uid = user.getUserId();
        return userStyleService.getNoteByUid(uid);
    }

}
