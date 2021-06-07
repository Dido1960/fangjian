package com.ejiaoyi.common.enums;

/**
 *
 * @Auther: liuguoqiang
 * @Date: 2020-12-28 11:35
 */
public enum MutualItemMethod {
    /**
     * 互保共建
     */
    MUTUAL_ITEM_METHOD_1("投标人在投标文件所附《互保共建互为市场承诺书》（复印件）中承诺凡采购注册在本市行政区域范围内企业原材料不足所竞标工程总材料的30%，无加分",
            "0", "fixed", "", ""),
    MUTUAL_ITEM_METHOD_2("投标人在投标文件所附《互保共建互为市场承诺书》（复印件）中承诺凡采购注册在本市行政区域范围内企业原材料占到所竞标工程总材料的30%（含30%）以上，50%以下的加1分",
            "1", "fixed", "", ""),
    MUTUAL_ITEM_METHOD_3("投标人在投标文件所附《互保共建互为市场承诺书》（复印件）中承诺凡采购注册在本市行政区域范围内企业原材料占到所竞标工程总材料的50%（含50%）以上的加1.5分",
            "1.5", "fixed", "", "");

    /**
     * 评分项内容
     */
    private final String itemContent;

    /**
     * 评分项分数
     */
    private final String score;

    /**
     * 分数类型（拓展字段：酌情打分  固定分值）范围 range 固定 fixed
     */
    private final String scoreType;

    /**
     * 分数范围
     */
    private final String scoreRange;

    /**
     * 备注说明
     */
    private final String remark;


    MutualItemMethod(String itemContent, String score, String scoreType, String scoreRange, String remark) {
        this.itemContent = itemContent;
        this.score = score;
        this.scoreType = scoreType;
        this.scoreRange = scoreRange;
        this.remark = remark;
    }

    public String getItemContent() {
        return this.itemContent;
    }

    public String getScore() {
        return this.score;
    }

    public String getScoreType() {
        return this.scoreType;
    }

    public String getScoreRange() {
        return this.scoreRange;
    }

    public String getRemark() {
        return this.remark;
    }

}
