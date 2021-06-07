package com.ejiaoyi.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * API 认证
 *
 * @author Z0001
 * @since 2020-03-18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ApiAuthentication {

    /**
     * 认证信息参数下标
     */
    int authentication() default 0;

    /**
     * 接口名称
     */
    String apiName();

    /**
     * 是否启用 幂等防重复验证
     */
    boolean replay() default true;
}
