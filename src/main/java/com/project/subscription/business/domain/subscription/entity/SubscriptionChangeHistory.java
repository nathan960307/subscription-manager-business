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
public class SubscriptionChangeHistory { // 구독 변경 이력 : 구독의 상태/속성 변경 이력을 남기는 엔티티

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 변경 기록 ID

    @Column(name = "user_id", nullable = false)
    private Long userId; // 사용자 ID

    @Column(name = "subscription_id", nullable = false)
    private Long subscriptionId; // 구독 ID

    @Enumerated(EnumType.STRING)
    @Column(name = "change_type", nullable = false, length = 20)
    private ChangeType changeType;

    @Column(name = "old_value", length = 255)
    private String oldValue; // 변경 전 값

    @Column(name = "new_value", nullable = false, length = 255)
    private String newValue; // 변경 후 값

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt; // 변경 발생 시점

    @Enumerated(EnumType.STRING)
    @Column(name = "changed_by", nullable = false, length = 50)
    private ChangedBy changedBy; // SYSTEM, USER, ADMIN

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 기록 생성일

    // 도메인 메서드

    //생성
    public static SubscriptionChangeHistory create(
            Long userId,
            Long subscriptionId,
            ChangeType changeType,
            String oldValue,
            String newValue,
            ChangedBy changedBy
    ) {
        SubscriptionChangeHistory history = new SubscriptionChangeHistory();
        history.userId = userId;
        history.subscriptionId = subscriptionId;
        history.changeType = changeType;
        history.oldValue = oldValue;
        history.newValue = newValue;
        history.changedBy = changedBy;
        history.changedAt = LocalDateTime.now();
        history.createdAt = LocalDateTime.now();
        return history;
    }

}
