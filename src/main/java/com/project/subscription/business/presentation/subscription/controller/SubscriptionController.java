package com.project.subscription.business.presentation.subscription.controller;

import com.project.subscription.business.application.subscription.service.SubscriptionService;
import com.project.subscription.business.presentation.subscription.dto.internal.SubscriptionBillingHistoryInternalDto;
import com.project.subscription.business.presentation.subscription.dto.internal.SubscriptionChangeHistoryInternalDto;
import com.project.subscription.business.presentation.subscription.dto.internal.SubscriptionInternalDto;
import com.project.subscription.business.presentation.subscription.dto.request.SubscriptionCreateRequest;
import com.project.subscription.business.presentation.subscription.dto.request.SubscriptionUpdateRequest;
import com.project.subscription.business.presentation.subscription.dto.response.SubscriptionBillingHistoryListResponse;
import com.project.subscription.business.presentation.subscription.dto.response.SubscriptionChangeHistoryListResponse;
import com.project.subscription.business.presentation.subscription.dto.response.SubscriptionDetailResponse;
import com.project.subscription.business.presentation.subscription.dto.response.SubscriptionListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    // 내 구독 목록 조회
    // complete
    @GetMapping
    public SubscriptionListResponse getMySubscriptions() {

        Long userId = 1L; // TODO: 인증 도입 후 실제 userId로 교체

        List<SubscriptionInternalDto> subscriptions = subscriptionService.getMySubscriptions(userId);

        SubscriptionListResponse subscriptionListResponse = SubscriptionListResponse.success(subscriptions);

        return subscriptionListResponse;

    }

    // complete
    // 구독 상세 조회
    @GetMapping("/{id}")
    public SubscriptionDetailResponse getSubscriptionDetail(@PathVariable Long id) {

        SubscriptionInternalDto subscription = subscriptionService.getSubscriptionDetail(id);

        // internal dto 를 response 로 포장
        SubscriptionDetailResponse subscriptionDetailResponse = SubscriptionDetailResponse.success(subscription);

        return subscriptionDetailResponse;
    }



    //complete
    // 구독 생성
    @PostMapping
    public SubscriptionDetailResponse createSubscription(@RequestBody SubscriptionCreateRequest request) {

        Long userId = 1L; // TODO: 인증 도입 후 교체

        SubscriptionInternalDto subscription = subscriptionService.createSubscription(userId, request);

        SubscriptionDetailResponse subscriptionListResponse = SubscriptionDetailResponse.success(subscription);

        return subscriptionListResponse;
    }

    // complete
    // 구독 수정
    @PatchMapping("/{subscriptionId}")
    public SubscriptionDetailResponse updateSubscription(@PathVariable Long subscriptionId,
                                                         @RequestBody SubscriptionUpdateRequest request) {

        Long userId = 1L; // TODO: 인증 도입 후 교체

        SubscriptionInternalDto subscription = subscriptionService.updateSubscription(userId,subscriptionId, request);

        SubscriptionDetailResponse subscriptionListResponse = SubscriptionDetailResponse.success(subscription);

        return subscriptionListResponse;
    }

    //complete
    // 구독 삭제
    @DeleteMapping("/{subscriptionId}")
    public void deleteSubscription(@PathVariable Long subscriptionId) {

        Long userId = 1L; // TODO: 인증 도입 후 교체

        subscriptionService.deleteSubscription(userId, subscriptionId);

    }

    // complete
    // 구독 변경 내역 조회
    @GetMapping("/{subscriptionId}/changes")
    public SubscriptionChangeHistoryListResponse getMySubscriptionChangeHistories(@PathVariable Long subscriptionId) {

        Long userId = 1L; // TODO: 인증 도입 후 실제 userId로 교체

        List<SubscriptionChangeHistoryInternalDto> subscriptions = subscriptionService.getMySubscriptionChangeHistories(userId,subscriptionId);

        SubscriptionChangeHistoryListResponse subscriptionChangeHistoryListResponse = SubscriptionChangeHistoryListResponse.success(subscriptions);

        return subscriptionChangeHistoryListResponse;

    }

    // complete
    // 구독 결제 내역 조회
    @GetMapping("/{subscriptionId}/billings")
    public SubscriptionBillingHistoryListResponse getMySubscriptionBillingHistories(@PathVariable Long subscriptionId) {

        Long userId = 1L; // TODO: 인증 도입 후 실제 userId로 교체

        List<SubscriptionBillingHistoryInternalDto> subscriptions = subscriptionService.getMySubscriptionBillingHistories(userId,subscriptionId);

        SubscriptionBillingHistoryListResponse subscriptionBillingHistoryListResponse = SubscriptionBillingHistoryListResponse.success(subscriptions);

        return subscriptionBillingHistoryListResponse;

    }




}
