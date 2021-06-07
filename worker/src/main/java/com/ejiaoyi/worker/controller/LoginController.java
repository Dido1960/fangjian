package com.ejiaoyi.worker.controller;

import com.ejiaoyi.worker.support.AuthUser;
import com.ejiaoyi.worker.support.CurrentUserHolder;
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
        mv.addObject("user", user);
        return mv;
    }

    /**
     * 获取当前用户信息
     *
     * @param
     * @return
     */
    @RequestMapping("/getUser")
    public AuthUser getUser() {
        AuthUser user = CurrentUserHolder.getUser();
        return user;
    }
}
