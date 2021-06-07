package com.ejiaoyi.admin.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengjunhong
 * @since 2020/5/19
 */
public enum PayTypeEnum {

    /**
     * pos机
     */
    POS("pos机"),

    /**
     * 现金
     */
    CASH("现金"),

    /**
     * 对公转账
     */
    TRANSFER_ACCOUNTS("对公转账");

    private String name;

    PayTypeEnum(String name) {
        this.name = name;
    }

    /**
     * 返回所有支付方式
     *
     * @return
     */
    public static List<String> listPayType() {
        List<String> list = new ArrayList<>();
        for (PayTypeEnum payTypeEnum : PayTypeEnum.values()) {
            list.add(payTypeEnum.name);
        }
        return list;
    }

}
