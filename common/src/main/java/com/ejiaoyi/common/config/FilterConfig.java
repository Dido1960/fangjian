package com.ejiaoyi.common.config;

import com.ejiaoyi.common.session.OnlineFilter;
import com.ejiaoyi.common.xss.XssFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;


/**
 * 过滤器配置文件
 *
 * @author Xie
 * @date 2021-01-19
 ***/
@Configuration
public class FilterConfig {

    /***
     * session
     * ***/
    @Bean("sessionCofigOnlineInfo")
    public FilterRegistrationBean<OnlineFilter> registerFilter1(OnlineFilter myFilter1) {
        FilterRegistrationBean<OnlineFilter> registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(myFilter1);
        //url拦截
        registrationBean.addUrlPatterns("/login/*", "/index/*");
        registrationBean.setOrder(2);
        registrationBean.setAsyncSupported(true);
        return registrationBean;
    }

    @Bean
    OnlineFilter getApiAuthenticationFilter1() {
        return new OnlineFilter();
    }

    /****
     * 跨域配置 转义参数
     *
     * ***/
    @Bean(name = "xssFilter")
    public XssFilter xssFilter() {
        return new XssFilter();
    }

    @Bean
    public FilterRegistrationBean registerFilter1() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new DelegatingFilterProxy("xssFilter"));
        //url拦截 所有的参数
        registrationBean.addUrlPatterns("/*");
        registrationBean.addInitParameter("targetFilterLifecycle", "true");
        registrationBean.setDispatcherTypes(DispatcherType.REQUEST);
        registrationBean.setOrder(1);
        registrationBean.setName("xssFilter");
        registrationBean.setAsyncSupported(true);
        return registrationBean;
    }

}
