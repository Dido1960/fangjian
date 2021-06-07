package com.ejiaoyi.common.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 监管 评标流程
 * @author : liuguoqiang
 * @date : 2020-12-7 14:35
 */
public enum EvalProcessGov {
    /**
     * 初步评审
     */
    PRELIMINARY(1, "preliminary","初步评审"),

    /**
     * 详细评审
     */
    DETAILED(2, "detailed","详细评审"),

    /**
     * 评审结果
     */
    RESULT(3, "result","评审结果"),

    /**
     * 其他评审
     */
    OTHER(4, "other","其他评审"),

    /**
     * 资格审查
     */
    QUALIFICATION(5, "qualification","资格审查"),

    /**
     * 投标报价
     */
    CALC_PRICE_SCORE(6, "calcPriceScore","投标报价"),

    /**
     * 组长推选
     */
    SELECT_LEADER(7, "selectLeader","组长推选"),

    /**
     * 推荐候选人
     */
    CANDIDATES(8, "candidates","推荐候选人");

    /**
     * 清标结果
     */
//    CLEARING_RESULTS(9, "clearingResults","清标结果");

    private final int code;

    /**
     * 环节名
     */
    private final String processName;

    /**
     * 说明
     */
    private final String remake;

    EvalProcessGov(Integer code, String processName, String remake) {
        this.code = code;
        this.processName = processName;
        this.remake = remake;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getRemake() {
        return this.remake;
    }

    public String getProcessName() {
        return this.processName;
    }

    /**
     * 根据扩展名获取type
     *
     * @param code
     */
    public static EvalProcessGov getCode(int code){
        for(EvalProcessGov evalProcess: EvalProcessGov.values()) {
            if(evalProcess.code == code) {
                return evalProcess;
            }
        }
        return null;
    }

    /**
     * 通过标段类型，顺序获取监管评标流程列表
     * @param bidProtype 标段类型
     * @param isOtherEval 是否开启其他评审（施工类需要传）
     * @return 流程列表
     */
    public static List<EvalProcessGov> listEvalProcessGovByBp(BidProtype bidProtype,Boolean isOtherEval){
        ArrayList<EvalProcessGov> result = new ArrayList<>();
        switch (bidProtype) {
            case CONSTRUCTION:
                //施工
                result.add(EvalProcessGov.SELECT_LEADER);
//                result.add(EvalProcessGov.CLEARING_RESULTS);
                result.add(EvalProcessGov.PRELIMINARY);
                result.add(EvalProcessGov.DETAILED);
                result.add(EvalProcessGov.CALC_PRICE_SCORE);
                if (isOtherEval){
                    result.add(EvalProcessGov.OTHER);
                }
                result.add(EvalProcessGov.RESULT);
                break;
            case EPC:
                //施工总承包
                result.add(EvalProcessGov.SELECT_LEADER);
                result.add(EvalProcessGov.QUALIFICATION);
                result.add(EvalProcessGov.PRELIMINARY);
                result.add(EvalProcessGov.DETAILED);
                result.add(EvalProcessGov.CALC_PRICE_SCORE);
                result.add(EvalProcessGov.RESULT);
                result.add(EvalProcessGov.CANDIDATES);
                break;
            case DESIGN:
            case INVESTIGATION:
            case QUALIFICATION:
            case SUPERVISION:
            case ELEVATOR:
                result.add(EvalProcessGov.SELECT_LEADER);
                result.add(EvalProcessGov.PRELIMINARY);
                result.add(EvalProcessGov.DETAILED);
                result.add(EvalProcessGov.RESULT);
                break;
        }
        return result;
    }
}
