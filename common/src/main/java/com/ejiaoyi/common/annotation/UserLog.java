package com.ejiaoyi.common.annotation;

import com.ejiaoyi.common.enums.DMLType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用户日志
 *
 * @author Z0001
 * @since 2020-03-17
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface UserLog {

    /**
     * 日志内容 支持 SpEL
     */
    String value();

    /**
     * 数据库执行类型 默认未知
     */
    DMLType dmlType() default DMLType.UNKNOWN;
}
