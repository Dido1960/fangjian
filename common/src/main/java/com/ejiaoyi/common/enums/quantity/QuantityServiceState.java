package com.ejiaoyi.common.enums.quantity;

import org.apache.commons.lang3.StringUtils;

/**
 * 服务状态 枚举类
 *
 * @author Kevin
 * @since 2020-04-22
 */
public enum QuantityServiceState {

    /**
     * 等待状态
     */
    WAIT("WAIT"),

    /**
     * 活动状态
     */
    ACTIVE("ACTIVE"),

    /**
     * 完成
     */
    COMPLETED("COMPLETED"),

    /**
     * 下线状态
     */
    OUTLINE("OUTLINE");

    private final String code;

    public String getCode() {
        return this.code;
    }

    QuantityServiceState(String code) {
        this.code = code;
    }

    public static QuantityServiceState getEnum(String code) {
        for (QuantityServiceState quantityServiceState : QuantityServiceState.values()) {
            if(StringUtils.equals(quantityServiceState.code, code)) {
                return quantityServiceState;
            }
        }

        return null;
    }
}
