package com.ejiaoyi.api;

import com.ejiaoyi.common.util.FileUtil;
import com.ejiaoyi.common.util.RedisUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 项目启动任务
 *
 * @author Z0001
 * @since 2020-5-9
 */
@Component
public class StartRunner implements CommandLineRunner {

    @Value("${spring.cache.redis.key-prefix}")
    private String CACHE_PREFIX;

    @Override
    public void run(String... args) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> start runner >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        // 系统启动逻辑
        // 设置项目路劲
        FileUtil.setRootPath(System.getProperty("user.dir"));
        RedisConnection connection = RedisUtil.redisTemplate.getConnectionFactory().getConnection();

        Set<byte[]> caches = connection.keys(CACHE_PREFIX.getBytes());
        if (CollectionUtils.isNotEmpty(caches)) {
            connection.del(caches.toArray(new byte[][]{}));
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> end start runner >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }
}