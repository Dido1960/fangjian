package com.ejiaoyi.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 手机 开标流程 信息
 * @author : liuguoqiang
 * @date : 2020-12-7 14:35
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum BidOpenProcessPhone {
    /**
     * 投标人名单
     */
    BIDDER_LIST(1, "bidder-list","投标人名单"),

    /**
     * 文件解密
     */
    DESTROY(2, "destroy","文件解密"),

    /**
     * 浮动点
     */
    FLOAT(3, "float","浮动点"),

    /**
     * 招标控制价
     */
    PRICE(4, "price","招标控制价"),

    /**
     * 报价确认
     */
    CONFIRM(5, "confirm","报价确认"),

    /**
     * 控制价分析
     */
    ANALYSIS(6, "analysis","控制价分析"),

    /**
     * 开标一览表
     */
    OPEN_TABLE(7, "open-table","开标一览表"),

    /**
     * 开标结束
     */
    OPEN_END(8, "open-end","开标结束"),

    /**
     * 最高投标限价
     */
    HIGHEST_PRICE(9, "price","最高投标限价"),

    /**
     * 最高投标限价分析
     */
    HIGHEST_ANALYSIS(10, "analysis","最高投标限价分析"),

    /**
     * 质询"
     */
    QUESTION(11, "question","质询");

    private final int code;

    /**
     * 环节名
     */
    private final String processName;

    /**
     * 说明
     */
    private final String chineseName;

    BidOpenProcessPhone(Integer code, String processName, String chineseName) {
        this.code = code;
        this.processName = processName;
        this.chineseName = chineseName;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getChineseName() {
        return this.chineseName;
    }

    public String getProcessName() {
        return this.processName;
    }
}
