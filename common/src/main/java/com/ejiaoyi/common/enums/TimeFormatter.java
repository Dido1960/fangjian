package com.ejiaoyi.common.enums;

/**
 * DateTimeFormatter 格式化类型枚举
 *
 * @author Z0001
 * @since 2020-03-17
 */
public enum TimeFormatter {
    /**
     * 年月日
     */
    YYYY_MM_DD("yyyy-MM-dd"),

    /**
     * 年月日
     */
    YYYY_HH_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),

    /**
     * 年月日
     */
    YYYYHHDDHHMMSS("yyyyMMddHHmmss"),
    /**
     * 用于生成订单号，年月日
     */
    PAY_YYYY_HH_DD_HH_MM_SS("yyyyMMddHHmmss"),

    /**
     * 年
     */
    YYYY("yyyy"),

    /**
     * 月
     */
    MM("MM"),

    /**
     * 日
     */
    DD("dd");

    private String code;

    TimeFormatter(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
