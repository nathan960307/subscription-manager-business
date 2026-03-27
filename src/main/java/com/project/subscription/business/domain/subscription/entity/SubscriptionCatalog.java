package com.project.subscription.business.domain.subscription.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "subscription_catalogs")
public class SubscriptionCatalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 서비스 id

    @Column(name = "company_name", nullable = false)
    private String companyName; // 회사명

    @Column(name = "service_name", nullable = false)
    private String serviceName; // 서비스 명

    @Column(name = "normalized_name", nullable = false, unique = true)
    private String normalizedName; // 정규화 이름

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 생성일


}
