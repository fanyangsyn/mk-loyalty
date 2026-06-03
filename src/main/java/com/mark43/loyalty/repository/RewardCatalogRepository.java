package com.mark43.loyalty.repository;

import com.mark43.loyalty.entity.RewardCatalog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RewardCatalogRepository extends JpaRepository<RewardCatalog, Long> {
    List<RewardCatalog> findByActiveTrue();
}
