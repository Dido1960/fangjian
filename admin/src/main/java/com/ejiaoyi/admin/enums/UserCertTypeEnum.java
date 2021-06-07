package com.ejiaoyi.admin.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 证件类型
 *
 * @author fengjunhong
 * @since 2020/5/19
 */
public enum UserCertTypeEnum {

    /**
     * 营业执照
     */
    YYZZ("营业执照"),

    /**
     * 身份证
     */
    SFZ("身份证"),

    /**
     * 驾驶证
     */
    JSZ("驾驶证"),

    /**
     * 护照
     */
    HZ("护照");


    private String name;

    UserCertTypeEnum(String value) {
        this.name = value;
    }

    /**
     * 证件类型键值对
     *
     * @return
     */
    public static List listUserCertType() {
        List list = new ArrayList();
        for (UserCertTypeEnum userCertTypem : UserCertTypeEnum.values()) {
            list.add(userCertTypem.name);
        }
        return list;
    }

}
