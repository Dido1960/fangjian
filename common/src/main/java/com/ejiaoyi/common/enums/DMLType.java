package com.ejiaoyi.common.enums;

/**
 * 数据库操纵类型 枚举类
 *
 * @author Z0001
 * @since 2020-03-17
 */
public enum DMLType {
    /**
     * 插入
     */
    INSERT("插入"),

    /**
     * 删除
     */
    DELETE("删除"),

    /**
     * 更新
     */
    UPDATE("更新"),

    /**
     * 查询
     */
    SELECT("查询"),

    /**
     * 导出
     */
    EXPORT("导出"),

    /**
     * 下载
     */
    DOWNLOAD("下载"),

    /**
     * 网络异常
     */
    NET_EXCEPTION("网络异常"),

    /**
     * 访问
     */
    ACCESS("访问"),

    /**
     * UNKNOWN
     */
    UNKNOWN("未知");

    private String name;

    DMLType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
