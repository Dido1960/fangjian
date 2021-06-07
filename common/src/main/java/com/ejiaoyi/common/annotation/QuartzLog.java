package com.ejiaoyi.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Quartz 定时任务日志
 *
 * @author Z0001
 * @since 2020-05-25
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface QuartzLog {

    String value() default "";
}
