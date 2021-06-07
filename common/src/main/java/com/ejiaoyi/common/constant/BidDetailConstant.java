package com.ejiaoyi.common.constant;

/**
 * 招标项目详细信息  xml节点 及属性
 *
 * @Auther: liuguoqiang
 * @Date: 2020-11-17 14:45
 */
public interface BidDetailConstant {
    /**
     * 投标工具版本号
     */
    String VERSION = "Version";

    /**
     * 项目编号
     */
    String PROJECT_CODE = "ProjectCode";

    /**
     * 项目名称
     */
    String PROJECT_NAME = "ProjectName";

    /**
     * 招标项目编号
     */
    String TENDER_PROJECT_CODE = "TenderProjectCode";

    /**
     * 招标项目编号
     */
    String TENDER_PROJECT_NAME = "TenderProjectName";

    /**
     * 标段名称
     */
    String SECT_NAME = "SectName";

    /**
     * 标段编码
     */
    String SECT_CODE = "SectCode";

    /**
     * 项目交易平台
     */
    String TRADING_PLATFORM = "TradingPlatform";

    /**
     * 招标人
     */
    String CO_NAME_INV = "CoName_Inv";

    /**
     * 招标代理机构
     */
    String IPB_NAME = "IPBName";

    /**
     * 招标代理组织机构代码
     */
    String IPB_CODE = "IPBCode";

    /**
     * 招标代表人数
     */
    String TENDER_REPRE = "TenderRepre";

    /**
     * 评标委员会总人数
     */
    String EXPERT = "Expert";

    /**
     * 投标文件递交截止时间
     */
    String BID_DOC_REFER_END_TIME = "BidDocReferEndTime";

    /**
     * 开标时间
     */
    String BID_OPEN_TIME = "BidOpenTime";

    /**
     * 投标保证金
     */
    String MARGIN_AMOUNT = "MarginAmount";

    /**
     * 资格审查方式
     */
    String QUALIFICATION_TYPE = "QualificationType";

    /**
     * 招标方式
     */
    String BID_METHOD = "BidMethod";

    /**
     * 合同价款方式
     */
    String CONTRACT_METHOD = "ContractMethod";

    /**
     * 评标办法
     */
    String BID_EVAL_METHOD = "BidEvalMethod";

    /**
     * 计价方式
     */
    String CALC_METHOD = "CalcMethod";

    /*******************************子节点*******************************/

    /**
     * 节点子项
     */
    String ITEM = "item";
    /*******************************属性值*******************************/

    /**
     * 标题
     */
    String TITLE = "title";

    /**
     * 提示
     */
    String TIP = "tip";

    /**
     * 提示
     */
    String WATERTEXT = "watertext";

    /**
     * 属性值
     */
    String VALUE = "value";

    /**
     * 类型
     */
    String TYPE = "type";

    /**
     * 可见
     */
    String VISIBLE = "visible";

    /**
     * 类型
     */
    String CLASSTYPE = "classtype";
}
