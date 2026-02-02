package com.project.subscription.business.presentation.subscription.controller;

import com.project.subscription.business.application.subscription.service.SubscriptionService;
import com.project.subscription.business.presentation.subscription.dto.internal.SubscriptionInternalDto;
import com.project.subscription.business.presentation.subscription.dto.response.SubscriptionDetailResponse;
import com.project.subscription.business.presentation.subscription.dto.response.SubscriptionListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    // 구독 상세 조회
    @GetMapping("/{id}")
    public SubscriptionDetailResponse getSubscriptionDetail(@PathVariable Long id) {

        SubscriptionInternalDto subscription = subscriptionService.getSubscriptionDetail(id);

        // subscription 을 SubscriptionResponse로 포장 필요

        return null;
    }

    // 내 구독 목록 조회
    @GetMapping("/me")
    public SubscriptionListResponse getMySubscriptions() {

        List<SubscriptionInternalDto> subscriptions = subscriptionService.getMySubscriptions();

        // subscriptions 을 SubscriptionResponse로 포장 필요


        return null;

    }




}
