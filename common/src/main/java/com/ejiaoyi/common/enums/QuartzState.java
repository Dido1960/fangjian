package com.ejiaoyi.common.enums;

/**
 * Quartz定时任务状态 枚举类
 *
 * @author Z0001
 * @since 2020-05-20
 */
public enum QuartzState {
    // 暂停
    PAUSED("paused"),
    // 执行中
    RUNNING("running"),
    // 执行完成
    COMPLETED("completed");

    private final String state;

    public String getState() {
        return this.state;
    }

    QuartzState(String state) {
        this.state = state;
    }
}
