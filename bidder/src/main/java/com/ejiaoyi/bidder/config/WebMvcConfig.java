package com.ejiaoyi.bidder.config;

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
            "/css/**", "/img/**", "/js/**", "/logo/**",
            "/phoneScan/**","/xin/**",
            "/layuiAdmin/**", "/plugin/**",
            "/errorPages/**", "/kaptcha/**",
            "/aliPay/certOrderAlipayNotify",
            "/visitor/noFaceIndex.html",
            "/visitor/noLoginPage.html",
            "/common/data/decrypt",
            "/visitor/noLoginPage.html", "/",
            "/dist/**","/pages","/mobilePhoneScanCode/**",
            "/home.html","/bidConRolePage","/noTemplateRolePage",
            "/govRolePage","/trafficRolePage","/waterRolePage","/paperRolePage"
    };

    @Override
    protected void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("/bidOpeningHall/noFaceIndex");
        super.addViewControllers(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);
    }
}
