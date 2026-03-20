package com.project.subscription.business.presentation.subscription.controller;

import com.project.subscription.business.application.subscription.service.SubscriptionService;
import com.project.subscription.business.global.response.ApiResponse;
import com.project.subscription.business.presentation.subscription.dto.internal.SubscriptionBillingHistoryInternalDto;
import com.project.subscription.business.presentation.subscription.dto.internal.SubscriptionChangeHistoryInternalDto;
import com.project.subscription.business.presentation.subscription.dto.internal.SubscriptionInternalDto;
import com.project.subscription.business.presentation.subscription.dto.request.SubscriptionCreateRequest;
import com.project.subscription.business.presentation.subscription.dto.request.SubscriptionUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ApiResponse<List<SubscriptionInternalDto>> getMySubscriptions(@AuthenticationPrincipal Long userId) {

        List<SubscriptionInternalDto> subscriptions = subscriptionService.getMySubscriptions(userId);

        return ApiResponse.success(subscriptions);
    }

    // 구독 단일 조회
    // complete
    @GetMapping("/{subscriptionId}")
    public ApiResponse<SubscriptionInternalDto> getSubscriptionDetail(@AuthenticationPrincipal Long userId,
                                                                      @PathVariable Long subscriptionId) {

        SubscriptionInternalDto subscription = subscriptionService.getSubscriptionDetail(userId,subscriptionId);

        return ApiResponse.success(subscription);
    }

    // 구독 생성
    //complete
    @PostMapping
    public ApiResponse<SubscriptionInternalDto> createSubscription(@AuthenticationPrincipal Long userId,
                                                                   @RequestBody SubscriptionCreateRequest request) {

        SubscriptionInternalDto subscription = subscriptionService.createSubscription(userId, request);

        return ApiResponse.success(subscription);
    }

    // 구독 수정
    // complete
    @PatchMapping("/{subscriptionId}")
    public ApiResponse<SubscriptionInternalDto> updateSubscription(@AuthenticationPrincipal Long userId,
                                                                   @PathVariable Long subscriptionId,
                                                         @RequestBody SubscriptionUpdateRequest request) {

        SubscriptionInternalDto subscription = subscriptionService.updateSubscription(userId,subscriptionId, request);

        return ApiResponse.success(subscription);
    }

    // 구독 삭제
    //complete
    @DeleteMapping("/{subscriptionId}")
    public ApiResponse<Void> deleteSubscription(@AuthenticationPrincipal Long userId,
                                                @PathVariable Long subscriptionId) {

        subscriptionService.deleteSubscription(userId, subscriptionId);

        return ApiResponse.success(null);

    }

    // complete
    // 구독 변경 내역 조회
    @GetMapping("/{subscriptionId}/changes")
    public ApiResponse<List<SubscriptionChangeHistoryInternalDto>> getMySubscriptionChangeHistories(@AuthenticationPrincipal Long userId,
                                                                                                    @PathVariable Long subscriptionId) {

        List<SubscriptionChangeHistoryInternalDto> subscriptions = subscriptionService.getMySubscriptionChangeHistories(userId,subscriptionId);

        return ApiResponse.success(subscriptions);

    }

    // complete
    // 구독 결제 내역 조회
    @GetMapping("/{subscriptionId}/billings")
    public ApiResponse<List<SubscriptionBillingHistoryInternalDto>> getMySubscriptionBillingHistories(@AuthenticationPrincipal Long userId,
                                                                                                      @PathVariable Long subscriptionId) {

        List<SubscriptionBillingHistoryInternalDto> subscriptions = subscriptionService.getMySubscriptionBillingHistories(userId,subscriptionId);

        return ApiResponse.success(subscriptions);

    }




}
