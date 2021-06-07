package com.ejiaoyi.common.enums;

/**
 * 启用状态
 */
public enum Enabled {
    /**
     * 启用
     */
    YES(1),

    /**
     * 禁用
     */
    NO(0);

    private final Integer code;

    Enabled(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }

    public static Enabled getEnabled(Integer code) {
        for(Enabled enabled: Enabled.values()) {
            if(enabled.getCode().equals(code)) {
                return enabled;
            }
        }

        return null;
    }
}