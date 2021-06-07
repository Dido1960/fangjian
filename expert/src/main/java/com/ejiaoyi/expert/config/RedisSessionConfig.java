package com.ejiaoyi.expert.config;

import com.ejiaoyi.common.locker.RedissonDistributeLocker;
import com.ejiaoyi.common.util.RedissonUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * spring session 配置
 *
 * @author Z0001
 * @since 2020-03-17
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 3600,redisNamespace = "bid_con_expert_session")
public class RedisSessionConfig {

    @Autowired
    private Environment env;
    @Bean
    public DefaultCookieSerializer defaultCookieSerializer() {
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        cookieSerializer.setUseBase64Encoding(false);
        cookieSerializer.setCookieName("NORMAL_BID_CON_EXPERT_COOKIE");
        return cookieSerializer;
    }


    /**
     * Redisson客户端注册
     * 单机模式
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient createRedissonClient() {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress("redis://" + env.getProperty("spring.redis.host") + ":" + env.getProperty("spring.redis.port"));
        singleServerConfig.setTimeout(Integer.valueOf(env.getProperty("spring.redis.timeout")));
         singleServerConfig.setPassword(env.getProperty("spring.redis.password"));
        singleServerConfig.setDatabase(Integer.valueOf(env.getProperty("spring.redis.database")));
        return Redisson.create(config);
    }

    /**
     * 分布式锁实例化并交给工具类
     * @param redissonClient
     */
    @Bean
    public RedissonDistributeLocker redissonLocker(@Qualifier("createRedissonClient") RedissonClient redissonClient) {
        RedissonDistributeLocker locker = new RedissonDistributeLocker(redissonClient);
        RedissonUtil.setLocker(locker);
        return locker;
    }
}
