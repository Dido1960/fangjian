package com.ejiaoyi.common.enums;

/**
 * 标段类型
 *
 * @author Make
 * @since 2020/7/9
 */
public enum BidProtype {

    /**
     * 勘察
     */
    INVESTIGATION("A03", "Investigation","勘察","survBidOpenRecord","KC"),
    /**
     * 设计
     */
    DESIGN("A04", "Design","设计","desiBidOpenRecord","SJ"),
    /**
     * 监理
     */
    SUPERVISION("A05", "Supervision","监理","manaBidOpenRecord","JL"),
    /**
     * 施工
     */
    CONSTRUCTION("A08", "Constuction" ,"施工","consBidOpenRecord","SG"),
    /**
     * 资格预审
     */
    QUALIFICATION("A10", "Qualification","资格预审","preqBidOpenRecord","ZGYS"),
    /**
     * 电梯采购与安装
     */
    ELEVATOR("A11", "Eevator","电梯采购与安装","elevBidOpenRecord","DT"),
    /**
     * 施工总承包
     */
    EPC("A12", "EPC","施工总承包","epcBidOpenRecord","EPC");

    private final String code;

    private final String name;

    private final String chineseName ;

    private final String template;

    private final String templateDir;

    BidProtype(String code, String name,String chineseName,String template,String templateDir) {
        this.code = code;
        this.name = name;
        this.chineseName = chineseName;
        this.template = template;
        this.templateDir = templateDir;
    }

    public static BidProtype getBidProtypeByCode(String code){
        for (BidProtype bidProtype : values()) {
            if (bidProtype.getCode().equals(code)){
                return bidProtype;
            }
        }
        return null;
    }

    /**
     * 返回标段类型代码
     * @return
     */
    public String getCode() {
        return this.code;
    }

    /**
     * 返回标段类型名称
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * 返回标段类型中文名称
     * @return
     */
    public String getChineseName() {
        return this.chineseName;
    }

    /**
     * 返回标段类型模板名称
     * @return
     */
    public String getTemplate() {
        return this.template;
    }

    /**
     * 返回当前类型模板目录名称
     * @return
     */
    public String getTemplateDir() {
        return templateDir;
    }

    public static BidProtype getBidProtypeName(String code){
        for (BidProtype bidProtype : values()) {
            if (bidProtype.getCode().equals(code)){
                return bidProtype;
            }
        }
        return null;
    }

    /**
     * 返回标段分类中文名称
     * @param code
     * @return
     */
    public static String getProtypeChineseName(String code){
        for (BidProtype bidProtype : values()) {
            if (bidProtype.getCode().equals(code)){
                return bidProtype.getChineseName();
            }
        }
        return null;
    }

    /**
     * 返回标段分类模板名称
     * @param code
     * @return
     */
    public static String getTemplate(String code){
        for (BidProtype bidProtype : values()) {
            if (bidProtype.getCode().equals(code)){
                return bidProtype.getTemplate();
            }
        }
        return null;
    }
}
