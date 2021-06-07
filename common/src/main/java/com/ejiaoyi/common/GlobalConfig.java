package com.ejiaoyi.common;

import freemarker.template.Configuration;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import kr.pe.kwonnam.freemarker.inheritance.BlockDirective;
import kr.pe.kwonnam.freemarker.inheritance.ExtendsDirective;
import kr.pe.kwonnam.freemarker.inheritance.PutDirective;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局配置
 * 用于进行系统级全局通用型配置
 *
 * @author Z0001
 * @since 2020-03-17
 */
public class GlobalConfig {

    public static RedisTemplate<String, Object> redisTemplateBean(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        FastJson2JsonRedisSerializer fastJson2JsonRedisSerializer = new FastJson2JsonRedisSerializer(Object.class);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用fastJson
        template.setValueSerializer(fastJson2JsonRedisSerializer);
        // hash的value序列化方式采用fastJson
        template.setHashValueSerializer(fastJson2JsonRedisSerializer);
        template.afterPropertiesSet();

        return template;
    }

    public static Configuration configureFreemarkerLayoutDirectives(Configuration configuration) throws TemplateModelException {
        //设置母版布局指令
        Map<String, TemplateModel> freemarkerLayoutDirectives = new HashMap<>(3);
        freemarkerLayoutDirectives.put("extends", new ExtendsDirective());
        freemarkerLayoutDirectives.put("block", new BlockDirective());
        freemarkerLayoutDirectives.put("put", new PutDirective());
        configuration.setSharedVariable("layout", freemarkerLayoutDirectives);
        return configuration;
    }

}
