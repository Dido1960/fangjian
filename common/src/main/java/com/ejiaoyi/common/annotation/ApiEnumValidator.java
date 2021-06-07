package com.ejiaoyi.common.annotation;

import com.ejiaoyi.common.enums.BaseEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * API 枚举数据检测
 *
 * @author Z0001
 * @since 2020-03-18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ApiEnumValidator {

    /**
     * 枚举类型
     */
    Class<? extends Enum<?>> type() default BaseEnum.class;

    /**
     * 枚举取值参数名
     */
    String param() default "code";
}
