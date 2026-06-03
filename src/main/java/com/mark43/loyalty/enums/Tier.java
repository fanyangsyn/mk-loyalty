package com.mark43.loyalty.enums;

import java.math.BigDecimal;

public enum Tier {
    SILVER(BigDecimal.ZERO, new BigDecimal("999.99")),
    GOLD(new BigDecimal("1000"), new BigDecimal("4999.99")),
    PLATINUM(new BigDecimal("5000"), null);

    private final BigDecimal minSpend;
    private final BigDecimal maxSpend;

    Tier(BigDecimal minSpend, BigDecimal maxSpend) {
        this.minSpend = minSpend;
        this.maxSpend = maxSpend;
    }

    public BigDecimal getMinSpend() { return minSpend; }
    public BigDecimal getMaxSpend() { return maxSpend; }

    public static Tier fromSpend(BigDecimal totalSpend) {
        if (totalSpend.compareTo(PLATINUM.minSpend) >= 0) return PLATINUM;
        if (totalSpend.compareTo(GOLD.minSpend) >= 0) return GOLD;
        return SILVER;
    }
}
