package com.project.subscription.business.presentation.subscription.controller;

import com.project.subscription.business.application.subscription.service.SubscriptionService;
import com.project.subscription.business.presentation.subscription.dto.internal.SubscriptionInternalDto;
import com.project.subscription.business.presentation.subscription.dto.response.SubscriptionDetailResponse;
import com.project.subscription.business.presentation.subscription.dto.response.SubscriptionListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    // complete
    // 구독 상세 조회
    @GetMapping("/{id}")
    public SubscriptionDetailResponse getSubscriptionDetail(@PathVariable Long id) {

        SubscriptionInternalDto subscription = subscriptionService.getSubscriptionDetail(id);

        SubscriptionDetailResponse subscriptionDetailResponse = SubscriptionDetailResponse.success(subscription);

        return subscriptionDetailResponse;
    }

    // 내 구독 목록 조회
    @GetMapping("/me")
    public SubscriptionListResponse getMySubscriptions() {

        Long userId = 1L; // TODO: 인증 도입 후 실제 userId로 교체

        List<SubscriptionInternalDto> subscriptions = subscriptionService.getMySubscriptions(userId);

        SubscriptionListResponse subscriptionListResponse = SubscriptionListResponse.success(subscriptions);


        return subscriptionListResponse;

    }

    // 구독 결제 내역 조회

    // 구독 변경 내역 조회




}
