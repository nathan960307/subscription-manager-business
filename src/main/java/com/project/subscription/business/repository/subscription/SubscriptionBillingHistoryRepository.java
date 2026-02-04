package com.project.subscription.business.repository.subscription;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 구독 결제 이력 조회
public interface SubscriptionBillingHistoryRepository extends JpaRepository<SubscriptionBillingHistoryRepository,Long> {

    // 특정 구독의 모든 Billing History를 조회 하여 날짜 순으로 내림차순으로 정렬(최신순)
    List<SubscriptionBillingHistoryRepository> findBySubscriptionIdOrderByBillingDateDesc(Long subscriptionId);

    // 과거순(오름차순) 조회
    List<SubscriptionBillingHistoryRepository> findBySubscriptionIdOrderByBillingDateAsc(Long subscriptionId);


}
