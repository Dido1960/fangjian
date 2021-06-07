package com.ejiaoyi.common.enums;

/**
 * 状态
 *
 * @author fengjunhong
 * @since 2020/5/19
 */
public enum StatusEnum {

    //等待
    WAIT(0),

    //进行中
    RUNNING(3),

    // 成功
    SUCCESS(1),

    // 失败
    ERROR(2);

    private Integer status;

    StatusEnum(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
