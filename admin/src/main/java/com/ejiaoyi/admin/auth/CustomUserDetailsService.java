package com.ejiaoyi.admin.auth;

import com.alibaba.fastjson.JSONObject;
import com.ejiaoyi.admin.service.impl.MenuServiceImpl;
import com.ejiaoyi.admin.service.impl.RoleUserServiceImpl;
import com.ejiaoyi.admin.service.impl.UserInfoServiceImpl;
import com.ejiaoyi.admin.support.AuthUser;
import com.ejiaoyi.common.crypto.SM2Util;
import com.ejiaoyi.common.entity.UserInfo;
import com.ejiaoyi.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 用户认证规则
 *
 * @author Z0001
 * @since 2020-03-17
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserInfoServiceImpl userInfoService;

    @Autowired
    RoleUserServiceImpl roleUserService;

    @Autowired
    MenuServiceImpl menuService;


    @Override
    public UserDetails loadUserByUsername(String param) throws AuthenticationException {
        // 处理由UserAuthenticationFilter预处理的参数
        JSONObject json = JSONObject.parseObject(param);
        String loginName = json.getString("username");
        String password = json.getString("password");
        String auth = json.getString("auth");
        String loginType = json.getString("loginType");
        String casn = json.getString("casn");
        UserInfo user = null;
        // ukey序列号
        String ukey_serial_number = json.getString("ukey_serial_number");

        String pwd = null;
        // 用户名、密码登录
        if (!CommonUtil.isEmpty(loginName) && CommonUtil.isEmpty(ukey_serial_number)) {
            user = userInfoService.userAuth(loginName);
            if (user == null) {
                throw new UsernameNotFoundException("用户名或密码错误，请重新输入!");
            } else {
                pwd = user.getPassword();
            }
        } else if (!CommonUtil.isEmpty(ukey_serial_number)) {
            user = userInfoService.userCasn(casn);
            if (user == null) {
                throw new BadCredentialsException("该CA并未绑定用户");
            }
            pwd = user.getPassword();
        } else {
            throw new UsernameNotFoundException("用户名或密码错误，请重新输入!");
        }

        return AuthUser.builder()
                .userId(user.getId())
                .name(user.getName())
                .loginName(loginName)
                .password(pwd)
                .regId(user.getRegId())
                .userRoleStatus(user.getUserRoleStatus())
                .enabled(user.getEnabled())
                .build();
    }
}
