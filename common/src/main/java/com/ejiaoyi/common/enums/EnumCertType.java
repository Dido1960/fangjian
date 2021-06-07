package com.ejiaoyi.common.enums;

/**
 * 证书类型枚举
 *
 * @author fengjunhong
 * @since 2020/5/27
 */
public enum EnumCertType {

    //-----------------订单类型----------------------

    //
    ZB_CERT("1", "招标文件生成器"),

    TB_CERT("2", "投标文件生成器"),

    GZRY_CERT("3", "工作人员锁"),

    PUT_ON_RECORD_CERT("4", "工作人员锁"),

    SIGN_CERT("5", "签章锁"),

    SIGN_CERT_COMPANY("5-1", "企业章"),

    SIGN_CERT_PERSON("5-2", "个人章");

    private String code;

    private String name;

    private EnumCertType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 返回订单类型，状态码
     * @return
     */
    public String getCode() {
        return this.code;
    }

    /**
     * 返回订单类型，名称
     * @return
     */
    public String getCertTypeName() {
        return this.name;
    }

    public static String getName(String code){
        for (EnumCertType certType : values()) {
            if (certType.getCode().equals(code)){
                return certType.getCertTypeName();
            }
        }
        return null;
    }

}
