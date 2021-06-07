package com.ejiaoyi.common.enums;


/**
 * 系统内置用户类型
 *
 * @author lesgod
 * @return
 * @date 2020/5/12 12:47
 */
public enum SysUserType {

    /**
     * 系统用户
     */
    USER(0),

    /**
     * 政府级用户
     */
    GOVUSER(1),

    /**
     * 企业用户
     */
    COMPANYUSER(2);


    private Integer type;

    SysUserType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return this.type;
    }


    /**
     * 获取系统内置用户类型
     *
     * @return
     * @author lesgod
     * @date 2020/5/12 13:23
     */
    public static SysUserType getSysUserType(Integer type) {
        for (SysUserType sysUserType : SysUserType.values()) {
            if (String.valueOf(sysUserType.getType()).equals(String.valueOf(type))) {
                return sysUserType;
            }
        }
        return null;
    }
}
