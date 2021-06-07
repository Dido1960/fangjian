package com.ejiaoyi.common.enums;

/**
 * 用户认证类型 枚举类
 *
 * @author fengjunhong
 * @since 2020/5/11
 */
public enum UserAuthType {

    /**
     * 用户名登录
     */
    LOGIN_USER_NAME(0),

    /**
     * CA锁登录
     */
    LOGIN_CERT(1);

    private Integer authType;

    UserAuthType(Integer authType) {
        this.authType = authType;
    }

    public Integer getUserAuthType() {
        return this.authType;
    }

}
