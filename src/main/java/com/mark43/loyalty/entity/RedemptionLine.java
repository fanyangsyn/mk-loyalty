package com.mark43.loyalty.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "redemption_line")
public class RedemptionLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "redemption_id")
    private Long redemptionId;

    @Column(name = "point_ledger_id")
    private Long pointLedgerId;

    @Column(name = "points_used")
    private int pointsUsed;
}
