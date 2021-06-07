package com.ejiaoyi.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 招标方式
 *
 * @author Make
 * @since 2020/12/28
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TenderMode {

    /**
     * 公开招标
     */
    OPEN_TENDER("1", "公开招标"),
    /**
     * 邀请招标
     */
    INVITE_BIDDING("2", "邀请招标"),
    /**
     * 其他
     */
    OTHER("99", "其他");

    private final String code;

    private String name;


    TenderMode(String code) {
        this.code = code;
        for (TenderMode tenderMode : values()) {
            if (tenderMode.getCode().equals(code)){
                this.name =tenderMode.getName();
            }
        }

    }

   private TenderMode(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 招标方式代码
     * @return
     */
    public String getCode() {
        return this.code;
    }

    /**
     * 招标方式名称
     * @return
     */
    public String getName() {
        return this.name;
    }

    public static TenderMode getTenderModeByCode(String code){
        for (TenderMode tenderMode : values()) {
            if (tenderMode.getCode().equals(code)){
                return tenderMode;
            }
        }
        return null;
    }

    public static TenderMode getTenderModeName(String code){
        for (TenderMode tenderMode : values()) {
            if (tenderMode.getCode().equals(code)){
                return tenderMode;
            }
        }
        return null;
    }

}
