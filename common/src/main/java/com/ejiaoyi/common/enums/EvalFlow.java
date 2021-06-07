package com.ejiaoyi.common.enums;

/**
 * 评标流程环节
 *
 * @author Mike
 * @since 2021/3/18
 */
public enum EvalFlow {

    /**
     * 开标结束
     */
    END_BID_OPENING(0,"开标结束"),

    /**
     * 资格审查
     */
    QUALIFICATION_REVIEW(1,"资格审查"),
    /**
     * 初步评审
     */
    PRELIMINARY_REVIEW(2,"初步评审"),
    /**
     * 详细评审
     */
    DETAIL_REVIEW(3,"详细评审"),
    /**
     * 报价得分
     */
    QUOTE_SCORE(4, "报价得分"),
    /**
     * 其他评审
     */
    OTHER_REVIEW(5, "其他评审"),
    /**
     * 评审结果
     */
    RESULT(6, "评审结果"),
    /**
     * 评标结束
     */
    END_BID_EVAL(7, "评标结束");

    private final Integer code;

    private final String showName;

    EvalFlow(Integer code, String showName) {
        this.code = code;
        this.showName = showName;
    }

    public Integer getCode(){
        return this.code;
    }

    public String getShowName(){
        return this.showName;
    }

    public static EvalFlow getEvalFlowByCode(Integer code){
        for (EvalFlow evalFlow : values()){
            if (evalFlow.getCode().equals(code)){
                return evalFlow;
            }
        }
        return null;
    }
}
