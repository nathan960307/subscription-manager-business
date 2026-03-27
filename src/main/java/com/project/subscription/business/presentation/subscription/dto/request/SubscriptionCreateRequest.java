package com.project.subscription.business.presentation.subscription.dto.request;

import com.project.subscription.business.domain.subscription.entity.BillingCycle;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class SubscriptionCreateRequest {

    @NotBlank
    private String serviceName; // 구독 서비스 이름

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price; // 구독 가격

    @NotNull
    private BillingCycle billingCycle; // 주기

    private LocalDateTime startDate; // 시작일
}
