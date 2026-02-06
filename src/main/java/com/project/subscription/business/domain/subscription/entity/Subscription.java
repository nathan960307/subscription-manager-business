package com.project.subscription.business.domain.subscription.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "subscription")
public class Subscription { // 사용자의 현재 구독 상태를 나타내는 테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 구독 ID

    @Column(name = "user_id", nullable = false)
    private Long userId; // 사용자 ID

    @Column(name = "service_id", nullable = false)
    private Long serviceId; // 구독 서비스 ID

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price; // 구독 가격

    @Column(name = "billing_cycle", nullable = false)
    private String billingCycle; // 결제 주기
    // MONTHLY, YEARLY (enum은 나중)

    @Column(name = "next_billing_date", nullable = false)
    private LocalDateTime nextBillingDate; // 다음 결제일

    @Column(nullable = false)
    private String status; // 구독 상태
    // ACTIVE, CANCELED, PAUSED (enum은 나중)

    @Column(name = "auto_renew", nullable = false)
    private boolean autoRenew = true; // 자동 갱신 여부

    @Column(name = "trial_end_date")
    private LocalDateTime trialEndDate; // 무료 체험 종료일

    @Column(name = "first_paid_at")
    private LocalDateTime firstPaidAt; // 첫 유료 결제

    @Column(name = "converted_to_paid_at")
    private LocalDateTime convertedToPaidAt; // 유료 전환 시점

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 구독 등록일

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt; // 수정일

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt; // 구독 취소일
}
