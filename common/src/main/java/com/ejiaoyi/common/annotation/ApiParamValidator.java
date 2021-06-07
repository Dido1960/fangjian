package com.ejiaoyi.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * API 数据检测
 *
 * @author Z0001
 * @since 2020-03-18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ApiParamValidator {

    /**
     * 非空条件
     */
    String condition() default "";

    /**
     * 数据长度
     */
    int length() default 0;

    /**
     * 枚举数据检测
     */
    ApiEnumValidator validEnum() default @ApiEnumValidator;
}
