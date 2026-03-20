package com.project.subscription.business.repository.subscription;

import com.project.subscription.business.domain.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {

    // 사용자 별 구독 서비스 목록 조회
    List<Subscription> findByUserId(Long userId);

    // 사용자, 구독ID로 구독 entity 조회
    Optional<Subscription> findByIdAndUserId(Long subscriptionId, Long userId);

    // 삭제 여부가 false인 구독 목록 조회
    List<Subscription> findAllByUserIdAndDeletedFalse(Long userId);

    // 유저ID 와 구독ID 로 해당 구독 조회
    Optional<Subscription> findByIdAndUserIdAndDeletedFalse(Long userId, Long subscriptionId);
}
