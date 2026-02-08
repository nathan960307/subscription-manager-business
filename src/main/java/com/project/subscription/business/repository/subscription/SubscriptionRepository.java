package com.project.subscription.business.repository.subscription;

import com.project.subscription.business.domain.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {

    // 사용자 별 구독 서비스 목록 조회
    List<Subscription> findByUserId(Long userId);
}
