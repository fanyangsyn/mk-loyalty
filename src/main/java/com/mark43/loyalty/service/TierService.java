package com.mark43.loyalty.service;

import com.mark43.loyalty.enums.Tier;
import com.mark43.loyalty.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TierService {

    private final PurchaseRepository purchaseRepository;

    public Tier calculateTier(Long customerId) {
        LocalDateTime twelveMonthsAgo = LocalDateTime.now().minusMonths(12);
        BigDecimal totalSpend = purchaseRepository.calculateTotalSpendSince(customerId, twelveMonthsAgo);
        return Tier.fromSpend(totalSpend);
    }

    public BigDecimal getRollingSpend(Long customerId) {
        LocalDateTime twelveMonthsAgo = LocalDateTime.now().minusMonths(12);
        return purchaseRepository.calculateTotalSpendSince(customerId, twelveMonthsAgo);
    }
}
