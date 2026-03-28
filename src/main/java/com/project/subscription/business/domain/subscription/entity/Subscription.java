package com.project.subscription.business.domain.subscription.entity;

import com.project.subscription.business.presentation.subscription.dto.request.SubscriptionCreateRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@Table(name = "subscriptions")
public class Subscription { // 사용자의 현재 구독 상태를 나타내는 테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 구독 ID

    @Column(name = "user_id", nullable = false)
    private Long userId; // 사용자 ID

    @Column(name = "service_id", nullable = false)
    private Long serviceId; // 구독 서비스 ID

    @Column(name = "service_name", nullable = false)
    private String serviceName; // 구독 서비스 이름

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price; // 구독 서비스 가격

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_cycle", nullable = false)
    private BillingCycle  billingCycle; // 결제 주기

    @Column(name = "next_billing_date", nullable = false)
    private LocalDateTime nextBillingDate; // 다음 결제일
    //todo : 조회 시 nextBillingDate가 현재보다 과거인 경우,
    // billingCycle 기분으로 미래 시점까지 보정 필요

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_status",nullable = false)
    private SubscriptionStatus subscriptionStatus; // 구독 상태

    @Column(name = "id_deleted", nullable = false)
    private boolean deleted = false; // 삭제 여부

    @Column(name = "start_date")
    private LocalDateTime startDate; // 구독 시작일

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 구독 등록일

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt; // 구독 수정일

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt; // 구독 취소일

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt; // 구독 삭제일

    ///
    /// 도메인 메서드
    ///

    // 구독 생성(수동)
    public static Subscription create(
            Long userId,
            Long serviceId,
            String serviceName,
            BigDecimal price,
            BillingCycle billingCycle,
            LocalDateTime startDate
            ) {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime baseDate = startDate == null ? now : startDate;

        Subscription subscription = new Subscription();

        subscription.userId = userId;
        subscription.serviceId = serviceId;
        subscription.serviceName = serviceName;
        subscription.price = price;
        subscription.billingCycle = billingCycle;
        subscription.nextBillingDate =
                billingCycle == BillingCycle.YEARLY
                        ? baseDate.plusYears(1)
                        : baseDate.plusMonths(1);


        subscription.subscriptionStatus = SubscriptionStatus.ACTIVE;

        subscription.startDate = baseDate;

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
