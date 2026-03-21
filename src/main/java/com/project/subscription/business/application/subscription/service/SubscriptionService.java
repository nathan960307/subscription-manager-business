package com.project.subscription.business.application.subscription.service;

import com.project.subscription.business.domain.subscription.entity.*;
import com.project.subscription.business.presentation.subscription.dto.internal.*;
import com.project.subscription.business.presentation.subscription.dto.request.SubscriptionCreateRequest;
import com.project.subscription.business.presentation.subscription.dto.request.SubscriptionUpdateRequest;
import com.project.subscription.business.repository.subscription.SubscriptionBillingHistoryRepository;
import com.project.subscription.business.repository.subscription.SubscriptionChangeHistoryRepository;
import com.project.subscription.business.repository.subscription.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
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

        // 변경 전 값 저장
        BigDecimal oldPrice = subscription.getPrice();
        BillingCycle oldCycle = subscription.getBillingCycle();

        // 가격 변경 여부 검증
        if (request.getPrice() != null && request.getPrice().compareTo(oldPrice) != 0) {
            subscription.changePrice(request.getPrice());

            subscriptionChangeHistoryRepository.save(
                    SubscriptionChangeHistory.create(
                            userId,
                            subscriptionId,
                            ChangeType.PRICE,
                            oldPrice.toString(),
                            request.getPrice().toString(),
                            ChangedBy.USER
                    )
            );
        }

        // 결제 주기 변경
        if (request.getBillingCycle() != null && !request.getBillingCycle().equals(oldCycle)) {
            subscription.changeBillingCycle(request.getBillingCycle());

            subscriptionChangeHistoryRepository.save(
                    SubscriptionChangeHistory.create(
                            userId,
                            subscriptionId,
                            ChangeType.BILLING_CYCLE,
                            oldCycle.name(),
                            request.getBillingCycle().name(),
                            ChangedBy.USER
                    )
            );
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

    // 구독 서비스 요약 통계 조회
    // complete
    @Transactional(readOnly = true)
    public SubscriptionSummaryInternalDto getSubscriptionSummary(Long userId) {

        // 구독 목록 조회
        List<Subscription> subscriptions = subscriptionRepository.findAllByUserIdAndDeletedFalse(userId);

        // 구독 서비스 수 조회
        int totalCount = subscriptions.size();

        // 예상 결제 내역 조회
        LocalDate now =  LocalDate.now();

        BigDecimal totalAmount = subscriptions.stream()
                // nextBillingDate가 이번 달인 것만 필터
                .filter(s -> {
                    LocalDate billingDate = s.getNextBillingDate().toLocalDate();
                    return billingDate.getYear() == now.getYear() && billingDate.getMonth() == now.getMonth();
                })
                // 가격만 추출
                .map(Subscription::getPrice)
                // 전부 더하기
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        // monthlyBillingitems 생성
        List<MonthlyBillingItemDto> monthlyBillingItems = subscriptions.stream()
                .filter(s -> {
                    LocalDate billingDate = s.getNextBillingDate().toLocalDate();
                    return billingDate.getYear() == now.getYear() && billingDate.getMonth() == now.getMonth();
                })
                .map(MonthlyBillingItemDto::fromEntity)
                .toList();

        SubscriptionSummaryInternalDto subscriptionSummaryInternalDto = SubscriptionSummaryInternalDto.builder()
                .totalCount(totalCount)
                .totalAmount(totalAmount)
                .monthlyBillingItems(monthlyBillingItems)
                .build();

        return subscriptionSummaryInternalDto;
    }



}
