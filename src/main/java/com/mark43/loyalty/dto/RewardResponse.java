package com.mark43.loyalty.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RewardResponse {

    private Long id;
    private String name;
    private String description;
    private int pointsCost;
    private boolean active;
}
