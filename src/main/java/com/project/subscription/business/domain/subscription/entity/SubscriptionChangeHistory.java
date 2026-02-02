package com.project.subscription.business.domain.subscription.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "subscription_change_history")
public class SubscriptionChangeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 변경 기록 ID

    @Column(name = "subscription_id", nullable = false)
    private Long subscriptionId; // 구독 ID

    @Column(name = "change_type", nullable = false, length = 20)
    private String changeType;
    // STATUS, PRICE, PLAN (enum 추후 추가 예정)

    @Column(name = "old_value", length = 255)
    private String oldValue; // 변경 전 값

    @Column(name = "new_value", nullable = false, length = 255)
    private String newValue; // 변경 후 값

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt; // 변경 발생 시점

    @Column(name = "changed_by", nullable = false, length = 50)
    private String changedBy; // SYSTEM, USER, ADMIN

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 기록 생성일
}
