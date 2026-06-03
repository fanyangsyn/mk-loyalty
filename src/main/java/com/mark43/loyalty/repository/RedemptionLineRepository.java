package com.mark43.loyalty.repository;

import com.mark43.loyalty.entity.RedemptionLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RedemptionLineRepository extends JpaRepository<RedemptionLine, Long> {
    List<RedemptionLine> findByRedemptionId(Long redemptionId);
}
