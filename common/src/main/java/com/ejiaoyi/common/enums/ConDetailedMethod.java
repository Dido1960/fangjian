package com.ejiaoyi.common.enums;

/**
 * 施工详细评审评标办法名称
 */
public enum ConDetailedMethod {

    /**
     * 施工能力扣分
     */
    CONSTRUCTION_ABILITY ("施工能力扣分"),
    /**
     * 施工组织设计扣分
     */
    CONSTRUCTION_DESIGN("施工组织设计扣分"),
    /**
     * 安全质量事故扣分
     */
    SAFETY_QUALITY_ACCIDENT("安全质量事故扣分"),
    /**
     * 建筑市场不良记录扣分
     */
    BAD_RECORD_MARKET("建筑市场不良记录扣分");

    private String name;

    private ConDetailedMethod(String name) {
        this.name = name;
    }

    /**
     * 返回方法名称
     * @return 返回方法名称
     */
    public String getName() {
        return this.name;
    }
}
