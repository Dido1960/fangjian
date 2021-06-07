package com.ejiaoyi.common.enums.quantity;

import org.apache.commons.lang3.StringUtils;

/**
 * 路径类型 枚举类
 *
 * @author Kevin
 * @since 2020-10-09
 */
public enum PathType {

    /**
     * 网络地址
     */
    NETWORK("network"),

    /**
     * 本地地址
     */
    LOCAL("local");

    private final String code;

    public String getCode() {
        return this.code;
    }

    PathType(String code) {
        this.code = code;
    }

    public static PathType getEnum(String code) {
        for (PathType pathType : PathType.values()) {
            if(StringUtils.equals(pathType.code, code)) {
                return pathType;
            }
        }

        return null;
    }
}
