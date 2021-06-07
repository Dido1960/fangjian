package com.ejiaoyi.common.enums;

/**
 * API 返回值信息码定义
 *
 * @author Kevin
 * @since 2020-05-09
 */
public enum ApiResultCode {

    /*******建议长度在5位*******/
    //1、数据不规范
    //
    //2、数据成功的数据
    //
    //3、请求失败的数据
    //
    //4、重定向、请求方法不合适

    //5 .服务器内部错误



    XML_NOT_FOUNT(11001,"当前XML不存在"),
    // 业务code已存在
    REPEAT_YW_CODE(30011, "业务code已存在"),

    // 数据不存在
    NO_DATA(10010, "数据不存在"),

    // 数据不存在
    NO_DATA_USER_INFO(10012, "个人信息数据不存在"),
    // 数据不存在
    NO_DATA_BIND(10013, "绑定数据不存在"),
    // 数据必填
    DATA_REQUIRED(10020, "必填字段为空"),
    // 数据匹配错误
    DATA_MISMATCH(10030, "数据不匹配"),

    // 数据匹配错误
    DATA_MISMATCH_HAVING_BING(10031, "用户已经绑定了企业"),
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
    // 成功
    SUCCESS(200 , "请求成功"),
    // 重复请求
    REPEAT(20010, "该操作已进行，无须多次请求"),

    // 该用户已存在
    REPEAT_USER(30011, "该用户已存在"),
    // 该用户已存在
    BIND_USER_HAVING(30012, "该用户已绑定企业用户绑定失败"),

    // 权限
    PERMISSIONS_COMPANY(40010, "无权限"),

    // 权限
    PERMISSIONS_BIND(40011, "绑定权限不存在"),

    // 权限
    PERMISSIONS_LOGIN(40012, "未进行身份确立，无法使用账号密码登录"),

    //二维码激活失败
    QR_ACTIVE_ERROR(40013, "二维码暂未被激活"),

    ERROR(500, "内部出错"),

    ERROR_SIGNAR(50010, "签名失败"),
    //支付宝内部创建失败
    ERROR_ORDER_CREATE(50010, "订单内部创建失败"),
    //
    ERROR_OTHER(50011, "其他错误");

    private final Integer code;

    private final String msg;

    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    ApiResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ApiResultCode getEnum(int code) {
        for (ApiResultCode apiCode : ApiResultCode.values()) {
            if (apiCode.code-code==0) {
                return apiCode;
            }
        }
        return null;
    }
}
