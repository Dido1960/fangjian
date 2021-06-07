package com.ejiaoyi.common.enums;

/**
 * 评标报告模板 每个环节对应名称
 * @author fengjunhong
 */
public enum TemplateNameEnum {
    /**
     * 评标报告封面
     */
    COVER_REPORT("coverReport",false,"getCoverReportData", "封面"),

    /**
     * 专家签到表
     */
    EXPERT_SING_IN( "expertSignIn",true,null, "专家签到表"),

    /**
     * 专家承诺书
     */
     EXPERT_PROMISE( "expertPromise",false,null, "专家承诺书"),

    // 资格审查
    QUALIFICATION_REVIEW( "qualificationReviewTable",true,"getQualificationReviewDataMap", "资格审查"),

    /**
     * 初步评审表 （投标文件废标情况评审表）
     */
    PRELIMINARY_EVALUATION( "preliminaryEvaluation",true,"getFirstStepDataMap", "初步评审表"),

    /**
     * 合格投标人名单
     */
    QUALIFIED_BIDDERS( "qualifiedBiddersForm",true,"getQuoteScoreDataMap", "合格投标人名单"),

    /**
     * 商务评审表
     */
    SHANG_WU_EVALUATION_TABLE( "shangWuEvaluationTable",true,"getShangWuReviewDataMap", "商务评审表"),

    /**
     * 小组 技术评审表
     */
    JI_SHU_GROUP_EVALUATION_TABLE( "jiShuGroupTable",true,"getJiShuGroupReviewDataMap", "技术评审表"),

    /**
     * 个人 技术评审表
     */
    JI_SHU_PERSON_EVALUATION_TABLE( "jiShuPersonTable",true,"getJiShuGroupReviewDataMap", "技术评审评审明细表"),

    /**
     * 推荐中标候选人名单
     */
    PUSH_WIN_BIDDERS( "pushWinBidders",true,"getDetailReviewDataMap", "推荐中标候选人名单"),

    /**
     * 评委推荐表
     */
    PUSH_WIN_BIDDERS_SINGLE("pushWinBiddersSingle",true,"getExpertPersonTuiJiForm", "评委推荐表"),

    /**
     * 评委投票统计表
     */
    VOTE_COUNT_FORM( "voteCountForm",true,"getVoteCountForm", "评委投票统计表"),
    /**
     * 详细评审表
     */
    DETAILED_REVIEW( "detailedReview",true,"getDetailReviewDataMap", "详细评审表"),

    /**
     * 详细评审专家个人表
     */
    DETAILED_PERSON_REVIEW("detailedPersonReview",true,"getDetailPersonReviewDataMap", "详细评审专家个人表"),

    /**
     * 报价得分表
     */
     QUOTE_SCORE("quoteScore",true,"getQuoteScoreDataMap", "报价得分表"),

    /**
     * 报价得分修正表
     */
    QUOTE_SCORE_UPDATE("quoteScoreUpdate",true,"getQuoteScoreUpdateDataMap", "报价得分修正表"),

    /**
     * 评标得分汇总表
     */
    EVALUATION_SCORE( "evaluationScore",true,"getEvaluationScoreDataMap", "评标得分汇总表"),

    /**
     * 资格预审评审汇总表
     */
    PREQUALIFICATION_REVIEW_SUMMARY( "prequalificationReviewSummary",true,"getReviewSummaryDataMap", "资格预审评审汇总表"),

    /**
     * 评审意见汇总表
     */
    REVIEW_OPINION("reviewOpinion",true,"getReviewOpinionDataMap", "评审意见汇总表"),

    /**
     * 评审意见个人表
     */
    REVIEW_PERSON_OPINION("reviewPersonlOpinion",true,"getReviewOpinionDataMap","评审意见个人表"),

    /**
     * 评审工作履职情况记录表
     */
    PERFORMANCE_DUTIES_FORM( "performanceDutiesForm",true,"getExpertUsersNoOwner", "评审工作履职情况记录表"),

    /**
     * 互保共建
     */
    MUTUAL_PROTECTION("mutualSecurity",true,"getEvaluationScoreDataMap","互保共建"),

    // ============================复会报告=========start======================
    /**
     * 废标投标人列表
     */
    NO_PASS_BIDDERS_FORM("noPassBiddersForm",false,"getNoPassBiddersDataMap","否决投标人名单"),

    /**
     * 各种不同类型返回的不同
     * 如：设计类，显示的是推选结果；其他是评标结果
     */
    BID_EVAL_RESULT("bidEvalResultForm",false,"getBidderEvalResultDataMap","评标结果")
    // ============================复会报告==========end=======================
    ;

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

    TemplateNameEnum(String name, Boolean levelPrint, String getDataMethodName, String templateChineseName) {
        this.name = name;
        this.levelPrint = levelPrint;
        this.getDataMethodName = getDataMethodName;
        this.templateChineseName = templateChineseName;
    }

    public String getName() {
        return this.name;
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


}
