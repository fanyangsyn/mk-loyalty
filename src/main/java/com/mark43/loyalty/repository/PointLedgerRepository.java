package com.mark43.loyalty.repository;

import com.mark43.loyalty.entity.PointLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PointLedgerRepository extends JpaRepository<PointLedger, Long> {

    @Query("SELECT pl FROM PointLedger pl WHERE pl.customerId = :customerId AND pl.type = 'EARN' AND pl.pointsRemaining > 0 AND pl.expiresAt > :now ORDER BY pl.expiresAt ASC")
    List<PointLedger> findAvailablePointsFifo(@Param("customerId") Long customerId, @Param("now") LocalDateTime now);

    @Query("SELECT COALESCE(SUM(pl.pointsRemaining), 0) FROM PointLedger pl WHERE pl.customerId = :customerId AND pl.type = 'EARN' AND pl.pointsRemaining > 0 AND pl.expiresAt > :now")
    int calculateAvailableBalance(@Param("customerId") Long customerId, @Param("now") LocalDateTime now);

    List<PointLedger> findByPurchaseId(Long purchaseId);
}
