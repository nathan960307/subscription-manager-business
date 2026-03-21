package com.project.subscription.business.presentation.subscription.dto.internal;

import com.project.subscription.business.domain.external.entity.ExternalPayment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExternalPaymentDto {

    private Long userId;
    private String provider;
    private String transactionId;
    private String merchantName;
    private BigDecimal amount;
    private String currency;
    private String transactionType;
    private LocalDateTime transactionDate;

    public static ExternalPaymentDto fromEntity(ExternalPayment entity) {
        return ExternalPaymentDto.builder()
                .userId(entity.getUserId())
                .provider(entity.getProvider())
                .transactionId(entity.getTransactionId())
                .merchantName(entity.getMerchantName())
                .amount(entity.getAmount())
                .currency(entity.getCurrency())
                .transactionType(entity.getTransactionType())
                .transactionDate(entity.getTransactionDate())
                .build();
    }
}
