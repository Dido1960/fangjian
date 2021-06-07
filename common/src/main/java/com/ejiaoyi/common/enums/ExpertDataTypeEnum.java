package com.ejiaoyi.common.enums;

/**
 * 推送专家数据类型枚举类
 * @author Mike
 */
public enum ExpertDataTypeEnum {

    /**
     * 正常抽取专家
     */
    MARGIN("1"),

    /**
     * 补抽专家
     */
    GUARANTEE("2"),

    /**
     * 其他
     */
    OTHER("99");

    private final String code;

    ExpertDataTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static ExpertDataTypeEnum getExpertDataTypeCode(String code) {
        for(ExpertDataTypeEnum expertDataTypeEnum: ExpertDataTypeEnum.values()) {
            if(expertDataTypeEnum.getCode().equals(code)) {
                return expertDataTypeEnum;
            }
        }

        return null;
    }
}
