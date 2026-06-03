package com.mark43.loyalty.repository;

import com.mark43.loyalty.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    Optional<Purchase> findByReferenceNumber(String referenceNumber);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Purchase p WHERE p.customerId = :customerId AND p.status = 'COMPLETED' AND p.purchasedAt > :since")
    BigDecimal calculateTotalSpendSince(@Param("customerId") Long customerId, @Param("since") LocalDateTime since);
}
