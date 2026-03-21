package com.project.subscription.business.presentation.subscription.dto.request;

import com.project.subscription.business.domain.subscription.entity.BillingCycle;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class SubscriptionCreateRequest {

    private Long serviceId; // 서비스 ID
    private String serviceName; // 구독 서비스 이름
    private BigDecimal price; // 구독 가격
    private BillingCycle billingCycle; // 주기
}
