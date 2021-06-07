package com.ejiaoyi.common.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 评审类型
 * @Auther: liuguoqiang
 * @Date: 2020-11-17 15:57
 */
public enum ReviewType {
    /**
     * 基础合格制
     */
    BASIC_QUALIFICATION(1, ""),

    /**
     * 基础扣分制（只选择扣分不扣分）
     */
    BASIC_DEDUCT(2, ""),

    /**
     * 监理评审方法： 商务标（打分形式）
     */
    BUSINESS_STANDARD(3, "商务标"),

    /**
     * 监理评审方法： 技术标（打分形式）
     */
    TECHNICAL_STANDARD(4, "技术标"),

    /**
     * 监理评审方法： 违章行为（打分形式）
     */
    VIOLATION(5, "违章行为"),

    /**
     * 基础打分制
     */
    BASIC_SCORE(6, ""),

    /**
     * 推选投标人
     */
    SELECTION_BIDDERS(7, "");

    /**
     * 编码
     */
    private final int code;

    /**
     * 名称
     */
    private final String textName;

    /**
     * 总分
     */
    private String gradeScore;

    ReviewType(Integer code, String textName) {
        this.code = code;
        this.textName = textName;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getTextName() {
        return this.textName;
    }

    public String getGradeScore() {
        return this.gradeScore;
    }

    public void setGradeScore(String score) {
        this.gradeScore = score;
    }

    /**
     * 根据扩展名获取type
     *
     * @param code 对应code
     */
    public static ReviewType getCode(int code){
        for(ReviewType reviewType: ReviewType.values()) {
            if(reviewType.code == code) {
                return reviewType;
            }
        }
        return null;
    }

    /**
     * 获取监理详细评审的评审类型列表
     * @return 获取监理详细评审的评审类型列表
     */
    public static List<ReviewType> listSupervisionType(){
        ArrayList<ReviewType> reviewTypes = new ArrayList<>();
        reviewTypes.add(ReviewType.BUSINESS_STANDARD);
        reviewTypes.add(ReviewType.TECHNICAL_STANDARD);
        reviewTypes.add(ReviewType.VIOLATION);
        return reviewTypes;
    }

}
