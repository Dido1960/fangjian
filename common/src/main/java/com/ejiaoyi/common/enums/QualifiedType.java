package com.ejiaoyi.common.enums;

/**
 * @author fengjunhong
 * @since 2020-11-24
 */
public enum QualifiedType {

    UNQUALIFIED("0","不合格"),

    QUALIFIED("1","合格");

   /* 状态码*/
    private String code;

    /*状态名称*/
    private String codeMsg;

    QualifiedType(String code,String codeMsg){
        this.code = code;
        this.codeMsg = codeMsg;
    }

    /**
     *
     * @return
     */
    public String getCode() {
        return code;
    }

    public String getCodeMsg() {
        return codeMsg;
    }
}
