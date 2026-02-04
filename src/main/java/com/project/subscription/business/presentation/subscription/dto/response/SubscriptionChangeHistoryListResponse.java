package com.project.subscription.business.presentation.subscription.dto.response;

import com.project.subscription.business.presentation.subscription.dto.internal.SubscriptionChangeHistoryInternalDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class SubscriptionChangeHistoryListResponse {
    private final String message;
    private final OffsetDateTime timestamp;
    private final List<SubscriptionChangeHistoryInternalDto> data;

    public static SubscriptionChangeHistoryListResponse success(List<SubscriptionChangeHistoryInternalDto> subscriptions) {
        return new SubscriptionChangeHistoryListResponse(
                "구독 변경 목록 조회 성공",
                OffsetDateTime.now(),
                subscriptions
        );
    }
}
