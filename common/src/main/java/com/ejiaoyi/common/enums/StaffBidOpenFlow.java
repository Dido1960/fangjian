package com.ejiaoyi.common.enums;

/**
 * 代理开标流程环节
 *
 * @author Make
 * @since 2020/7/9
 */
public enum StaffBidOpenFlow {

    /**
     * 招标控制价
     */
    CONTROL_PRICE("controlPrice", "招标控制价"),
    /**
     * 抽取浮动点
     */
    FLOAT_POINT("floatPoint", "抽取浮动点"),
    /**
     * 公布投标人名单
     */
    PUBLISH_BIDDER("publishBidder", "公布投标人名单"),
    /**
     * 委托人身份检查
     */
    CHECK_PRINCIPAL_IDENTITY("checkPrincipalIdentity", "委托人身份检查"),
    /**
     * 文件上传及解密
     */
    BIDDER_FILE_DECRYPT("bidderFileDecrypt", "文件上传及解密"),
    /**
     * 控制价分析
     */
    CONTROL_PRICE_ANALYSIS("controlPriceAnalysis", "控制价分析"),
    /**
     * 文件唱标
     */
    FILE_CURSOR("fileCursor", "文件唱标"),
    /**
     * 开标记录表
     */
    BID_OPEN_RECORD("bidOpenRecord", "开标记录表"),
    /**
     * 文件数据
     */
    BID_FILE_UPLOAD("bidFileUpload", "数据上传评标"),
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

    StaffBidOpenFlow(String name, String flowName) {
        this.name = name;
        this.flowName = flowName;
    }

    public String getFlowName() {
        return this.flowName;
    }

    public String getName() {
        return this.name;
    }

    public static StaffBidOpenFlow getStaffBidOpenFlowByName(String name){
        for (StaffBidOpenFlow staffBidOpenFlow : values()) {
            if (staffBidOpenFlow.getName().equals(name)){
                return staffBidOpenFlow;
            }
        }
        return null;
    }
}
