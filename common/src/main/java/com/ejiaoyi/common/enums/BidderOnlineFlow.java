package com.ejiaoyi.common.enums;

/**
 * 代理开标流程环节
 *
 * @author Make
 * @since 2020/7/9
 */
public enum BidderOnlineFlow {

    /**
     * 投标文件
     */
    BID_FILE("bidFile", "投标文件"),
    /**
     * 签到
     */
    IDENTITY_AUTH("identityAuth", "签到"),
    /**
     * 招标控制价
     */
    CONTROL_PRICE("controlPrice", "招标控制价"),
    /**
     * 抽取浮动点
     */
    FLOAT_POINT("floatPoint", "抽取浮动点"),
    /**
     * 投标人名单
     */
    ALL_BIDDERS("allBidders", "投标人名单"),
    /**
     * 文件上传及解密
     */
    BIDDER_FILE_DECRYPT("bidderFileDecrypt", "文件上传及解密"),
    /**
     * 报价确认
     */
    CONFIRM_BIDDER_PRICE("confirmBidderPrice", "报价确认"),
    /**
     * 开标一览表确认
     */
    CONFIRM_BID_OPEN_RECORD("confirmBidOpenRecord", "开标一览表确认"),
    /**
     * 控制价分析
     */
    CONTROL_PRICE_ANALYSIS("controlPriceAnalysis", "控制价分析"),
    /**
     * 开标结束
     */
    BID_OPEN_END("bidOpenEnd", "开标结束"),
    /**
     * 复会时间
     */
    RESUME_TIME("resumeTime", "复会时间");

    private String name;

    private String flowName;

    BidderOnlineFlow(String name, String flowName) {
        this.name = name;
        this.flowName = flowName;
    }

    public String getFlowName() {
        return this.flowName;
    }

    public String getName() {
        return this.name;
    }

    public static BidderOnlineFlow getBidderOnlineFlowByName(String name){
        for (BidderOnlineFlow bidderOnlineFlow : values()) {
            if (bidderOnlineFlow.getName().equals(name)){
                return bidderOnlineFlow;
            }
        }
        return null;
    }
}
