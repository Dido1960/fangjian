package com.ejiaoyi.worker;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * worker程序启动入口
 *
 * @author Z0001
 * @since 2020-08-05
 */
@EnableAsync
@EnableCaching
@ComponentScan("com.ejiaoyi")
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
public class WorkerApplication extends WebMvcConfigurationSupport {
    public static void main(String[] args) {
        SpringApplication.run(WorkerApplication.class, args);
    }
}
