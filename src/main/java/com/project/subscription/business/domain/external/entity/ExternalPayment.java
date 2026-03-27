package com.project.subscription.business.domain.external.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// 외부 결제 내역 테이블

@Entity
@Table(name = "external_payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExternalPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; // 외부 결제 내역 id

    @Column(name = "user_id", nullable = false)
    private Long userId; // 사용자 id

    @Column(name = "provider", length = 50, nullable = false)
    private String provider; // 결제 제공자 (SHINHAN, NH, KB, TOSS)

    @Column(name = "transaction_id", length = 100, nullable = false)
    private String transactionId; // 외부 거래 고유 id

    @Column(name = "merchant_name", length = 255, nullable = false)
    private String merchantName; // 가맹점/회사명 (NETFLIX, GPT 등)

    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount; // 결제 금액

    @Column(name = "currency", length = 10, nullable = false)
    private String currency; // 통화

    @Column(name = "transaction_type", length = 20, nullable = false)
    private String transactionType; // 결제 유형

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate; // 결제 발생 시각

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 데이터 수집 시각


}
