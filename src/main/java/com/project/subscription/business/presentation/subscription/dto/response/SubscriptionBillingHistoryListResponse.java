package com.project.subscription.business.presentation.subscription.dto.response;

import com.project.subscription.business.presentation.subscription.dto.internal.SubscriptionBillingHistoryInternalDto;
import com.project.subscription.business.presentation.subscription.dto.internal.SubscriptionChangeHistoryInternalDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class SubscriptionBillingHistoryListResponse {
    private final String message;
    private final OffsetDateTime timestamp;
    private final List<SubscriptionBillingHistoryInternalDto> data;

    public static SubscriptionBillingHistoryListResponse success(List<SubscriptionBillingHistoryInternalDto> subscriptions) {
        return new SubscriptionBillingHistoryListResponse(
                "구독 결제 목록 조회 성공",
                OffsetDateTime.now(),
                subscriptions
        );
    }
}
