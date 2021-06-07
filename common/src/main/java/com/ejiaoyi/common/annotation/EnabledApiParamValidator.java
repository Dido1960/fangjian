package com.ejiaoyi.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用ApiParamValidator 检查的注解
 *
 * @author Z0001
 * @since 2020-05-13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface EnabledApiParamValidator {
    String value() default "";
}
