package com.ejiaoyi.worker.auth;

import com.alibaba.fastjson.JSONObject;
import com.ejiaoyi.common.crypto.SM2Util;
import com.ejiaoyi.common.entity.GovUser;
import com.ejiaoyi.common.enums.GovDepType;
import com.ejiaoyi.common.service.IGovUserService;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.worker.support.AuthUser;
import lombok.SneakyThrows;
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
    IGovUserService govUserService;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String param) throws AuthenticationException {
        // 处理由UserAuthenticationFilter预处理的参数
        JSONObject json = JSONObject.parseObject(param);
        String loginName = json.getString("username");
        String password = json.getString("password");
        String auth = json.getString("auth");
        String loginType = json.getString("loginType");
        String casn = json.getString("casn");
        // ukey序列号
        String ukey_serial_number = json.getString("ukey_serial_number");
        GovUser user = null;

        String pwd = null;
        //账号登录
        if (!CommonUtil.isEmpty(loginName))  {
            user = govUserService.getGovUserByLoginName(loginName, GovDepType.WORKER_EXTRACT.getType());
            if (user == null) {
                throw new UsernameNotFoundException("用户名或密码错误，请重新输入!");
            } else {
                pwd = user.getPassword();
            }
        }
        //CA登录
        if (!CommonUtil.isEmpty(ukey_serial_number)) {
            user = govUserService.getGovUserByCasn(ukey_serial_number, GovDepType.WORKER_EXTRACT.getType());
            if (user == null) {
                throw new BadCredentialsException("该CA并未绑定用户");
            }
            pwd = SM2Util.encrypt(password);
        }

        if (user == null) {
            throw new BadCredentialsException("用户名或密码错误，请重新输入!");
        }

        return AuthUser.builder()
                .userId(user.getId())
                .name(user.getName())
                .loginName(loginName)
                .dep(user.getDep())
                .regId(user.getDep().getRegId())
                .password(pwd)
                .enabled(user.getEnabled())
                .build();
    }
}
