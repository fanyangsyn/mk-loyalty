package com.mark43.loyalty.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceResponse {

    private Long customerId;
    private int availablePoints;
    private String tier;
    private BigDecimal rollingSpend;
}
