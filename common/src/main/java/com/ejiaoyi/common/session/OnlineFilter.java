package com.ejiaoyi.common.session;

import cn.hutool.core.bean.BeanUtil;
import com.ejiaoyi.common.entity.OnlineInfo;
import com.ejiaoyi.common.service.IOnlineInfoService;
import com.ejiaoyi.common.util.ApplicationContextUtil;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;


/**
 * xss 拦截器
 *
 * @author lesgod
 * @return
 * @date 2020-7-7 9:32
 */
public class OnlineFilter implements Filter {


    @Autowired
    private IOnlineInfoService onlineInfoService;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
        String sessionId = ((HttpServletRequest) request).getRequestedSessionId();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            System.out.println(authentication.getPrincipal().getClass().getName());
            Map map = BeanUtil.beanToMap(authentication.getPrincipal());
            if (onlineInfoService == null) {
                onlineInfoService = ApplicationContextUtil.getBean(IOnlineInfoService.class);
            }
            OnlineInfo onlineInfo = OnlineInfo.builder()
                    .sessionId(sessionId)
                    .module(authentication.getPrincipal().getClass().getName().replace("com.ejiaoyi.",""))
                    .build();
            if (!CommonUtil.isEmpty(map.get("name"))) {
                onlineInfo.setUserId(String.valueOf(map.get("userId")));
                onlineInfo.setName(String.valueOf(map.get("name")));

            }
            onlineInfo.setIpInfo(HttpUtil.getIP((HttpServletRequest) request));
            onlineInfoService.saveOrUpdateOnline(onlineInfo);
        }
    }

}
