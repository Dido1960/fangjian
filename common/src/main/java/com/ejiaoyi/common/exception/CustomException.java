package com.ejiaoyi.common.exception;

/**
 * 自定义运行时异常
 *
 * @author Z0001
 * @since 2020-03-17
 */
public class CustomException extends RuntimeException {

    public CustomException(String message) {
        super(message);
    }
}
