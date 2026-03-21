package com.project.subscription.business.global.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final int httpStatus;
    private final int code;

    // ErrorCode로 상태와 메시지 받음
    public CustomException(ErrorCode code) {
        super(code.getMessage());
        this.httpStatus = code.getHttpStatus();
        this.code = code.getCode();
    }
}
