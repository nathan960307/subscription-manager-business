package com.project.subscription.business.presentation.subscription.dto.internal;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class SubscriptionSummaryInternalDto {

    //필드
    private int totalCount; // 구독 서비스 갯수
    private BigDecimal totalAmount; // 금월 예상 결제 내역
    private List<MonthlyBillingItemDto> monthlyBillingItems;

}
