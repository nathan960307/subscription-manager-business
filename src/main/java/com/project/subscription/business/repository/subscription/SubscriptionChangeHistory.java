package com.project.subscription.business.repository.subscription;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionChangeHistory extends JpaRepository<SubscriptionChangeHistory, Long> {

    // 구독 변경 내역 리스트 최신순 조회
    List<SubscriptionChangeHistory> findBySubscriptionIdOrderByChangedAtDesc(Long subscriptionId);

    // 과거순
    List<SubscriptionChangeHistory> findBySubscriptionIdOrderByChangedAtAsc(Long subscriptionId);
}
