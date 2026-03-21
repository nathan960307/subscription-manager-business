package com.project.subscription.business.domain.subscription.entity;

import com.project.subscription.business.presentation.subscription.dto.request.SubscriptionCreateRequest;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@Table(name = "subscription")
public class Subscription { // 사용자의 현재 구독 상태를 나타내는 테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 구독 ID

    @Column(name = "user_id", nullable = false)
    private Long userId; // 사용자 ID

    @Column(name = "service_id", nullable = false)
    private Long serviceId; // 구독 서비스 ID

    @Column(name = "merchant_name", nullable = false)
    private String serviceName; // 구독 서비스 이름

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price; // 구독 서비스 가격

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_cycle", nullable = false)
    private BillingCycle  billingCycle; // 결제 주기

    @Column(name = "next_billing_date", nullable = false)
    private LocalDateTime nextBillingDate; // 다음 결제일

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_status",nullable = false)
    private SubscriptionStatus subscriptionStatus; // 구독 상태

    @Column(name = "auto_renew", nullable = false)
    private boolean autoRenew = true; // 자동 갱신 여부

    @Column(name = "trial_end_date")
    private LocalDateTime trialEndDate; // 무료 체험 종료일

    @Column(name = "first_paid_at")
    private LocalDateTime firstPaidAt; // 첫 유료 결제

    @Column(name = "converted_to_paid_at")
    private LocalDateTime convertedToPaidAt; // 유료 전환 시점

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false; // 삭제 여부

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 구독 등록일

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt; // 수정일

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt; // 구독 취소일

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt; // 삭제일

    ///
    /// 도메인 메서드
    ///

    // 구독 생성(수동)
    public static Subscription create(Long userId, SubscriptionCreateRequest request) {

        LocalDateTime now = LocalDateTime.now();

        Subscription subscription = new Subscription();

        subscription.userId = userId;
        subscription.serviceId = request.getServiceId();
        // todo 서비스 명 입력 받기
        subscription.price = request.getPrice();
        subscription.billingCycle = request.getBillingCycle();

        subscription.subscriptionStatus = SubscriptionStatus.ACTIVE;
        subscription.autoRenew = true;

        subscription.nextBillingDate =
                request.getBillingCycle() == BillingCycle.YEARLY
                        ? now.plusYears(1)
                        : now.plusMonths(1);

        subscription.createdAt = now;
        subscription.updatedAt = now;

        return subscription;
    }

    // 구독 생성(자동)
    public static Subscription create(Long userId,
                                      String merchantName,
                                      BigDecimal price,
                                      LocalDateTime lastPaymentDate) {

        LocalDateTime now = LocalDateTime.now();

        Subscription subscription = new Subscription();

        subscription.userId = userId;
        subscription.serviceId = 0L; // todo
        subscription.merchantName = merchantName;
        subscription.price = price;

        // 기본값 (일단 단순하게)
        subscription.billingCycle = BillingCycle.MONTHLY;

        subscription.subscriptionStatus = SubscriptionStatus.ACTIVE;
        subscription.autoRenew = true;

        // 다음 결제일 (임시: 한달 뒤)
        subscription.nextBillingDate = lastPaymentDate.plusMonths(1);

        subscription.createdAt = now;
        subscription.updatedAt = now;

        return subscription;

    }


    // 가격 변경
    public void changePrice(BigDecimal price) {
        this.price = price;
        this.updatedAt = LocalDateTime.now();
    }

    // 결제 주기 변경
    public void changeBillingCycle(BillingCycle billingCycle) {
        this.billingCycle = billingCycle;

        LocalDateTime now = LocalDateTime.now();
        this.nextBillingDate =
                billingCycle == BillingCycle.YEARLY
                        ? now.plusYears(1)
                        : now.plusMonths(1);

        this.updatedAt = now;
    }

    // 구독 삭제
    public void delete(){
        if(this.subscriptionStatus == SubscriptionStatus.DELETED){
            throw new IllegalStateException("이미 삭제된 구독 입니다");
        }
        this.updatedAt = LocalDateTime.now();
        this.deletedAt = LocalDateTime.now();
        this.subscriptionStatus = SubscriptionStatus.DELETED;
        this.deleted = true;
    }
}
