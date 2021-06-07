package com.ejiaoyi.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.StandardCharsets;

/**
 * 自定义 FastJson 反序列化器
 *
 * @param <T>
 * @author Z0001
 * @since 2020-03-26
 */
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {

    private Class<T> clazz;

    public FastJson2JsonRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    // 设置反序列化白名单 防止JSONException 设置需要的entity DTO 等实体类相关
    static {
        ParserConfig.getGlobalInstance().addAccept("com.ejiaoyi.common.dto");
        ParserConfig.getGlobalInstance().addAccept("com.ejiaoyi.common.entity");
    }

    @Override
    public byte[] serialize(T o) throws SerializationException {
        if (o == null) {
            return new byte[0];
        }
        return JSON.toJSONString(o, SerializerFeature.WriteClassName).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        String str = new String(bytes, StandardCharsets.UTF_8);
        // 打开autotype功能
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        return (T) JSON.parseObject(str, clazz);
    }
}