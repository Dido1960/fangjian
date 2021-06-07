package com.ejiaoyi.common.config;

import com.ejiaoyi.common.session.OnlineInfoSessionListener;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpSessionListener;

@Configuration
public class SessionCofig {

    @Bean
    public ServletListenerRegistrationBean<HttpSessionListener> sessionListenerWithMetrics() {
        ServletListenerRegistrationBean<HttpSessionListener> listenerRegBean = new ServletListenerRegistrationBean<>();
        listenerRegBean.setListener(new OnlineInfoSessionListener());
        return listenerRegBean;
    }

}
