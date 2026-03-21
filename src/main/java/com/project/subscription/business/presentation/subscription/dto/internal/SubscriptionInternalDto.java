package com.project.subscription.business.presentation.subscription.dto.internal;

import com.project.subscription.business.domain.subscription.entity.BillingCycle;
import com.project.subscription.business.domain.subscription.entity.Subscription;
import com.project.subscription.business.domain.subscription.entity.SubscriptionStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class SubscriptionInternalDto { //구독 서비스 단일 조회 DTO ( entity -> response dto 변환용 내부 DTO)

    private Long id;
    private Long userId;
    private Long serviceId;
    private BigDecimal price;
    private BillingCycle billingCycle;
    private SubscriptionStatus subscriptionStatus;
    private LocalDateTime nextBillingDate;
    private boolean autoRenew;

    // entity -> dto
    public static SubscriptionInternalDto fromEntity(Subscription subscription) {
        return SubscriptionInternalDto.builder()
                .id(subscription.getId())
                .userId(subscription.getUserId())
                .serviceId(subscription.getServiceId())
                .price(subscription.getPrice())
                .billingCycle(subscription.getBillingCycle())
                .subscriptionStatus(subscription.getSubscriptionStatus())
                .nextBillingDate(subscription.getNextBillingDate())
                .autoRenew(subscription.isAutoRenew())
                .build();
    }
}
