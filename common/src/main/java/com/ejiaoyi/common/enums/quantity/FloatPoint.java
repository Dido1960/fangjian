package com.ejiaoyi.common.enums.quantity;

import org.apache.commons.lang3.StringUtils;

/**
 * 浮动点 枚举类
 *
 * @author Kevin
 * @since 2020-09-22
 */
public enum FloatPoint {

    A("0.015"),

    B("0.0125"),

    C("0.01"),

    D("0.0075"),

    E("0.005"),

    F("0.0025");

    private final String code;

    public String getCode() {
        return this.code;
    }

    FloatPoint(String code) {
        this.code = code;
    }

    public static FloatPoint getEnum(String code) {
        for (FloatPoint floatPoint : FloatPoint.values()) {
            if (StringUtils.equals(floatPoint.code, code)) {
                return floatPoint;
            }
        }

        return null;
    }
}
