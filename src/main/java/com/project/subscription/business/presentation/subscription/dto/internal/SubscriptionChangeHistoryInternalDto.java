package com.project.subscription.business.presentation.subscription.dto.internal;

import com.project.subscription.business.domain.subscription.entity.SubscriptionChangeHistory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SubscriptionChangeHistoryInternalDto {
    private Long id; // 변경 기록 ID
    private Long userId;
    private Long subscriptionId; // 구독 ID
    private String changeType;
    private String oldValue; // 변경 전 값
    private String newValue; // 변경 후 값
    private LocalDateTime changedAt; // 변경 발생 시점
    private String changedBy; // SYSTEM, USER, ADMIN
    private LocalDateTime createdAt; // 기록 생성일

    // entity -> dto
    public static SubscriptionChangeHistoryInternalDto fromEntity(SubscriptionChangeHistory subscriptionChangeHistory) {
        return SubscriptionChangeHistoryInternalDto.builder()
                .id(subscriptionChangeHistory.getId())
                .userId(subscriptionChangeHistory.getUserId())
                .subscriptionId(subscriptionChangeHistory.getSubscriptionId())
                .changeType(subscriptionChangeHistory.getChangeType())
                .oldValue(subscriptionChangeHistory.getOldValue())
                .newValue(subscriptionChangeHistory.getNewValue())
                .changedAt(subscriptionChangeHistory.getChangedAt())
                .changedBy(subscriptionChangeHistory.getChangedBy())
                .createdAt(subscriptionChangeHistory.getCreatedAt())
                .build();
    }


}
