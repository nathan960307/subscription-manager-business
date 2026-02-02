package com.project.subscription.business.application.subscription.service;

import com.project.subscription.business.presentation.subscription.dto.internal.SubscriptionInternalDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {

    public List<SubscriptionInternalDto> getMySubscriptions() { // 구독 서비스 목록 조회
        return null;
    }

    public SubscriptionInternalDto getSubscriptionDetail(Long subscriptionId) { // 구독 서비스 단일 조회
        return null;
    }

}
