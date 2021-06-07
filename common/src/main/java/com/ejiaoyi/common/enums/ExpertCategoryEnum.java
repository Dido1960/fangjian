package com.ejiaoyi.common.enums;

/**
 * 专家类型枚举类
 * @author Mike
 */
public enum ExpertCategoryEnum {

    /**
     * 经济标专家
     */
    ECONOMIC(1),

    /**
     * 技术标专家
     */
    TECHNOLOGY(2),

    /**
     * 业主代表
     */
    OWNER(3);

    private final Integer code;

    ExpertCategoryEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }

    public static ExpertCategoryEnum getExpertCategoryCode(Integer code) {
        for(ExpertCategoryEnum expertCategoryEnum: ExpertCategoryEnum.values()) {
            if(expertCategoryEnum.getCode().equals(code)) {
                return expertCategoryEnum;
            }
        }

        return null;
    }
}
