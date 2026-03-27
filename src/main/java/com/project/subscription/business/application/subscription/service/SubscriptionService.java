package com.project.subscription.business.application.subscription.service;

import com.project.subscription.business.domain.external.entity.ExternalPayment;
import com.project.subscription.business.domain.subscription.entity.*;
import com.project.subscription.business.presentation.subscription.dto.internal.*;
import com.project.subscription.business.presentation.subscription.dto.request.SubscriptionCreateRequest;
import com.project.subscription.business.presentation.subscription.dto.request.SubscriptionUpdateRequest;
import com.project.subscription.business.repository.external.ExternalPaymentRepository;
import com.project.subscription.business.repository.subscription.SubscriptionBillingHistoryRepository;
import com.project.subscription.business.repository.subscription.SubscriptionCatalogRepository;
import com.project.subscription.business.repository.subscription.SubscriptionChangeHistoryRepository;
import com.project.subscription.business.repository.subscription.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository; // 구독 repository
    private final SubscriptionChangeHistoryRepository subscriptionChangeHistoryRepository; // 구독 변경 내역 repository
    private final SubscriptionBillingHistoryRepository subscriptionBillingHistoryRepository; // 구독 결제 내역 repository
    private final ExternalPaymentRepository externalPaymentRepository; // 외부 결제 내역 repository
    private final SubscriptionCatalogRepository subscriptionCatalogRepository; // 구독 카탈로그 repository


    // 구독 서비스 목록 조회
    // complete
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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

        // 서비스 명 정규화
        String normalized = normalizeServiceName(request.getServiceName());

        // 정규화된 이름으로 serviceID 조회

        Optional<SubscriptionCatalog> catalogOpt = subscriptionCatalogRepository.
                findByNormalizedName(normalized);

        SubscriptionCatalog catalog;

        if(catalogOpt.isPresent()) { // 조회 결과가 존재 하는 경우
            catalog = catalogOpt.get();
        }else { // 조회 결과가 존재 하지 않는 경우
            catalog = SubscriptionCatalog.create(normalized, normalized, normalized); // id 없음
            catalog = subscriptionCatalogRepository.save(catalog); // id 생성됨
        }

        // 서비스 id 저장
        Long serviceId = catalog.getId();

        // 구독 정보 생성
        Subscription subscription = Subscription.create(
                userId,
                serviceId, // 서비스 id
                catalog.getServiceName(),
                request.getPrice(),
                request.getBillingCycle(),
                request.getStartDate()
                );

        // 구독 저장
        Subscription saved =  subscriptionRepository.save(subscription);

        SubscriptionInternalDto subscriptionInternalDto = SubscriptionInternalDto.fromEntity(saved);

        // 구독 생성 이력 생성 및 저장
        SubscriptionChangeHistory history =
                SubscriptionChangeHistory.create(
                        userId, // 사용자 id
                        saved.getId(), // 구독 id
                        ChangeType.CREATE,
                        null, // 생성이니까 이전값 없음
                        saved.getServiceName(), // 또는 전체 JSON도 가능
                        ChangedBy.SYSTEM
                );

        subscriptionChangeHistoryRepository.save(history);

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
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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

    // 구독 서비스 자동 생성
    @Transactional
    public void autoCreateSubscription(Long userId) {

        // 외부 데이터 조회 (mock)
        List<ExternalPayment> externalPayments = externalPaymentRepository.findByUserId(userId);

        // 결제 내역(entity list)을 merchant 기준으로 그룹화
        Map<String, List<ExternalPayment>> groupedPayments =
                externalPayments.stream()
                        .collect(Collectors.groupingBy(ExternalPayment::getMerchantName));

        // todo : 필터링 로직 적용
        Map<String, List<ExternalPayment>> candidates = groupedPayments;

        // 기존 구독 조회
        List<Subscription> subscriptions = subscriptionRepository.findByUserIdAndDeletedFalse(userId);

        // 기존 구독 이름 Set
        Set<String> existingNames = subscriptions.stream()
                .map(Subscription::getServiceName)
                .collect(Collectors.toSet());

        // 비교
        for (String serviceName : candidates.keySet()) {

            // 있는 경우
            if (existingNames.contains(serviceName)) {
                continue;
            }

            // 없는 경우

            List<ExternalPayment> payments = candidates.get(serviceName); // 외부 결제 내역 저장

            ExternalPayment latest = payments.get(payments.size() - 1); // 거래 목록중 최신 거래 항목 지정

            // 1. 구독 생성
            Subscription subscription = Subscription.create(
                    userId,
                    serviceName,
                    latest.getAmount(),
                    serviceId,
                    latest.getTransactionDate()
            );

            subscriptionRepository.save(subscription);

            // 2. 구독 결제 내역 연결
            for(ExternalPayment payment  : payments) {

                SubscriptionBillingHistory history = SubscriptionBillingHistory.create(
                        userId,
                        subscription.getId(),
                        payment.getTransactionDate(),
                        payment.getAmount()
                );

                subscriptionBillingHistoryRepository.save(history);
            }
        }


    }


    /// 공통 ///

    // 대표 서비스명 매핑
    private static final Map<String, String> ALIAS_MAP = Map.of(
            "NETFLIX", "NETFLIX",
            "NETFLIXCOM", "NETFLIX",
            "넷플릭스", "NETFLIX"
    );

    // 서비스 이름 정규화
    private String normalizeServiceName(String serviceName) {

        // 이름 정규화
        String normal = serviceName
                .trim()
                .toUpperCase()
                .replaceAll("[^A-Z0-9가-힣]", "");

        // 이름 매핑
        String mapped = ALIAS_MAP.getOrDefault(normal, normal);

        return mapped;
    }




}
