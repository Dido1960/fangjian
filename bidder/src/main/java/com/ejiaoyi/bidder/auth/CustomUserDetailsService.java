package com.ejiaoyi.bidder.auth;

import com.alibaba.fastjson.JSONObject;
import com.ejiaoyi.bidder.sso.SsoLogin;
import com.ejiaoyi.bidder.support.AuthUser;
import com.ejiaoyi.common.crypto.SM2PasswordEncoder;
import com.ejiaoyi.common.crypto.SM2Util;
import com.ejiaoyi.common.entity.CompanyUser;
import com.ejiaoyi.common.enums.SysUserType;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.RedisUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 用户认证规则
 *
 * @author Z0001
 * @since 2020-03-17
 */
public class CustomUserDetailsService implements UserDetailsService {

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String param) throws AuthenticationException {
        // 处理由UserAuthenticationFilter预处理的参数
        JSONObject json = JSONObject.parseObject(param);
        String loginName = json.getString("username");
        String password = json.getString("password");
        String qrCodeKey = json.getString("qrCodeKey");
        // 证书序列号
        String cert_serial_number = json.getString("cert_serial_number");
        // ukey序列号
        String ukey_serial_number = json.getString("ukey_serial_number");
        String pwd;
        CompanyUser user;

        //游客登录
        String visitorLogin = json.getString("visitorLogin");
        //验证码
        String vrifyCode = json.getString("vrifyCode");
        if (!CommonUtil.isEmpty(visitorLogin)) {
            //伪造id,密码
            Integer flag = RandomUtils.nextInt(0, 10000) + Integer.parseInt(vrifyCode);
            SM2PasswordEncoder sm2PasswordEncoder = new SM2PasswordEncoder();
            String visitorPwd = sm2PasswordEncoder.encode(vrifyCode);
            return AuthUser.builder()
                    .userId(flag)
                    .name("游客_" + flag)
                    .loginName("游客_" + flag)
                    .password(visitorPwd)
                    .code("游客_" + flag)
                    .enabled(1)
                    .build();
        }

        // 用户名、密码登录
        if (!CommonUtil.isEmpty(loginName)
                && CommonUtil.isEmpty(ukey_serial_number)
                && CommonUtil.isEmpty(cert_serial_number)
                && CommonUtil.isEmpty(qrCodeKey)) {
            // 单点登录
            user = JSONObject.parseObject(JSONObject.toJSONString(new SsoLogin().getUser(loginName, password, SysUserType.COMPANYUSER.getType())), CompanyUser.class);

            if (user == null) {
                throw new BadCredentialsException("用户名或密码错误，请重新输入!");
            } else {
                pwd = user.getPassword();
            }
        } else if (!CommonUtil.isEmpty(ukey_serial_number)) {
            // ukey登录
            try {
                // 单点登录
                user = JSONObject.parseObject(JSONObject.toJSONString(new SsoLogin().getCertBindUser(ukey_serial_number, SysUserType.COMPANYUSER.getType())), CompanyUser.class);
                if (CommonUtil.isEmpty(user)){
                    throw new BadCredentialsException("当前CA未绑定用户!");
                } else {
                    try {
                        pwd = SM2Util.encrypt(password);
                        user.setPassword(pwd);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new UsernameNotFoundException("CA解密异常！");
                    }
                }
            } catch (Exception e) {
                throw new BadCredentialsException("网络错误，请稍后再试!");
            }
        } else if (!CommonUtil.isEmpty(cert_serial_number)) {
            // 证书登录
            user = JSONObject.parseObject(JSONObject.toJSONString(new SsoLogin().getOtherCertBindUser(cert_serial_number, SysUserType.COMPANYUSER.getType())), CompanyUser.class);
            if (user == null) {
                throw new BadCredentialsException("未匹配到相应的企业!");
            } else {
                try {
                    pwd = SM2Util.encrypt(password);
                    user.setPassword(pwd);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new UsernameNotFoundException("CA解密异常！");
                }
            }
        } else if (!CommonUtil.isEmpty(qrCodeKey)) {
            // 手机扫码登录
            user = (CompanyUser) RedisUtil.get(qrCodeKey);
            if (user == null) {
                throw new BadCredentialsException("未匹配到相应的企业!");
            } else {
                try {
                    pwd = SM2Util.encrypt(password);
                    user.setPassword(pwd);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new UsernameNotFoundException("CA解密异常！");
                }
            }
        } else {
            throw new UsernameNotFoundException("用户名或密码错误，请重新输入!");
        }

        return AuthUser.builder()
                .userId(user.getId())
                .name(user.getName())
                .loginName(loginName)
                .password(pwd)
                .code(user.getCode())
                .enabled(user.getEnabled())
                .build();

    }

}
