package com.ejiaoyi.common.support;

/**
 * 动态数据源切换注解数据源KEY值
 *
 * @author Z0001
 * @since 2020-03-17
 */
public interface DataSourceKey {

    /**
     * 公用基础数据数据源
     */
    String COMMON = "common";

    /**
     * 日志数据源
     */
    String LOG = "log";
}
