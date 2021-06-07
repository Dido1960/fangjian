package com.ejiaoyi.api;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * api程序启动入口
 *
 * @author Z0001
 * @since 2020-03-17
 */
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@ComponentScan("com.ejiaoyi")
public class ApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
