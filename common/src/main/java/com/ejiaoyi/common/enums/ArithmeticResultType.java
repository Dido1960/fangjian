package com.ejiaoyi.common.enums;

/**
 * 运算结果状态
 *
 * @author Make
 * @since 2020/9/17
 */
public enum ArithmeticResultType {
    // 运算失败
    ERROR("-1", "运算失败"),

    // 运算成功
    SUCCESS("1", "运算成功");

    /**
     * 状态
     */
    private final String code;
    /**
     * 描述
     */
    private final String desc;

    ArithmeticResultType (String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}