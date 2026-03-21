package com.project.subscription.business.application.subscription.service;

import com.project.subscription.business.domain.subscription.entity.Subscription;
import com.project.subscription.business.domain.subscription.entity.SubscriptionBillingHistory;
import com.project.subscription.business.domain.subscription.entity.SubscriptionChangeHistory;
import com.project.subscription.business.presentation.subscription.dto.internal.SubscriptionBillingHistoryInternalDto;
import com.project.subscription.business.presentation.subscription.dto.internal.SubscriptionChangeHistoryInternalDto;
import com.project.subscription.business.presentation.subscription.dto.internal.SubscriptionInternalDto;
import com.project.subscription.business.presentation.subscription.dto.request.SubscriptionCreateRequest;
import com.project.subscription.business.presentation.subscription.dto.request.SubscriptionUpdateRequest;
import com.project.subscription.business.repository.subscription.SubscriptionBillingHistoryRepository;
import com.project.subscription.business.repository.subscription.SubscriptionChangeHistoryRepository;
import com.project.subscription.business.repository.subscription.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository; // 구독 repository
    private final SubscriptionChangeHistoryRepository subscriptionChangeHistoryRepository; // 구독 변경 내역 repository
    private final SubscriptionBillingHistoryRepository subscriptionBillingHistoryRepository; // 구독 결제 내역 repository


    // 구독 서비스 목록 조회
    // complete
    public List<SubscriptionInternalDto> getMySubscriptions(Long userId) {

        // 사용자별 구독 서비스 목록 조회
        List<Subscription> subscriptions =subscriptionRepository.findAllByUserIdAndDeletedFalse(userId);

        // entity -> dto 변환
        List<SubscriptionInternalDto> subscriptionInternalDtos =
                subscriptions.stream()
                .map(SubscriptionInternalDto::fromEntity)
                .toList();

        return subscriptionInternalDtos;
    }

    // 구독 서비스 단일 조회
    // complete
    public SubscriptionInternalDto getSubscriptionDetail(Long userId, Long subscriptionId) {

        // 구독 ID 로 해당 서비스 상세 조회
        Subscription subscription =subscriptionRepository.findByIdAndUserIdAndDeletedFalse(userId,subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("구독이 존재하지 않습니다."));

        SubscriptionInternalDto subscriptionInternalDto = SubscriptionInternalDto.fromEntity(subscription);

        return subscriptionInternalDto;
    }

    // 구독 서비스 생성
    // complete
    @Transactional
    public SubscriptionInternalDto createSubscription(Long userId, SubscriptionCreateRequest request) {

        Subscription subscription = Subscription.create(userId, request);

        Subscription saved =  subscriptionRepository.save(subscription);

        SubscriptionInternalDto subscriptionInternalDto = SubscriptionInternalDto.fromEntity(saved);

        return subscriptionInternalDto;
    }

    // 구독 서비스 수정
    // complete
    @Transactional
    public SubscriptionInternalDto updateSubscription(Long userId,
                                                      Long subscriptionId,
                                                      SubscriptionUpdateRequest request) {

        Subscription subscription =subscriptionRepository.findByIdAndUserIdAndDeletedFalse(userId,subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("구독이 존재하지 않습니다."));

        // 가격 변경
        if (request.getPrice() != null) {
            subscription.changePrice(request.getPrice());
        }

        // 결제 주기 변경
        if (request.getBillingCycle() != null) {
            subscription.changeBillingCycle(request.getBillingCycle());
        }

        Subscription saved =  subscriptionRepository.save(subscription);

        SubscriptionInternalDto subscriptionInternalDto = SubscriptionInternalDto.fromEntity(saved);

        return subscriptionInternalDto;
    }

    // 구독 서비스 삭제(soft delete-논리 삭제)
    // complete
    @Transactional
    public void deleteSubscription(Long userId, Long subscriptionId) {

        Subscription subscription = subscriptionRepository.findByIdAndUserIdAndDeletedFalse(userId, subscriptionId)
                .orElseThrow(() -> new RuntimeException("구독 없음"));

//        subscriptionRepository.delete(subscription); // hard delete
        subscription.delete(); // soft delete
    }


    // 구독 서비스 변경 내역 조회
    // complete
    public List<SubscriptionChangeHistoryInternalDto> getMySubscriptionChangeHistories(Long userId, Long subscriptionId) {

        // 사용자,구독 별 변경 내역 조회
        List<SubscriptionChangeHistory> subscriptionChangeHistories =subscriptionChangeHistoryRepository.findBySubscriptionIdAndUserIdOrderByChangedAtDesc(userId,subscriptionId);

        List<SubscriptionChangeHistoryInternalDto> subscriptionChangeHistoryInternalDtos = subscriptionChangeHistories.stream()
                .map(SubscriptionChangeHistoryInternalDto::fromEntity)
                .toList();

        return subscriptionChangeHistoryInternalDtos;
    }

    // 구독 서비스 결제 내역 조회
    // complete
    public List<SubscriptionBillingHistoryInternalDto> getMySubscriptionBillingHistories(Long userId, Long subscriptionId) {

        // 사용자,구독 별 결제 내역 조회
        List<SubscriptionBillingHistory> subscriptionBillingHistories =subscriptionBillingHistoryRepository.findByUserIdAndSubscriptionIdOrderByBillingDateDesc(userId,subscriptionId);

        List<SubscriptionBillingHistoryInternalDto> subscriptionBillingHistoryInternalDtos = subscriptionBillingHistories.stream()
                .map(SubscriptionBillingHistoryInternalDto::fromEntity)
                .toList();

        return subscriptionBillingHistoryInternalDtos;
    }



}
