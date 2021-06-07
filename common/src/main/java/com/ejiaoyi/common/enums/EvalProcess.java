package com.ejiaoyi.common.enums;

/**
 * @Desc: 评审环节
 * @author: Make
 * @date: 2020-9-8 10:30
 */
public enum EvalProcess {
    /**
     * 评标组长推选
     */
    IS_CHAIR_MAN(0, "isChairMan","评标组长推选"),

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
    CALC_PRICE_SCORE(6, "calcpricescore","投标报价");

    private final int code;

    /**
     * 环节名
     */
    private final String name;

    /**
     * 说明
     */
    private final String remake;

    EvalProcess(Integer code, String name, String remake) {
        this.code = code;
        this.name = name;
        this.remake = remake;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getRemake() {
        return this.remake;
    }

    public String getName() {
        return this.name;
    }
    /**
     * 根据扩展名获取type
     *
     * @param code
     */
    public static EvalProcess getCode(int code){
        for(EvalProcess evalProcess: EvalProcess.values()) {
            if(evalProcess.code == code) {
                return evalProcess;
            }
        }
        return null;
    }

    /**
     * 根据code获取remark环节名称
     *
     * @param code
     */
    public static String getRemark(int code){
        for(EvalProcess evalProcess: EvalProcess.values()) {
            if(evalProcess.code == code) {
                return evalProcess.getRemake();
            }
        }
        return null;
    }
}
