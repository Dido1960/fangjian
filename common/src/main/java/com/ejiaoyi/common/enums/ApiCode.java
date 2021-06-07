package com.ejiaoyi.common.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * API 返回值信息码定义
 *
 * @author Kevin
 * @since 2020-05-09
 */
public enum ApiCode {
    // API AUTH
    API_AUTH("-1", ""),
    // 成功
    SUCCESS("1", "成功"),
    // 失败
    ERROR("0X0101", "失败"),
    // 数据必填
    DATA_REQUIRED("0X0201", null),
    // 数据匹配错误
    DATA_MISMATCH("0X0202", null),
    // 数据长度错误
    DATA_LENGTH("0X0203", null),
    // 访问异常
    CONNECT("0x0301", null),
    // 访问服务未完成
    NOT_COMPLETE("0x9901", null),
    // 访问服务错误下线
    OUT_LINE("0x9902", null);

    private final String code;

    private final String msg;

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    ApiCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ApiCode getEnum(String code) {
        for (ApiCode apiCode : ApiCode.values()) {
            if(StringUtils.equals(apiCode.code, code)) {
                return apiCode;
            }
        }

        return null;
    }
}
