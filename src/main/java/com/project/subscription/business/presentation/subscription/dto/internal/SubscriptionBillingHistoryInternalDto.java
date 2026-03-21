package com.project.subscription.business.presentation.subscription.dto.internal;

import com.project.subscription.business.domain.subscription.entity.SubscriptionBillingHistory;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class SubscriptionBillingHistoryInternalDto {
    private Long billingHistoryId;
    private LocalDateTime billingDate;

    private LocalDateTime billingPeriodStart;
    private LocalDateTime billingPeriodEnd;

    private BigDecimal amount;
    private String status;

    // entity -> dto
    public static SubscriptionBillingHistoryInternalDto fromEntity(SubscriptionBillingHistory billingHistory) {
        return SubscriptionBillingHistoryInternalDto.builder()
                .billingHistoryId(billingHistory.getId())
                .billingDate(billingHistory.getBillingDate())
                .billingPeriodStart(billingHistory.getBillingPeriodStart())
                .billingPeriodEnd(billingHistory.getBillingPeriodEnd())
                .amount(billingHistory.getAmount())
                .status(billingHistory.getStatus())
                .build();
    }

}
