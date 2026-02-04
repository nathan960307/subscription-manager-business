package com.project.subscription.business.presentation.subscription.dto.response;

import com.project.subscription.business.presentation.subscription.dto.internal.SubscriptionInternalDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class SubscriptionListResponse { // 구독 서비스 목록 출력용

    private final String message;
    private final OffsetDateTime timestamp;
    private final List<SubscriptionInternalDto> data;

    public static SubscriptionListResponse success(List<SubscriptionInternalDto> subscriptions) {
        return new SubscriptionListResponse(
                "구독 목록 조회 성공",
                OffsetDateTime.now(),
                subscriptions
        );
    }
}
