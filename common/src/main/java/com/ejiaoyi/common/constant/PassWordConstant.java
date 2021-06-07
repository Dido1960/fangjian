package com.ejiaoyi.common.constant;

/**
 * @Desc: 密码常量类
 * @author: Make
 * @date: 2020-9-28 13:12
 */
public interface PassWordConstant {

    /**
     * zip解密密码
     * 后期固定密码： C2E4D687CBF53388474FB6FF11E64F4E
     */
    String UNZIP_PASS_WORD = "scqskj";

    /**
     * 北京CA(0的MD5值)
     */
    String BJ_CA = "A0B923820DCC509A";

    /**
     * 互认CA(1的MD5值)
     */
    String PUBLIC_CA = "9D4C2F636F067F89";

    /**
     * 手机证书(2的MD5值)
     */
    String PHONE_CA = "4B5CE2FE28308FD9";
}
