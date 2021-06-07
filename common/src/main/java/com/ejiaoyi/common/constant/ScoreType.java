package com.ejiaoyi.common.constant;

/**
 * 打分方式
 *
 * @author Make
 * @date 2020-7-9 13:30
 */
public interface ScoreType {
    /**
     * 合格制
     */
    String QUALIFIED = "qualified";

    /**
     * 打分制
     */
    String ACCUMULATE = "accumulate";

    /**
     * 扣分制
     */
    String DEDUCT = "deduct";

    /**
     * 扣分打分制
     */
    String ACCUMULATE_DEDUCT = "accumulateDeduct";
}
