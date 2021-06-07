package com.ejiaoyi.api.config;

import com.ejiaoyi.common.GlobalConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis 配置
 *
 * @author unknownChivalrous
 * @since 2020/3/26
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(@Qualifier("redissonConnectionFactory") RedisConnectionFactory factory) {
        return GlobalConfig.redisTemplateBean(factory);
    }
}
