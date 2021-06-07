package com.ejiaoyi.admin.controller;

import com.ejiaoyi.admin.service.impl.MenuServiceImpl;
import com.ejiaoyi.admin.support.AuthUser;
import com.ejiaoyi.admin.support.CurrentUserHolder;
import com.ejiaoyi.common.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录控制器
 *
 * @author Z0001
 * @since 2020/3/26
 */
@RestController
public class LoginController extends BaseController {

    @Autowired
    MenuServiceImpl menuService;

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
     * 退出
     *
     * @return
     */
    @RequestMapping("/logout")
    public boolean logout(HttpSession httpSession) {
        httpSession.invalidate();
        return true;
    }

    @RequestMapping("/listLeftMenu")
    public Map<String, Object> listLeftMenu(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        AuthUser user = CurrentUserHolder.getUser();
        String ip = HttpUtil.getIP(request);
        boolean isLocalhost = false;
        // 判断ip是否为本机或局域网
        if (("0:0:0:0:0:0:0:1".equals(ip) || "127.0.0.1".equals(ip))) {
            isLocalhost = true;
        }

        map.put("localhostIP", isLocalhost);
        map.put("user", user);
        map.put("menuList", menuService.listMenuByUserId(user.getUserId()));

        return map;
    }

}
