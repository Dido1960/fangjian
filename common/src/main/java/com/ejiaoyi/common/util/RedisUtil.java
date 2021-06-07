package com.ejiaoyi.common.util;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redis 操作工具类
 *
 * @author Z0001
 * @since 2020-03-18
 */
@Component
public class RedisUtil {
    // 默认失效时间24小时
    private static long DEFAULT_TIME_OUT = 24*60*60;
    public static RedisTemplate<String, Object> redisTemplate;

    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
    }

    /**************************** common part start ****************************************/

    /**
     * 设置失效时间
     *
     * @param key     键
     * @param timeout 失效时间 seconds
     */
    public static void expire(String key, long timeout) {
        if (timeout > 0) {
            redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        }
    }

    /**
     * 设置失效日期
     *
     * @param key  键
     * @param date 失效日期
     */
    public static void expireAt(String key, Date date) {
        redisTemplate.expireAt(key, date);
    }

    /**
     * 根据key获取失效时间
     *
     * @param key 键
     * @return 失效时间 seconds 0: 永久有效
     */
    public static Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key对应的数据是否存在
     *
     * @param key 键
     * @return 数据是否存在
     */
    public static Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 根据 key值集合 删除 redis
     *
     * @param keys 键，可以传一个或多个
     */
    public static void deleteKeys(String... keys) {
        if (CollectionUtils.isNotEmpty(Arrays.asList(keys))) {
            redisTemplate.delete(Arrays.asList(keys));
        }
    }
    /**
     * 根据 key值集合 删除 redis
     *
     * @param key ，
     */
    public static void delete(String key){
        if(redisTemplate.hasKey(key)){
            redisTemplate.delete(key);
        }
    }

    /**************************** common part end ****************************************/

    /**************************** string part start ****************************************/

    /**
     * 根据 key 获取 redis
     *
     * @param key 键
     * @return 值
     */
    public static Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 写入redis
     *
     * @param key   键
     * @param value 值
     */
    public static void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
        expire(key, DEFAULT_TIME_OUT);
    }

    /**
     * 存入redis 并设置失效时间
     *
     * @param key     键
     * @param value   值
     * @param timeout 失效时间 seconds timeout < 0 : 永久有效
     */
    public static void set(String key, Object value, long timeout) {
        if (timeout > 0) {
            redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
        } else {
            RedisUtil.set(key, value);
        }
    }

    /**
     * long类型递增
     *
     * @param key      键
     * @param increase 递增因子(大于0)
     * @return 递增后的值
     */
    public static Long increment(String key, long increase) {
        if (increase <= 0) {
            throw new IllegalArgumentException("param increase must > 0");
        }
        return redisTemplate.opsForValue().increment(key, increase);
    }

    /**
     * double类型递增
     *
     * @param key      键
     * @param increase 递增因子(大于0)
     * @return 递增后的值
     */
    public static Double increment(String key, double increase) {
        if (increase <= 0) {
            throw new IllegalArgumentException("param increase must > 0");
        }
        return redisTemplate.opsForValue().increment(key, increase);
    }

    /**
     * long类型递减
     *
     * @param key      键
     * @param decrease 递减因子(大于0)
     * @return 递减后的值
     */
    public static Long decrement(String key, long decrease) {
        if (decrease <= 0) {
            throw new IllegalArgumentException("param decrease must > 0");
        }
        return redisTemplate.opsForValue().increment(key, -decrease);
    }

    /**
     * double类型递减
     *
     * @param key      键
     * @param decrease 递减因子(大于0)
     * @return 递减后的值
     */
    public static Double decrement(String key, double decrease) {
        if (decrease <= 0) {
            throw new IllegalArgumentException("param decrease must > 0");
        }
        return redisTemplate.opsForValue().increment(key, -decrease);
    }

    /**************************** string part end ****************************************/

    /**************************** hashMap part start ****************************************/

    /**
     * 根据key以及hashKey获取map集合的value
     *
     * @param key     键
     * @param hashKey map集合的key
     * @return map集合的value
     */
    public static Object hashGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 写入map集合数据项 如果map集合不存在则创建
     *
     * @param key     键
     * @param hashKey map集合的key
     * @param value   map集合的value
     */
    public static void hashSet(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 写入map集合数据项 如果map集合不存在则创建 并设置失效时间
     *
     * @param key     键
     * @param hashKey map集合的key
     * @param value   map集合的value
     * @param timeout 失效时间 seconds timeout < 0 : 永久有效
     */
    public static void hashSet(String key, String hashKey, Object value, long timeout) {
        redisTemplate.opsForHash().put(key, hashKey, value);
        if (timeout > 0) {
            RedisUtil.expire(key, timeout);
        }
    }

    /**
     * 根据key获取map集合
     *
     * @param key 键
     * @return map集合
     */
    public static Map<Object, Object> hashMapGet(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 写入map集合
     *
     * @param key 键
     * @param map map集合
     */
    public static void hashMapSet(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 写入 map集合 并设置失效时间
     *
     * @param key     键
     * @param map     map集合
     * @param timeout 失效时间 seconds timeout < 0 : 永久有效
     */
    public static void hashMapSet(String key, Map<String, Object> map, long timeout) {
        redisTemplate.opsForHash().putAll(key, map);
        if (timeout > 0) {
            RedisUtil.expire(key, timeout);
        }
    }

    /**
     * 删除map集合中的值
     *
     * @param key     键
     * @param hashKey map集合的key
     */
    public static void hashRemove(String key, Object... hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    /**
     * 获取map集合大小
     *
     * @param key 键
     * @return map集合大小
     */
    public static Long hashGetHashSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    /**
     * 判断map集合中是否有hashKey所对应的值
     *
     * @param key     键
     * @param hashKey map集合的key
     * @return hashKey所对应的值是否存在
     */
    public static Boolean hashHasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * long类型hash递增
     *
     * @param key      键
     * @param hashKey  map集合的key
     * @param increase 递增因子(大于0)
     * @return 递增后的值
     */
    public static Long hashIncrement(String key, String hashKey, long increase) {
        if (increase <= 0) {
            throw new IllegalArgumentException("param increase must > 0");
        }
        return redisTemplate.opsForHash().increment(key, hashKey, increase);
    }


    /**
     * double类型hash递增
     *
     * @param key      键
     * @param hashKey  map集合的key
     * @param increase 递增因子(大于0)
     * @return 递增后的值
     */
    public static Double hashIncrement(String key, String hashKey, double increase) {
        if (increase <= 0) {
            throw new IllegalArgumentException("param increase must > 0");
        }
        return redisTemplate.opsForHash().increment(key, hashKey, increase);
    }

    /**
     * long类型hash递减
     *
     * @param key      键
     * @param hashKey  map集合的key
     * @param decrease 递增因子(大于0)
     * @return 递减后的值
     */
    public static Long hashDecrement(String key, String hashKey, long decrease) {
        if (decrease <= 0) {
            throw new IllegalArgumentException("param decrease must > 0");
        }
        return redisTemplate.opsForHash().increment(key, hashKey, -decrease);
    }

    /**
     * double类型hash递减
     *
     * @param key      键
     * @param hashKey  map集合的key
     * @param decrease 递增因子(大于0)
     * @return 递减后的值
     */
    public static Double hashDecrement(String key, String hashKey, double decrease) {
        if (decrease <= 0) {
            throw new IllegalArgumentException("param decrease must > 0");
        }
        return redisTemplate.opsForHash().increment(key, hashKey, -decrease);
    }

    /**************************** hashMap part end ****************************************/

    /**************************** hashSet part start ****************************************/

    /**
     * 根据key获取set集合
     *
     * @param key 键
     * @return set集合
     */
    public static Set<Object> setGet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 判断set集合中是否有对应的值
     *
     * @param key 键
     * @param obj 值
     * @return 对应的值是否存在
     */
    public static Boolean setHasObj(String key, Object obj) {
        return redisTemplate.opsForSet().isMember(key, obj);
    }

    /**
     * 写入set集合数据
     *
     * @param key   键
     * @param value 值
     */
    public static void setSet(String key, Object... value) {
        redisTemplate.opsForSet().add(key, value);
    }

    /**
     * 写入set集合数据 并设置失效时间
     *
     * @param key     键
     * @param timeout 失效时间 seconds timeout < 0 : 永久有效
     * @param value   值
     */
    public static void setSet(String key, long timeout, Object... value) {
        redisTemplate.opsForSet().add(key, value);
        if (timeout > 0) {
            RedisUtil.expire(key, timeout);
        }
    }

    /**
     * 获取set集合大小
     *
     * @param key 键
     * @return set集合大小
     */
    public static Long setGetSetSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 移除set集合值
     *
     * @param key   键
     * @param value 要移除的值
     */
    public static void setRemove(String key, Object... value) {
        redisTemplate.opsForSet().remove(key, value);
    }

    /**************************** hashSet part end ****************************************/

    /**************************** list part start ****************************************/

    /**
     * 根据key以及集合索引获取list集合
     *
     * @param key   键
     * @param start 开始索引
     * @param end   结束索引
     * @return list集合
     */
    public static List<Object> listGet(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 根据key获取list集合
     *
     * @param key 键
     * @return list集合
     */
    public static List<Object> listGet(String key) {
        return RedisUtil.listGet(key, 0, -1);
    }

    /**
     * 获取list集合大小
     *
     * @param key 键
     * @return list集合大小
     */
    public static Long listGetListSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 通过索引获取list集合中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 第一个元素，1 第二个元素，依次类推；index<0时，-1 倒数第一个元素，-2 倒数第二个元素，依次类推
     * @return 数据项
     */
    public static Object listGetByIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * 写入list数据
     *
     * @param key   键
     * @param value 数据项
     */
    public static void listSet(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 写入list数据 并设置失效时间
     *
     * @param key     键
     * @param value   数据项
     * @param timeout 失效时间 seconds timeout < 0 : 永久有效
     */
    public static void listSet(String key, Object value, long timeout) {
        redisTemplate.opsForList().rightPush(key, value);
        if (timeout > 0) {
            RedisUtil.expire(key, timeout);
        }
    }

    /**
     * 存入list集合
     *
     * @param key  键
     * @param list list集合
     */
    public static void listSetAll(String key, List<Object> list) {
        redisTemplate.opsForList().rightPushAll(key, list);
    }

    /**
     * 写入list数据 并设置失效时间
     *
     * @param key     键
     * @param list    list集合
     * @param timeout 失效时间 seconds timeout < 0 : 永久有效
     */
    public static void listSetAll(String key, List<Object> list, long timeout) {
        redisTemplate.opsForList().rightPushAll(key, list);
        if (timeout > 0) {
            RedisUtil.expire(key, timeout);
        }
    }

    /**
     * 根据索引index修改list集合中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 替换的值
     */
    public static void listUpdateByIndex(String key, long index, Object value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    /**
     * 移除count个值为value的数据项
     *
     * @param key   键
     * @param count 移除数量
     * @param value 移除的值
     */
    public static void listRemove(String key, long count, Object value) {
        redisTemplate.opsForList().remove(key, count, value);
    }
}
