package com.ejiaoyi.common.annotation;

import java.lang.annotation.*;

/**
 * Redisson 分布式锁
 *
 * @author Z0001
 * @since 2020-03-25
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD})
public @interface RedissonLock {
    String key() default "";
}
