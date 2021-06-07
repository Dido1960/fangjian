package com.ejiaoyi.expert.auth;

import com.alibaba.fastjson.JSONObject;
import com.ejiaoyi.common.constant.ExpertStatus;
import com.ejiaoyi.common.crypto.SM2Util;
import com.ejiaoyi.common.entity.ExpertUser;
import com.ejiaoyi.common.service.IExpertUserService;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.expert.support.AuthUser;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户认证规则
 *
 * @author Z0001
 * @since 2020-03-17
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    IExpertUserService expertUserService;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String param) throws AuthenticationException {
        // 处理由UserAuthenticationFilter预处理的参数
        JSONObject json = JSONObject.parseObject(param);
        String loginName = json.getString("username");
        String password = json.getString("password");
        String userId = json.getString("userid");
        String casn = json.getString("casn");
        ExpertUser user = null;
        password = SM2Util.encrypt(password);
        Map<Integer, Integer> sectionIdAndUserId = new HashMap<>();
        if (!CommonUtil.isEmpty(loginName)) {
            if (CommonUtil.isEmpty(userId)) {
                List<ExpertUser> users = expertUserService.getExpertUser(loginName, password);
                if (users != null && users.size() > 0) {
                    user = users.get(0);
                    for (ExpertUser expertUser : users) {
                        sectionIdAndUserId.put(expertUser.getBidSectionId(), expertUser.getId());
                    }
                }
            } else {
                user = expertUserService.getExpertUserById(Integer.valueOf(userId));
            }
            if (user == null) {
                throw new BadCredentialsException("用户名或密码错误，请重新输入!");
            }
        }

        if (!CommonUtil.isEmpty(casn)) {
            if (user == null) {
                throw new BadCredentialsException("该CA并未绑定用户");
            }
        }

        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误，请重新输入!");
        }

        //专家回避后登录
        if (ExpertStatus.AVOIDED.equals(user.getAvoid()) && sectionIdAndUserId.size() == 1) {
            throw new BadCredentialsException("你已回避，登录失败!");
        }

        return AuthUser.builder()
                .userId(user.getId())
                .loginName(user.getExpertName())
                .password(password)
                .name(user.getExpertName())
                .bidSectionId(user.getBidSectionId())
                .isChairman(user.getIsChairman())
                .sectionIdAndUserId(sectionIdAndUserId)
                .regId(user.getRegId())
                .enabled(user.getEnabled())
                .build();
    }
}
