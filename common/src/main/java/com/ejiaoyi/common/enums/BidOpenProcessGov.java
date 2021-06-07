package com.ejiaoyi.common.enums;

/**
 * 监管 开标流程
 * @author : yyb
 * @date : 2020-12-7 14:35
 */
public enum BidOpenProcessGov {
    /**
     * 签到信息
     */
    SIGN_INFO(1, "signInfo","签到信息"),

    /**
     * 身份检查
     */
    IDENTITY_CHECK(2, "identityCheck","身份检查"),

    /**
     * 浮动点抽取
     */
    FLOAT_POINT(3, "floatPoint","浮动点抽取"),

    /**
     * 标书解密
     */
    BIDDER_FILE_DECRYPT(4, "bidderFileDecrypt","标书解密"),

    /**
     * 招标控制价
     */
    BID_CONTROL_PRICE(5, "bidControlPrice","招标控制价"),

    /**
     * 开标记录表
     */
    BID_OPEN_RECORD(6, "bidOpenRecord","开标记录表");



    private final int code;

    /**
     * 环节名
     */
    private final String processName;

    /**
     * 说明
     */
    private final String remake;

    BidOpenProcessGov(Integer code, String processName, String remake) {
        this.code = code;
        this.processName = processName;
        this.remake = remake;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getRemake() {
        return this.remake;
    }

    public String getProcessName() {
        return this.processName;
    }

}
