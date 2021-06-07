package com.ejiaoyi.common.constant;

/**
 * 评标分数信息  xml节点 及属性
 * @Auther: liuguoqiang
 * @Date: 2020-11-17 15:44
 */
public interface EvalMethodConstant {

    /*******************************方法根节点*******************************/

    /**
     * 评标办法根节点
     */
    String BID_EVALUATION_METHOD = "BidEvaluationMethod";

    /******************************具体方法节点*******************************/
    /**
     * 具体方法节点
     */
    String EVALUATION_FACTOR = "EvaluationFactor";

    /******************************详细方法ITEM节点*******************************/

    /**
     * 详细方法ITEM节点
     */
    String EVALUATION_STANDARD = "EvaluationStandard";
    /******************************具体方法节点 属性*******************************/

    /**
     * 标题
     */
    String TITLE = "title";

    /**
     * 总分值
     */
    String FACTOR_SCORE = "factorscore";

    /**
     * 打分方式 参照 ScoreType
     */
    String SCORE_METHOD = "scoremethod";

    /**
     * 评审流程 参照 EvalProcess
     */
    String REVIEW_PROCESS = "reviewprocess";

    /**
     * 评审类型 参照 ReviewType
     */
    String REVIEW_TYPE = "reviewtype";

    /**
     * 备注
     */
    String REMARK = "remark";

    /**
     * 每增加1%，扣分
     */
    String E1 = "E1";

    /**
     * 每减少1%，扣分
     */
    String E2 = "E2";
    /******************************详细方法ITEM节点 属性*******************************/

    /**
     * 方法描述
     */
    String DESCRIBE = "describe";

    /**
     * 方法总分
     */
        String SCORE = "score";

    /**
     * 打分方式 参考ItemScoreType
     */
    String SCORE_TYPE = "scoretype";

    /**
     * 具体打分点
     */
    String SCORE_RANGE = "scorerange";
}
