package com.ejiaoyi.common.enums;

/**
 * 服务类型
 *
 * @author Make
 * @since 2020/9/2
 */
public enum ServiceType {

    /**
     * 房屋建设与市政基础
     */
    CONSTUCTION("1", "Constuction","房屋建设与市政基础"),
    /**
     * 政府采购
     */
    PURCHASE("2", "Purchase","政府采购"),
    /**
     * 交通工程
     */
    TRAFFIC("3", "Traffic","交通工程"),
    /**
     * 水利工程
     */
    IRRIGATION("4", "Irrigation","水利工程");

    private final String code;

    private final String name;

    private final String chineseName ;


    ServiceType(String code, String name, String chineseName) {
        this.code = code;
        this.name = name;
        this.chineseName = chineseName;
    }

    /**
     * 服务类型名称代码
     * @return
     */
    public String getCode() {
        return this.code;
    }

    /**
     * 服务类型名称
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * 服务类型中文名称
     * @return
     */
    public String getChineseName() {
        return this.chineseName;
    }


    public static ServiceType getServiceTypeName(String code){
        for (ServiceType serviceType : values()) {
            if (serviceType.getCode().equals(code)){
                return serviceType;
            }
        }
        return null;
    }

    /**
     * 返回服务类型中文名称
     * @param code
     * @return
     */
    public static String getServiceTypeChineseName(String code){
        for (ServiceType serviceType : values()) {
            if (serviceType.getCode().equals(code)){
                return serviceType.getChineseName();
            }
        }
        return null;
    }
}
