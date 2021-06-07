package com.ejiaoyi.common.enums;


/**
 * 政府部门类型
 *
 * @author Make
 * @return
 * @date 2020/08/26 11:00
 */
public enum GovDepType {

    /**
     * 招标办
     */
    TENDER_OFFICE(1),

    /**
     * 交易中心（专家录入）
     */
    WORKER_EXTRACT(2),

    /**
     * 交易中心（工程处）
     */
    WORKER_THE_AGENCY(3);


    private Integer type;

    GovDepType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return this.type;
    }


    /**
     * 获取政府用户类型
     *
     * @return
     * @author Make
     * @date 2020/08/26 11:00
     */
    public static GovDepType getGovDepType(Integer type) {
        for (GovDepType govDepType : GovDepType.values()) {
            if (String.valueOf(govDepType.getType()).equals(String.valueOf(type))) {
                return govDepType;
            }
        }
        return null;
    }
}
