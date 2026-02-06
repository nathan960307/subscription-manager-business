package com.project.subscription.business.application.subscription.service;

import com.project.subscription.business.domain.subscription.entity.Subscription;
import com.project.subscription.business.domain.subscription.entity.SubscriptionBillingHistory;
import com.project.subscription.business.domain.subscription.entity.SubscriptionChangeHistory;
import com.project.subscription.business.presentation.subscription.dto.internal.SubscriptionBillingHistoryInternalDto;
import com.project.subscription.business.presentation.subscription.dto.internal.SubscriptionChangeHistoryInternalDto;
import com.project.subscription.business.presentation.subscription.dto.internal.SubscriptionInternalDto;
import com.project.subscription.business.repository.subscription.SubscriptionBillingHistoryRepository;
import com.project.subscription.business.repository.subscription.SubscriptionChangeHistoryRepository;
import com.project.subscription.business.repository.subscription.SubscriptionRepositoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepositoryRepository subscriptionRepository; // 구독 repository
    private final SubscriptionChangeHistoryRepository subscriptionChangeHistoryRepository; // 구독 변경 내역 repository
    private final SubscriptionBillingHistoryRepository subscriptionBillingHistoryRepository; // 구독 결제 내역 repository

    // 구독 서비스 단일 조회
    public SubscriptionInternalDto getSubscriptionDetail(Long subscriptionId) {
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

    // 구독 서비스 목록 조회
    public List<SubscriptionInternalDto> getMySubscriptions(Long userId) {

        // 사용자별 구독 서비스 목록 조회
        List<Subscription> subscriptions =subscriptionRepository.findByUserId(userId);

        // entity -> dto 변환
        List<SubscriptionInternalDto> subscriptionInternalDtos = SubscriptionInternalDto.fromEntities(subscriptions);

        return subscriptionInternalDtos;
    }

    // 구독 서비스 변경 내역 조회
    public List<SubscriptionChangeHistoryInternalDto> getMySubscriptionChangeHistories(Long userId, Long subscriptionId) {

        // 사용자,구독 별 변경 내역 조회
        List<SubscriptionChangeHistory> subscriptionChangeHistories =subscriptionChangeHistoryRepository.findBySubscriptionIdAndUserIdOrderByChangedAtDesc(userId,subscriptionId);

        // entity -> dto 변환
        List<SubscriptionChangeHistoryInternalDto> subscriptionChangeHistoryInternalDtos = SubscriptionChangeHistoryInternalDto.fromEntities(subscriptionChangeHistories);

        return subscriptionChangeHistoryInternalDtos;
    }

    // 구독 서비스 결제 내역 조회
    public List<SubscriptionBillingHistoryInternalDto> getMySubscriptionBillingHistories(Long userId, Long subscriptionId) {

        // 사용자,구독 별 결제 내역 조회
        List<SubscriptionBillingHistory> subscriptionChangeHistories =subscriptionBillingHistoryRepository.findByUserIdAndSubscriptionIdOrderByBillingDateDesc(userId,subscriptionId);

        // entity -> dto 변환
        List<SubscriptionBillingHistoryInternalDto> subscriptionBillingHistoryInternalDtos = SubscriptionBillingHistoryInternalDto.fromEntities(subscriptionChangeHistories);

        return subscriptionBillingHistoryInternalDtos;
    }



}
