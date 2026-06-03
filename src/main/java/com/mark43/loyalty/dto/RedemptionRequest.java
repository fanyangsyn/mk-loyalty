package com.mark43.loyalty.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedemptionRequest {

    @NotNull
    private Long customerId;

    @NotNull
    private Long rewardId;
}
