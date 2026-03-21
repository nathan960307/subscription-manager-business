package com.project.subscription.business.repository.external;

import com.project.subscription.business.domain.external.entity.ExternalPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExternalPaymentRepository extends JpaRepository<ExternalPayment, Long> {

    List<ExternalPayment> findByUserId(Long userId);
}
