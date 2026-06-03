package com.mark43.loyalty.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundResponse {

    private Long purchaseId;
    private String referenceNumber;
    private BigDecimal amountRefunded;
    private int pointsClawedBack;
    private String status;
}
