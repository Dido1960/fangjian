package com.ejiaoyi.common.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 服务状态 枚举类
 *
 * @author Kevin
 * @since 2020-04-22
 */
public enum ServiceState {

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

    ServiceState(String code) {
        this.code = code;
    }

    public static ServiceState getEnum(String code) {
        for (ServiceState serviceState : ServiceState.values()) {
            if(StringUtils.equals(serviceState.code, code)) {
                return serviceState;
            }
        }

        return null;
    }
}
