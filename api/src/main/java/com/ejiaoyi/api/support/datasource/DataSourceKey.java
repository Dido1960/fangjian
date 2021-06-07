package com.ejiaoyi.api.support.datasource;

/**
 * 动态数据源切换注解数据源KEY值
 *
 * @author Z0001
 * @since 2020-03-17
 */
public interface DataSourceKey {

    /**
     * 主数据源
     */
    String MASTER = "master";

    /**
     * 日志数据源
     */
    String LOG = "log";
}
