package com.project.subscription.business.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // 200번대: 성공
    OK(200, 2000, "성공"),
    CREATED(201, 2010, "생성 완료"),
    ACCEPTED(202, 2020, "요청 수락됨"),
    NO_CONTENT(204, 2030, "내용 없음"),

    // 400번대: 클라이언트 오류
    BAD_REQUEST(400, 4000, "잘못된 요청"),
    UNAUTHORIZED(401, 4010, "권한 없음"),
    LOGIN_FAILED(401, 4011, "로그인 실패"),
    INVALID_REFRESH_TOKEN(401, 4012, "유효하지 않은 refresh token"),
    INVALID_TOKEN(401, 4013, "유효하지 않은 token"),
    TOO_MANY_REQUESTS(429, 4290, "요청 과다 또는 중복 요청"),
    FORBIDDEN(403, 4030, "거부됨"),
    NOT_FOUND(404, 4040, "리소스를 찾을 수 없음"),
    USER_NOT_FOUND(404, 4041, "사용자를 찾을 수 없습니다"),
    SUBSCRIPTION_NOT_FOUND(404, 4042, "구독을 찾을 수 없습니다"),
    DUPLICATE_EMAIL(409, 4091, "이미 존재하는 이메일입니다."),

    // 500번대: 서버 오류
    INTERNAL_SERVER_ERROR(500, 5000, "서버 내부 오류"),
    NOT_IMPLEMENTED(501, 5010, "구현되지 않음"),
    BAD_GATEWAY(502, 5020, "잘못된 게이트웨이"),
    SERVICE_UNAVAILABLE(503, 5030, "서비스 사용 불가"),
    GATEWAY_TIMEOUT(504, 5040, "게이트웨이 시간 초과"); // 504 Gateway Timeout

    private final int httpStatus; // HTTP 상태코드
    private final int code; // 내부 에러코드
    private final String message;

    ErrorCode(int httpStatus, int code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

}
