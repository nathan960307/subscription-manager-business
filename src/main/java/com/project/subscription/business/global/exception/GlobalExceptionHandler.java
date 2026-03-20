package com.project.subscription.business.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //순서 중요 먼저 있는 거 부터 탐

    // custom 예외 처리 핸들러
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustom(CustomException e) {

        log.warn("비즈니스 예외 발생: code={}, message={}", e.getCode(), e.getMessage());

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(Map.of(
                        "code", e.getCode(),
                        "message", e.getMessage()
                ));
    }

    // 컨트롤러에서 처리되지 않은 RuntimeException을 잡아서
    // 400 Bad Request와 메시지를 반환
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handle(RuntimeException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }


}
