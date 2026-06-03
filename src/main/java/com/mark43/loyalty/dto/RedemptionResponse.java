package com.mark43.loyalty.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedemptionResponse {

    private Long id;
    private Long customerId;
    private String rewardName;
    private int pointsSpent;
    private String status;
    private LocalDateTime createdAt;
}
