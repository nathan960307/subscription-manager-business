package com.project.subscription.business.presentation.subscription.dto.internal;

import com.project.subscription.business.domain.subscription.entity.Subscription;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class SubscriptionInternalDto { //구독 서비스 단일 조회 DTO ( entity -> response dto 변환용 내부 DTO)

    private Long id;
    private Long userId;
    private Long serviceId;
    private BigDecimal price;
    private String billingCycle;
    private String status;
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
                .status(subscription.getStatus())
                .nextBillingDate(subscription.getNextBillingDate())
                .autoRenew(subscription.isAutoRenew())
                .build();
    }

    // List<entity> -> List<dto>
    public static List<SubscriptionInternalDto> fromEntities(List<Subscription> subscriptions) {
        return subscriptions.stream()
                .map(SubscriptionInternalDto::fromEntity)
                .toList();
    }
}
