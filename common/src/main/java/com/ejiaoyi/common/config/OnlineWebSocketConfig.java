package com.ejiaoyi.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 不见面开标WebSocket配置类
 *
 * @author Make
 * @since 2020-08-06
 */
@Configuration
@ConditionalOnWebApplication
public class OnlineWebSocketConfig {

    //使用boot内置tomcat时需要注入此bean
    /*@Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }*/

    @Bean
    public CustomSpringConfigurator customSpringConfigurator() {
        return new CustomSpringConfigurator();
    }
}