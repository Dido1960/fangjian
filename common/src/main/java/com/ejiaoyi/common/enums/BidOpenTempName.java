package com.ejiaoyi.common.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 开标记录表模板
 * @author fengjunhong
 */
public enum BidOpenTempName {

    /**
     * 开标记录表
     */
    BID_OPEN_TABLE("A03,A04,A05,A08,A10,A11,A12", "bidOpenTable",true,"getCoverReportData", "开标记录表"),

    /**
     * 投标人签到表
     */
    BIDDER_SIGN_TABLE("A03,A04,A05,A08,A10,A11,A12", "bidderSignTable",true,null, "投标人签到表"),

    /**
     * 投标文件递交时间签字表
     */
    FILE_SIGN_TABLE("A03,A04,A05,A08,A10,A11,A12", "fileSignTable",true,null, "投标文件递交时间签字表");


    /**
     * 模板编号字符串集合
     */
    private final String code;

    /**
     * 模板名称
     */
    private final String name;

    /**
     * 是否水平打印
     * 默认竖着打印 false
     */
    private final Boolean levelPrint;

    /**
     * 获取模板数据的方法名
     */
    private final String getDataMethodName;

    /**
     * 前台显示名称
     */
    private final String templateChineseName;

    BidOpenTempName(String code, String name, Boolean levelPrint, String getDataMethodName, String templateChineseName) {
        this.code = code;
        this.name = name;
        this.levelPrint = levelPrint;
        this.getDataMethodName = getDataMethodName;
        this.templateChineseName = templateChineseName;
    }

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
    }

    public Boolean getLevelPrint() {
        return levelPrint;
    }

    public String getGetDataMethodName() {
        return getDataMethodName;
    }

    public String getTemplateChineseName() {
        return templateChineseName;
    }

    /**
     *  根据标段类型和废标阶段筛选模板
     * @param code 模板编号字符串集合
     * @return
     */
    public static List<BidOpenTempName> listBidOpenTempName(String code) {
        List<BidOpenTempName> list = new ArrayList<>();
        for (BidOpenTempName temp : values()) {
            if (temp.getCode().contains(code)) {
                list.add(temp);
            }
        }
        return list;
    }
}
