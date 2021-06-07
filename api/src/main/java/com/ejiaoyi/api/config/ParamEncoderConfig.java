package com.ejiaoyi.api.config;

import com.ejiaoyi.api.filter.DataFilter;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源及事务配置
 *
 * @author unknownChivalrous
 * @since 2020-03-17
 */
@Configuration
public class ParamEncoderConfig {

    @Bean
    DataFilter getParamEncoderFilter1(){
        return new DataFilter();
    }

    @Bean("myFilter1RegistrationBeanName")
    public FilterRegistrationBean<DataFilter> registerFilter1(DataFilter myFilter1) {
        FilterRegistrationBean<DataFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(myFilter1);
        //url拦截
        registrationBean.addUrlPatterns("/v2/servicePlatformDock/*");
        registrationBean.setOrder(1);
        registrationBean.setAsyncSupported(true);
        return registrationBean;
    }


}
