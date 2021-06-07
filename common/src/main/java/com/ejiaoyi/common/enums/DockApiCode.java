package com.ejiaoyi.common.enums;

/**
 * 对接API 返回值信息码定义
 *
 * @author Make
 * @since 2020-12-26
 */
public enum DockApiCode {
    /*******建议长度在5位*******/
    //1、数据不规范
    //2、数据成功的数据
    //3、请求失败的数据
    //4、重定向、请求方法不合适
    //5 .服务器内部错误

    // 数据不存在
    NO_DATA(10010, "数据不存在"),
    // 数据不存在
    NO_DATA_COMPANY_INFO(10011, "企业信息数据不存在"),
    // 数据不存在
    NO_DATA_USER_INFO(10012, "个人信息数据不存在"),
    // 数据不存在
    NO_DATA_BIND(10013, "绑定数据不存在"),
    // 数据已存在
    DATA_ALREADY_EXIST(10014, "数据已存在"),
    // 数据解密异常
    DATA_DECRYPT_ERROR(10014, "数据解密异常"),

    // 数据必填
    DATA_REQUIRED(10020, "必填字段为空"),
    // 数据匹配错误
    DATA_MISMATCH(10030, "数据不匹配"),
    // 数据匹配错误
    DATA_MISMATCH_PASSWORD_ERROR(10032, "密码错误"),
    // 数据长度错误
    DATA_LENGTH(10040, "数据长度不符合规范"),
    // token无效
    TOKEN_INVALID(10050, "Token无效"),
    // token不允许为空
    TOKEN_MUST(10051, "Token不允许为空"),
    // token 验证失败
    TOKEN_INVALID_ERROR(10052, "Token验证失败"),
    // 数据长度错误
    UNAUTHORIZED_ACCESS(10060, "授权信息不存在"),
    // 数据错误
    PASSWORD_MISTAKE(10070, "密码错误"),
    // 数据不存在
    AGRS_VALIDATION_FAILS(10080, "参数校验失败"),

    // 文件校验失败信息
    FILE_VALID_ERROR_INFO(10090, "文件校验失败"),

    // 文件类型不匹配
    NO_FILE_TYPE_INFO(10091, "文件类型不匹配"),

    // 成功
    SUCCESS(200 , "请求成功"),
    // 重复请求
    REPEAT(20010, "该操作已进行，无须多次请求"),

    // 权限
    PERMISSIONS_COMPANY(40010, "无权限"),

    ERROR(500, "内部出错"),
    ERROR_OTHER(50011, "其他错误");

    private final Integer code;

    private final String msg;

    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    DockApiCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static DockApiCode getEnum(int code) {
        for (DockApiCode dockApiCode : DockApiCode.values()) {
            if (dockApiCode.code-code==0) {
                return dockApiCode;
            }
        }
        return null;
    }
}