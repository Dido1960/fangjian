package com.ejiaoyi.bidder.controller;

import com.alibaba.fastjson.JSONObject;
import com.ejiaoyi.bidder.sso.SsoLogin;
import com.ejiaoyi.bidder.support.AuthUser;
import com.ejiaoyi.bidder.support.CurrentUserHolder;
import com.ejiaoyi.common.entity.CompanyUser;
import com.ejiaoyi.common.enums.SysUserType;
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
     * 项目起始页面
     */
    @RequestMapping("/")
    public ModelAndView initPage() {
        return new ModelAndView("redirect:/login.html");
    }

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
    @RequestMapping("/login/getUser")
    public AuthUser getUser() {
        return CurrentUserHolder.getUser();
    }

    /**
     * 其他CA通过证书序列号认证是否互认绑定
     * @param certSerialNumber 证书序列号
     * @param password 密码
     * @return 是否合法
     */
    @RequestMapping("/login/otherCaCert")
    public Boolean otherCaCert(String certSerialNumber, String password) {
        // 从企业运行平台获取其他家锁认证信息
        CompanyUser user = JSONObject.parseObject(JSONObject.toJSONString(new SsoLogin().getOtherCertBindUser(certSerialNumber, SysUserType.COMPANYUSER.getType())), CompanyUser.class);
        return user != null;
    }
}
