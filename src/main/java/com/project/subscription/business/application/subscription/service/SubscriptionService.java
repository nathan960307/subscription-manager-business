package com.project.subscription.business.application.subscription.service;

import com.project.subscription.business.domain.subscription.entity.Subscription;
import com.project.subscription.business.presentation.subscription.dto.internal.SubscriptionInternalDto;
import com.project.subscription.business.repository.subscription.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository; // 구독 repository

    public SubscriptionInternalDto getSubscriptionDetail(Long subscriptionId) { // 구독 서비스 단일 조회
        // 구독 ID 로 해당 서비스 상세 조회
        Subscription subscription =subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("구독이 존재하지 않습니다."));

        // entity -> dto 변환
        // 1. 생성자 방식
        // 2. setter 방식
        // 3. 빌더 방식

        SubscriptionInternalDto subscriptionInternalDto = SubscriptionInternalDto.fromEntity(subscription);

        return subscriptionInternalDto;
    }

    public List<SubscriptionInternalDto> getMySubscriptions(Long userId) { // 구독 서비스 목록 조회

        // 사용자별 구독 서비스 목록 조회
        List<Subscription> subscriptions =subscriptionRepository.findByUserId(userId);

        // entity -> dto 변환
        List<SubscriptionInternalDto> subscriptionInternalDtos = SubscriptionInternalDto.fromEntities(subscriptions);

        return subscriptionInternalDtos;
    }



}
