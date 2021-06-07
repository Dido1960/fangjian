package com.ejiaoyi.common.enums;

/**
 * 保证金缴纳方式枚举类
 * @author Mike
 */
public enum MarginWayEnum {

    /**
     * 保证金方式
     */
    MARGIN("1"),

    /**
     * 保函方式
     */
    GUARANTEE("2"),

    /**
     * 其他
     */
    OTHER("99");

    private final String code;

    MarginWayEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static MarginWayEnum getMarginWayCode(String code) {
        for(MarginWayEnum marginWayEnum: MarginWayEnum.values()) {
            if(marginWayEnum.getCode().equals(code)) {
                return marginWayEnum;
            }
        }

        return null;
    }
}
