package com.project.subscription.business.application.subscription.service;


import com.project.subscription.business.domain.subscription.entity.BillingCycle;
import com.project.subscription.business.domain.subscription.entity.Subscription;
import com.project.subscription.business.presentation.subscription.dto.internal.SubscriptionInternalDto;
import com.project.subscription.business.presentation.subscription.dto.request.SubscriptionCreateRequest;
import com.project.subscription.business.presentation.subscription.dto.request.SubscriptionUpdateRequest;
import com.project.subscription.business.repository.external.ExternalPaymentRepository;
import com.project.subscription.business.repository.subscription.SubscriptionBillingHistoryRepository;
import com.project.subscription.business.repository.subscription.SubscriptionCatalogRepository;
import com.project.subscription.business.repository.subscription.SubscriptionChangeHistoryRepository;
import com.project.subscription.business.repository.subscription.SubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTest {

    // mock 객체 선언
    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private SubscriptionChangeHistoryRepository subscriptionChangeHistoryRepository;

    @Mock
    private SubscriptionBillingHistoryRepository subscriptionBillingHistoryRepository;

    @Mock
    private ExternalPaymentRepository externalPaymentRepository;

    @Mock
    private SubscriptionCatalogRepository subscriptionCatalogRepository;

    // test 대상 주입
    @InjectMocks
    private SubscriptionService subscriptionService;


    // 테스트 메서드

    // 구독 생성 성공
    @Test
    void createSubscription_success(){

        // given - data setup
        SubscriptionCreateRequest request = new SubscriptionCreateRequest(
                "Netflix",
                BigDecimal.valueOf(15000),
                BillingCycle.MONTHLY,
                LocalDateTime.now()
        );

        // given - mock setup

        when(subscriptionCatalogRepository.findByNormalizedName(any()))
                .thenReturn(Optional.empty());

        when(subscriptionCatalogRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(subscriptionRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when (실행)
        SubscriptionInternalDto result = subscriptionService.createSubscription(1L, request);

        // then (검증)
        assertNotNull(result);

        verify(subscriptionRepository).save(any(Subscription.class));
        verify(subscriptionChangeHistoryRepository).save(any());

    }

    // 구독 생성 실패
    @Test
    void createSubscription_fail(){

        // given - data setup
        SubscriptionCreateRequest request = new SubscriptionCreateRequest(
                "Netflix",
                BigDecimal.valueOf(15000),
                BillingCycle.MONTHLY,
                LocalDateTime.now()
        );

        // given - mock setup

        when(subscriptionCatalogRepository.findByNormalizedName(any()))
                .thenReturn(Optional.empty());

        when(subscriptionCatalogRepository.save(any()))
                .thenThrow(new RuntimeException());

        // when (실행) & then (검증)

        assertThrows(RuntimeException.class, () -> {
            subscriptionService.createSubscription(1L, request);
        });

    }

    // 구독 수정 성공
    @Test
    void updateSubscription_success() {

        // given - data setup
        Long userId = 1L;
        Long subscriptionId = 1L;

        SubscriptionUpdateRequest request = new SubscriptionUpdateRequest(
                BigDecimal.valueOf(20000),
                BillingCycle.YEARLY
        );

        // given - mock setup
        Subscription subscription = mock(Subscription.class);

        when(subscription.getPrice())
                .thenReturn(BigDecimal.valueOf(10000));

        when(subscription.getBillingCycle())
                .thenReturn(BillingCycle.MONTHLY);

        when(subscriptionRepository.findByUserIdAndIdAndDeletedFalse(userId, subscriptionId))
                .thenReturn(Optional.of(subscription));

        when(subscriptionRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));


        // when
        SubscriptionInternalDto result = subscriptionService.updateSubscription(userId, subscriptionId, request);

        // then
        assertNotNull(result);

        verify(subscription).changePrice(any());
        verify(subscription).changeBillingCycle(any());

        verify(subscriptionRepository).save(subscription);
        verify(subscriptionChangeHistoryRepository, times(2)).save(any());
    }

    // 구독 삭제 성공

    // 구독 자동 생성 성공






}
