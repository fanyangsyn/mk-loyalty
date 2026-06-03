package com.mark43.loyalty.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseResponse {

    private Long id;
    private Long customerId;
    private BigDecimal amount;
    private String referenceNumber;
    private String status;
    private int pointsEarned;
    private LocalDateTime purchasedAt;
}
