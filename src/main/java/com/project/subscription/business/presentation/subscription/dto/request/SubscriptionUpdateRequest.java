package com.project.subscription.business.presentation.subscription.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class SubscriptionUpdateRequest {

    private BigDecimal price; // 구독 가격
    private String billingCycle; // 주기
}
