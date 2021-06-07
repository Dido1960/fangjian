package com.ejiaoyi.supervise.controller;

import com.ejiaoyi.common.entity.Dep;
import com.ejiaoyi.common.entity.GovUser;
import com.ejiaoyi.common.service.IDepService;
import com.ejiaoyi.common.service.IGovUserService;
import com.ejiaoyi.supervise.support.AuthUser;
import com.ejiaoyi.supervise.support.CurrentUserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录控制器
 *
 * @author Z0001
 * @since 2020/3/26
 */
@RestController
public class LoginController extends BaseController {

    @Autowired
    IGovUserService govUserService;

    @Autowired
    IDepService depService;


    /**
     * 登录页面
     *
     * @return
     */
    @RequestMapping("/login.html")
    public ModelAndView loginPage() {
        return new ModelAndView("/login");
    }

    /**
     * 系统首页
     *
     * @param request 网络请求信息
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView indexPage(HttpServletRequest request) {
        AuthUser user = CurrentUserHolder.getUser();
        ModelAndView mv = new ModelAndView("/index");
        GovUser govUser = govUserService.getGovUserById(user.getUserId());
        Dep dep = depService.getDepById(govUser.getDepId());
        mv.addObject("user", user);
        mv.addObject("dep", dep);
        return mv;
    }

    /**
     * 系统首页
     *
     * @return
     */
    @RequestMapping("/getUser")
    public Dep getUser() {
        AuthUser user = CurrentUserHolder.getUser();
        GovUser govUser = govUserService.getGovUserById(user.getUserId());
        return depService.getDepById(govUser.getDepId());
    }


}
