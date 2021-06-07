package com.ejiaoyi.common.enums;

public enum ExecuteCode {
    /**
     * 成功
     */
    SUCCESS(1),

    /**
     * 失败
     */
    FAIL(0);

    private Integer code;

    ExecuteCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }

    public static ExecuteCode getExecuteCode(Integer code) {
        for(ExecuteCode executeCode: ExecuteCode.values()) {
            if(executeCode.getCode().equals(code)) {
                return executeCode;
            }
        }

        return null;
    }
}