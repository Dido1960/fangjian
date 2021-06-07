package com.ejiaoyi.common.enums;

/**
 * 状态值枚举类
 */
public enum Status {

    /**
     * 未开始
     */
    NOT_START(0),

    /**
     * 进行中
     */
    PROCESSING(1),

    /**
     * 结束
     */
    END(2),

    /**
     * 请求成功
     */
    SUCCESS(200);

    private Integer code;

    Status(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }

    public static Status getStatusCode(Integer code) {
        for(Status status: Status.values()) {
            if(status.getCode().equals(code)) {
                return status;
            }
        }

        return null;
    }
}
