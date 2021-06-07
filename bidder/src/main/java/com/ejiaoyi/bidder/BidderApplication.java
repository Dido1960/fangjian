package com.ejiaoyi.bidder;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * core程序启动入口
 *
 * @author Z0001
 * @since 2020-03-18
 */
@EnableAsync
@EnableCaching
@ComponentScan("com.ejiaoyi")
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
public class BidderApplication extends WebMvcConfigurationSupport {

    public static void main(String[] args) {
        SpringApplication.run(BidderApplication.class, args);
    }
}
