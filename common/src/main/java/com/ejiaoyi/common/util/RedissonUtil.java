package com.ejiaoyi.common.util;

import com.ejiaoyi.common.locker.DistributeLocker;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * Redisson 分布式锁工具类
 *
 * @author Z0001
 * @since 2020/3/25
 */
public class RedissonUtil {

    private static DistributeLocker locker;

    public static void setLocker(DistributeLocker locker) {
        RedissonUtil.locker = locker;
    }
    /**
     * 加锁
     *
     * @param key 锁的key值
     */
    public static void lock(String key) {
        locker.lock(key);
    }

    /**
     * 加锁
     *
     * @param key     锁的key值
     * @param timeout 超时时间 seconds
     */
    public static void lock(String key, int timeout) {
        locker.lock(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 加锁
     *
     * @param key      锁的key值
     * @param timeout  超时时间
     * @param timeUnit 超时单位
     * @return RLock对象
     */
    public static void lock(String key, int timeout, TimeUnit timeUnit) {
        locker.lock(key, timeout, timeUnit);
    }

    /**
     * 释放锁
     *
     * @param key 锁的key值
     */
    public static void unlock(String key) {
        locker.unlock(key);
    }

    /**
     * 尝试获取锁
     *
     * @param key       锁的key值
     * @param waitTime  最大等待时间 seconds
     * @param leaseTime 上锁后的自动释放时间 seconds
     * @return 是否成功获取到锁
     */
    public static boolean tryLock(String key, int waitTime, int leaseTime) throws InterruptedException {
        return locker.tryLock(key, waitTime, leaseTime, TimeUnit.SECONDS);
    }

    /**
     * 尝试获取锁
     *
     * @param key       锁的key值
     * @param timeUnit  时间单位
     * @param waitTime  最大等待时间
     * @param leaseTime 上锁后的自动释放时间
     * @return 是否成功获取到锁
     */
    public static boolean tryLock(String key, TimeUnit timeUnit, int waitTime, int leaseTime) throws InterruptedException {
        return locker.tryLock(key, waitTime, leaseTime, timeUnit);
    }

    /**
     * 释放锁
     *
     * @param rLock RLock对象
     */
    public static void unlock(RLock rLock) {
        rLock.unlock();
    }
}