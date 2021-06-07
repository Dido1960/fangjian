package com.ejiaoyi.common.enums;


/**
 * 专家类型
 *
 * @author Make
 * @return
 * @date 2020/10/30 14:47
 */
public enum ExpertUserType {

    /**
     * 经济标专家
     */
    ECONOMIC(1),

    /**
     * 技术标专家
     */
    TECHNICAL(2),

    /**
     * 业主代表
     */
    REPRESENTATIVE(3);


    private final Integer type;

    ExpertUserType(Integer type) {
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
    public static ExpertUserType getSysUserType(Integer type) {
        for (ExpertUserType expertUserType : ExpertUserType.values()) {
            if (String.valueOf(expertUserType.getType()).equals(String.valueOf(type))) {
                return expertUserType;
            }
        }
        return null;
    }
}
