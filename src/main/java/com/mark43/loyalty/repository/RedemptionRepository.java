package com.mark43.loyalty.repository;

import com.mark43.loyalty.entity.Redemption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RedemptionRepository extends JpaRepository<Redemption, Long> {
    List<Redemption> findByCustomerId(Long customerId);
}
