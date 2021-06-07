package com.ejiaoyi.common.enums.quantity;

import org.apache.commons.lang3.StringUtils;

/**
 * 工程量清单服务版本 枚举类
 *
 * @author Kevin
 * @since 2020-04-21
 */
public enum QuantityServiceVersion {

    V1("F325C7B24779580E85C85D7C5CF0D645"),

    V2_1("32C519D9C7D8540BAABAB3A66C1C67F6");

    /**
     * 版本编码使用16位 UUID 大写
     */
    private final String code;

    public String getCode() {
        return this.code;
    }

    QuantityServiceVersion(String code) {
        this.code = code;
    }

    public static QuantityServiceVersion getEnum(String code) {
        for (QuantityServiceVersion quantityServiceVersion : QuantityServiceVersion.values()) {
            if(StringUtils.equals(quantityServiceVersion.code, code)) {
                return quantityServiceVersion;
            }
        }

        return null;
    }
}
