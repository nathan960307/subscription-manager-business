package com.project.subscription.business.repository.subscription;

import com.project.subscription.business.domain.subscription.entity.SubscriptionCatalog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface SubscriptionCatalogRepository extends JpaRepository<SubscriptionCatalog,Long> {

    // 정규화된 이름으로 서비스 아이디 조회
    Optional<SubscriptionCatalog> findByNormalizedName(String normalizedName);
}
