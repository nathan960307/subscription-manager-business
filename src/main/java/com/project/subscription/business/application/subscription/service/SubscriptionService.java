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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
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
        Subscription subscription =subscriptionRepository.findByUserIdAndIdAndDeletedFalse(userId,subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("구독이 존재하지 않습니다."));

        SubscriptionInternalDto subscriptionInternalDto = SubscriptionInternalDto.fromEntity(subscription);

        return subscriptionInternalDto;
    }

    // 구독 서비스 생성(수동)
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

        Subscription subscription =subscriptionRepository.findByUserIdAndIdAndDeletedFalse(userId,subscriptionId)
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

        Subscription subscription = subscriptionRepository.findByUserIdAndIdAndDeletedFalse(userId, subscriptionId)
                .orElseThrow(() -> new RuntimeException("구독 없음"));

//        subscriptionRepository.delete(subscription); // hard delete
        subscription.delete(); // soft delete
    }


    // 구독 서비스 변경 내역 조회
    // complete
    @Transactional(readOnly = true)
    public List<SubscriptionChangeHistoryInternalDto> getMySubscriptionChangeHistories(Long userId, Long subscriptionId) {

        // 사용자,구독 별 변경 내역 조회
        List<SubscriptionChangeHistory> subscriptionChangeHistories =subscriptionChangeHistoryRepository.findByUserIdAndSubscriptionIdOrderByChangedAtDesc(userId,subscriptionId);

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
                // SubscriptionStatus 가 ACTIVE 인것만 필터
                .filter(s -> s.getSubscriptionStatus() == SubscriptionStatus.ACTIVE)
                // 가격만 추출
                .map(Subscription::getPrice)
                // 전부 더하기
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        log.info("totalAmount: {}", totalAmount);


        // monthlyBillingitems 생성
        List<MonthlyBillingItemDto> monthlyBillingItems = subscriptions.stream()
                // SubscriptionStatus 가 ACTIVE 인것만 필터
                .filter(s -> s.getSubscriptionStatus() == SubscriptionStatus.ACTIVE)
                .map(MonthlyBillingItemDto::fromEntity)
                .toList();

        log.info("monthlyBillingItems: {}", monthlyBillingItems);

        SubscriptionSummaryInternalDto subscriptionSummaryInternalDto = SubscriptionSummaryInternalDto.builder()
                .totalCount(totalCount)
                .totalAmount(totalAmount)
                .monthlyBillingItems(monthlyBillingItems)
                .build();

        return subscriptionSummaryInternalDto;
    }

    // 구독 서비스 자동 생성
    // complete
    @Transactional
    public List<SubscriptionInternalDto> autoCreateSubscription(Long userId) {

        // 결과 리스트
        List<SubscriptionInternalDto> result = new ArrayList<>();

        // 외부 데이터 조회 (mock)
        List<ExternalPayment> externalPayments = externalPaymentRepository.findByUserId(userId);

        // 결제 내역(entity list)을 merchant 기준으로 그룹화
        Map<String, List<ExternalPayment>> groupedPayments =
                externalPayments.stream()
                        .collect(Collectors.groupingBy(ExternalPayment::getMerchantName));

        // 필터링 로직 적용
        Map<String, List<ExternalPayment>> candidates = groupedPayments.entrySet().stream()
                .filter(entry -> entry.getValue().size() >=2) // 결제 내역이 2회 이상인지 확인
                .peek(entry -> entry.getValue().sort(Comparator.comparing(ExternalPayment::getCreatedAt))) // 오름차순 정렬
                .filter(entry -> {
                    List<ExternalPayment> payments = entry.getValue();

                    long prev = ChronoUnit.DAYS.between(
                            payments.get(0).getTransactionDate().toLocalDate(),
                            payments.get(1).getTransactionDate().toLocalDate()
                    );

                    for(int i=2; i<payments.size(); i++){
                        long current = ChronoUnit.DAYS.between(
                                payments.get(i-1).getTransactionDate(),
                                payments.get(i).getTransactionDate()
                        );

                        if(Math.abs(current-prev) > 5){
                            return false;
                        }
                    }

                    return true;

                }) // 결제 날짜 간격 계산
                .filter(entry -> {
                    List<ExternalPayment> payments = entry.getValue();

                    double avg = payments.stream()
                            .mapToDouble(p -> p.getAmount().doubleValue())
                            .average()
                            .orElse(0);

                    return payments.stream()
                            .allMatch(p ->
                                    Math.abs(p.getAmount().doubleValue() - avg) / avg <= 0.1
                            );
                }) // 결제 금액이 일정 수준 유사한지 검사
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // 정규화
        Map<String, List<ExternalPayment>> normalizedCandidates = new HashMap<>();

        for (Map.Entry<String, List<ExternalPayment>> entry : candidates.entrySet()) {
            normalizedCandidates.put(
                    normalizeServiceName(entry.getKey()),
                    entry.getValue()
            );
        }

        // 기존 구독 조회
        List<Subscription> subscriptions = subscriptionRepository.findByUserIdAndDeletedFalse(userId);

        // 기존 구독 이름 Set
        Set<String> existingNames = subscriptions.stream()
                .map(Subscription::getServiceName)
                .collect(Collectors.toSet());

        // 비교
        for (String serviceName : normalizedCandidates.keySet()) {

            // 있는 경우
            if (existingNames.contains(serviceName)) {
                continue;
            }

            // 없는 경우

            List<ExternalPayment> payments = normalizedCandidates.get(serviceName); // 외부 결제 내역 저장

            payments.sort(Comparator.comparing(ExternalPayment::getTransactionDate)); // 오름차순

            ExternalPayment latest = payments.get(payments.size() - 1); // 거래 목록중 최신 거래 항목 지정

            // 구독 서비스 id 조회
            Optional<SubscriptionCatalog> catalogOpt = subscriptionCatalogRepository.
                    findByNormalizedName(serviceName);

            SubscriptionCatalog catalog;

            if(catalogOpt.isPresent()) { // 조회 결과가 존재 하는 경우
                catalog = catalogOpt.get();
            }else { // 조회 결과가 존재 하지 않는 경우
                catalog = SubscriptionCatalog.create(serviceName, serviceName, serviceName); // id 없음
                catalog = subscriptionCatalogRepository.save(catalog); // id 생성됨
            }

            // 서비스 id 저장
            Long serviceId = catalog.getId();

            // 결제 주기 계산
            long cycle = ChronoUnit.DAYS.between(
                    payments.get(payments.size() - 2).getTransactionDate(),
                    payments.get(payments.size() - 1).getTransactionDate()
            );

            BillingCycle billingCycle;

            if (cycle >= 28 && cycle <= 31) {
                billingCycle = BillingCycle.MONTHLY;
            } else if (cycle >= 360 && cycle <= 370) {
                billingCycle = BillingCycle.YEARLY;
            } else {
                billingCycle = BillingCycle.MONTHLY; // 기본값
            }

            // 1. 구독 생성
            Subscription subscription = Subscription.create(
                    userId,
                    serviceId,
                    serviceName,
                    latest.getAmount(),
                    billingCycle,
                    latest.getTransactionDate()
            );

            Subscription saved = subscriptionRepository.save(subscription);

            result.add(SubscriptionInternalDto.fromEntity(saved));

            // 2. 구독 결제 내역 연결
            for(ExternalPayment payment  : payments) {

                BillingStatus status = convertStatus(payment.getTransactionType()); // 결제 타입 변환
                SubscriptionBillingHistory history = SubscriptionBillingHistory.create(
                        userId,
                        saved.getId(),
                        payment.getTransactionId(),
                        payment.getTransactionDate(),
                        status,
                        payment.getAmount()
                );

                subscriptionBillingHistoryRepository.save(history);
            }

            // 3. 구독 변경 내역 연결
            SubscriptionChangeHistory history =
                    SubscriptionChangeHistory.create(
                            userId, // 사용자 id
                            saved.getId(), // 구독 id
                            ChangeType.CREATE,
                            null, // 생성이니까 이전값 없음
                            subscription.getServiceName(), // 또는 전체 JSON도 가능
                            ChangedBy.SYSTEM
                    );

            subscriptionChangeHistoryRepository.save(history);
        }

        return result;
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

    // 결제 타입 변환
    private BillingStatus convertStatus(String status) {
        return switch (status) {
            case "SUCCESS", "APPROVED", "PAID" -> BillingStatus.SUCCESS;
            case "FAILED", "DECLINED" -> BillingStatus.FAILED;
            case "REFUNDED", "CANCELED", "VOID" -> BillingStatus.REFUNDED;
            default -> BillingStatus.UNKNOWN;
        };
    }




}
