package com.project.subscription.business.presentation.subscription.dto.response;

import com.project.subscription.business.presentation.subscription.dto.internal.SubscriptionInternalDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class SubscriptionDetailResponse{ // 구독 서비스 상세 출력용

    private final String message;
    private final OffsetDateTime timestamp;
    private final SubscriptionInternalDto data;

    public static SubscriptionDetailResponse success(SubscriptionInternalDto subscriptionInternalDto) {

        return new SubscriptionDetailResponse(
                "구독 상세 조회 성공",
                OffsetDateTime.now(),
                subscriptionInternalDto
        );
    }
}
