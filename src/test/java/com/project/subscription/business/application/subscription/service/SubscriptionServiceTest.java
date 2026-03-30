package com.project.subscription.business.application.subscription.service;


import com.project.subscription.business.repository.external.ExternalPaymentRepository;
import com.project.subscription.business.repository.subscription.SubscriptionBillingHistoryRepository;
import com.project.subscription.business.repository.subscription.SubscriptionCatalogRepository;
import com.project.subscription.business.repository.subscription.SubscriptionChangeHistoryRepository;
import com.project.subscription.business.repository.subscription.SubscriptionRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

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





}
