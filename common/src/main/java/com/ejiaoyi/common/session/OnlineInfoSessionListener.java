package com.ejiaoyi.common.session;

import cn.hutool.core.bean.BeanUtil;
import com.ejiaoyi.common.entity.OnlineInfo;
import com.ejiaoyi.common.service.IOnlineInfoService;
import com.ejiaoyi.common.util.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lesgod
 * @date 2019/2/15 14:44
 */
@Configuration
@WebListener
@Slf4j
public class OnlineInfoSessionListener implements HttpSessionListener  {

    public static AtomicInteger userCount = new AtomicInteger(0);

    private IOnlineInfoService onlineInfoService;



    /**
     * 用户登录，创建session，用户数增加
     *
     * @param event
     */
    @Override
    public void sessionCreated(HttpSessionEvent event) {

        userCount.getAndIncrement();
        if(onlineInfoService==null){
            onlineInfoService = ApplicationContextUtil.getBean(IOnlineInfoService.class);
        }
        // 当前用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication!=null&&authentication.getPrincipal()!=null) {
            Map map = BeanUtil.beanToMap(authentication.getPrincipal());

            OnlineInfo onlineInfo = OnlineInfo.builder()
                    .sessionId(event.getSession().getId())
                    .userId(String.valueOf(map.get("userId")))
                    .name(String.valueOf(map.get("name")))
                    .module(authentication.getPrincipal().getClass().getName()
                            .replaceAll("com","")
                            .replaceAll("ejiaoyi","")
                            .replaceAll("support","")
                            .replaceAll(".","")
                            .replaceAll("AuthUser",""))

                    .build();
            onlineInfoService.saveOrUpdateOnline(onlineInfo);
        }else {
            OnlineInfo onlineInfo = OnlineInfo.builder()
                    .sessionId(event.getSession().getId())
                    .name("游客_"+(userCount.get()+1))
                    .build();
            onlineInfoService.saveOrUpdateOnline(onlineInfo);
        }
        log.info("creatSessionId:"+event.getSession().getId());

    }


    /**
     * 用户下线，销毁session，用户数减少
     *
     * @param event
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        if(onlineInfoService==null){
            onlineInfoService = ApplicationContextUtil.getBean(IOnlineInfoService.class);
        }
        event.getSession().getId();
        userCount.getAndDecrement();
        onlineInfoService.removeBySessionId(event.getSession().getId());
        log.info("sessionDestroyed:"+event.getSession().getId());
    }
}