package com.ejiaoyi.common.constant;

/**
 * @Desc: 未签到状态
 * @author: yyb
 * @date: 2020-8-11 13:41
 */
public interface NotCheckinStatus {
    /**
     * 默认状态
     */
    Integer DEFAULT_ST = 0;

    /**
     * 迟到
     */
    Integer LATE = 1;

    /**
     * 弃标
     */
    Integer ABANDON_BID = 2;

    /**
     * 已递交
     */
    Integer SUBMIT = 3;

    /**
     * 其他情况
     */
    Integer OTHER_ST = 9;

}
