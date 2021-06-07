package com.ejiaoyi.common.enums.quantity;

/**
 * 服务类型 枚举类
 *
 * @author Kevin
 * @since 2020-09-01
 */
public enum QuantityServiceType {
    // 工程量清单解析服务
    PARSE_QUANTITY(1),
    // 零负报价分析服务
    PRICE_ANALYSIS(2),
    // 算术性分析服务
    ARITHMETIC_ANALYSIS(3),
    // 错漏项分析服务
    STRUCTURE_ANALYSIS(4),
    // 取费基础分析服务
    RULE_ANALYSIS(5),
    // 工程量清单报价得分计算服务
    CALC_SCORE(6),
    // 其它服务
    OTHER(7);

    final int code;

    public int getCode() {
        return this.code;
    }

    QuantityServiceType(int code) {
        this.code = code;
    }

    public QuantityServiceType getEnum(int code) {
        for (QuantityServiceType quantityServiceType : QuantityServiceType.values()) {
            if(this.code == code) {
                return quantityServiceType;
            }
        }

        return null;
    }
}
