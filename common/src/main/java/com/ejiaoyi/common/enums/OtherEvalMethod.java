package com.ejiaoyi.common.enums;

/**
 * 其他评审 grade 评标办法
 *
 * @Auther: liuguoqiang
 * @Date: 2020-12-28 11:17
 */
public enum OtherEvalMethod {
    /**
     * 互保共建
     */
    MUTUAL_PROTECTION("互保共建", 3, 1, 4, "");

    /**
     * 评分标准名称
     */
    private final String name;

    /**
     * 评审形式(0:打分;1:合格制;2扣分制；3互保共建选择制)
     */
    private final Integer type;

    /**
     * 1:加分 2:扣分
     */
    private final Integer calcType;

    /**
     * 评审环节(参照 EvalProcess)
     */
    private final Integer reviewProcess;

    /**
     * 总分
     */
    private final String score;


    OtherEvalMethod(String name, Integer type, Integer calcType, Integer reviewProcess, String score) {
        this.name = name;
        this.type = type;
        this.calcType = calcType;
        this.reviewProcess = reviewProcess;
        this.score = score;
    }

    public String getName() {
        return this.name;
    }

    public Integer getType() {
        return this.type;
    }

    public Integer getCalcType() {
        return this.calcType;
    }

    public Integer getReviewProcess() {
        return this.reviewProcess;
    }

    public String getScore() {
        return this.score;
    }


}
