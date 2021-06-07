package com.ejiaoyi.common.constant;

/**
 * @Description:
 * @Auther: liuguoqiang
 * @Date: 2020-10-8 10:47
 */
public interface BackStatus {
    /**
     * 未审核
     */
    Integer UNREVIEWED = 0;
    /**
     * 审核通过
     */
    Integer PASS = 1;

    /**
     * 审核未通过
     */
    Integer NOPASS = 2;
}
