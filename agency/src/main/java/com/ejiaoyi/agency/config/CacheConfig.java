package com.ejiaoyi.agency.config;

import com.ejiaoyi.common.constant.CacheName;
import com.ejiaoyi.common.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * spring 缓存机制配置类
 *
 * @author Z0001
 * @since 2020-4-21
 */
@Configuration
public class CacheConfig {


    @Autowired
    private RedisTemplate redisTemplate;

    @Bean
    public RedisCacheWriter writer() {
        return RedisCacheWriter.nonLockingRedisCacheWriter(redisTemplate.getConnectionFactory());
    }

    /***
     * 缓存时间不得超过${Constants.CACHE_NAME_TIME}天
     * **/
    @Bean
    public CacheManager cacheManager() throws IllegalAccessException {
        Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>();
        Field[] fields = CacheName.class.getFields();

        for (Field field : fields) {
            field.setAccessible(true);
            configurationMap.put(String.valueOf(field.get(null)), RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofDays(Constants.CACHE_NAME_TIME)));
        }

        return RedisCacheManager.builder(writer())
                .initialCacheNames(configurationMap.keySet())
                .withInitialCacheConfigurations(configurationMap)
                .build();
    }

}