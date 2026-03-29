package com.project.subscription.business.presentation.subscription.dto.internal;

import com.project.subscription.business.domain.subscription.entity.Subscription;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class MonthlyBillingItemDto {

    private Long serviceId;
    private BigDecimal price;
    private LocalDateTime startDate; // 이번 달 결제 예정 일자

    public static MonthlyBillingItemDto fromEntity(Subscription subscription) {
        return MonthlyBillingItemDto.builder()
                .serviceId(subscription.getServiceId())
                .price(subscription.getPrice())
                .startDate(subscription.getStartDate())
                .build();
    }
}
