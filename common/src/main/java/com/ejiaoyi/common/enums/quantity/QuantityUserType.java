package com.ejiaoyi.common.enums.quantity;

import org.apache.commons.lang3.StringUtils;

/**
 * 文件上传 关于部署方式 枚举 用于 测试 或 正式  使用
 * @Auther: liuguoqiang
 * @Date: 2021-1-6 11:52
 */
public enum QuantityUserType {

    /**
     * 正式
     */
    FORMAL("formal"),

    /**
     * 测试
     */
    TEST("test");

    private final String code;

    public String getCode() {
        return this.code;
    }

    QuantityUserType(String code) {
        this.code = code;
    }

    public static QuantityUserType getEnum(String code) {
        for (QuantityUserType userType : QuantityUserType.values()) {
            if(StringUtils.equals(userType.code, code)) {
                return userType;
            }
        }

        return null;
    }
}
