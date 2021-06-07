package com.ejiaoyi.expert.enums;

/**
 * 专家推送消息 类型
 */
public enum ExpertUserMsgStatus {
    /**
     * 环节重评
     */
    RE_EVALUATION(1, "环节重评"),

    /**
     * 小组结束
     */
    GROUP_END(2, "小组评审已结束");

    private final Integer code;
    private final String remark;

    ExpertUserMsgStatus(Integer code, String remark) {
        this.code = code;
        this.remark = remark;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getRemark() {
        return this.remark;
    }

    public static ExpertUserMsgStatus getStatus(Integer code) {
        for (ExpertUserMsgStatus enabled : ExpertUserMsgStatus.values()) {
            if (enabled.getCode().equals(code)) {
                return enabled;
            }
        }

        return null;
    }
}