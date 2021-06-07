package com.ejiaoyi.admin.controller;

import com.ejiaoyi.common.entity.UserReg;
import com.ejiaoyi.common.service.IUserRegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 用户区划关联表 前端控制器
 * </p>
 *
 * @author langwei
 * @since 2020-08-05
 */
@RestController
@RequestMapping("/userReg")
public class UserRegController {


    @Autowired
    private IUserRegService userRegService;


    @RequestMapping("/listUserRegByUserId")
    public List<UserReg> listUserRegByUserId(Integer userId){
        return userRegService.listUserRegByUserId(userId);
    }

    @RequestMapping("/addUserRegList")
    public void addUserRegList(HttpServletRequest request){
        String userId = request.getParameter("userId");
        String[] userRegs = request.getParameterValues("userRegs");
        userRegService.addUserRegList(userRegs,userId);
    }



}
