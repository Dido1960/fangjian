package com.ejiaoyi.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * spring mvc 配置
 *
 * @author Z0001
 * @since 2020-03-17
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    public static String[] STATIC_PATH = new String[]{
            "/css/**", "/img/**", "/js/**","/dist/**"
            , "/logo/**", "/layuiAdmin/**","/errorPages/**"
            , "/kaptcha/**","/aliPay/certOrderAlipayNotify"
            ,"/home.html","/bidConRolePage","/noTemplateRolePage",
            "/govRolePage","/trafficRolePage","/waterRolePage","/paperRolePage","/pages"
    };

    @Override
    protected void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("/index");
        super.addViewControllers(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);
    }
}
