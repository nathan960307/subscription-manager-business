package com.project.subscription.business.domain.subscription.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "subscription_billing_history")
public class SubscriptionBillingHistory { // 구독 결제 이력 테이블 : 구독에 대한 결제 이력을 기록하는 테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 결제 기록 ID

    @Column(name = "user_id", nullable = false)
    private Long userId; // 사용자 ID

    @Column(name = "subscription_id", nullable = false)
    private Long subscriptionId; // 구독 ID (1:N, ID만 보관)

    @Column(name = "billing_period_start", nullable = false)
    private LocalDateTime billingPeriodStart; // 청구 기간 시작일

    @Column(name = "billing_period_end", nullable = false)
    private LocalDateTime billingPeriodEnd; // 청구 기간 종료일

    @Column(name = "billing_date", nullable = false)
    private LocalDateTime billingDate; // 실제 결제 시도일

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount; // 결제 금액

    @Column(nullable = false)
    private String status;
    // SUCCESS, FAILED, REFUNDED (enum은 나중)

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 기록 생성일
}
