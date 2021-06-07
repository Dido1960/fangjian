package com.ejiaoyi.supervise.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;

/**
 * spring security 错误信息本地化配置文件
 *
 * @author Z0001
 * @since 2020-5-7
 */
@Configuration
public class SecurityMessageLocal {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.toString());
        messageSource.addBasenames("classpath:messages_zh_cn");
        return messageSource;
    }
}
