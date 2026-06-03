package com.mark43.loyalty.entity;

import com.mark43.loyalty.enums.LedgerType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "point_ledger")
public class PointLedger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "purchase_id")
    private Long purchaseId;

    private int points;

    @Enumerated(EnumType.STRING)
    private LedgerType type;

    @Column(name = "points_remaining")
    private int pointsRemaining;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
