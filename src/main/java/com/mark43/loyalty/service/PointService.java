package com.mark43.loyalty.service;

import com.mark43.loyalty.dto.BalanceResponse;
import com.mark43.loyalty.dto.RedemptionRequest;
import com.mark43.loyalty.dto.RedemptionResponse;
import com.mark43.loyalty.entity.PointLedger;
import com.mark43.loyalty.entity.Redemption;
import com.mark43.loyalty.entity.RedemptionLine;
import com.mark43.loyalty.entity.RewardCatalog;
import com.mark43.loyalty.enums.LedgerType;
import com.mark43.loyalty.enums.Tier;
import com.mark43.loyalty.exception.InsufficientPointsException;
import com.mark43.loyalty.exception.ResourceNotFoundException;
import com.mark43.loyalty.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointLedgerRepository pointLedgerRepository;
    private final RewardCatalogRepository rewardCatalogRepository;
    private final RedemptionRepository redemptionRepository;
    private final RedemptionLineRepository redemptionLineRepository;
    private final CustomerRepository customerRepository;
    private final TierService tierService;

    public BalanceResponse getBalance(Long customerId) {
        customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        int availableBalance = pointLedgerRepository.calculateAvailableBalance(customerId, LocalDateTime.now());
        Tier tier = tierService.calculateTier(customerId);
        BigDecimal rollingSpend = tierService.getRollingSpend(customerId);

        return BalanceResponse.builder()
                .customerId(customerId)
                .availablePoints(availableBalance)
                .tier(tier.name())
                .rollingSpend(rollingSpend)
                .build();
    }

    @Transactional
    public RedemptionResponse redeemPoints(RedemptionRequest request) {
        Long customerId = request.getCustomerId();
        Long rewardId = request.getRewardId();

        customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        RewardCatalog reward = rewardCatalogRepository.findById(rewardId)
                .orElseThrow(() -> new ResourceNotFoundException("Reward not found with id: " + rewardId));

        if (!reward.isActive()) {
            throw new ResourceNotFoundException("Reward is not active: " + reward.getName());
        }

        LocalDateTime now = LocalDateTime.now();
        int availableBalance = pointLedgerRepository.calculateAvailableBalance(customerId, now);
        int pointsCost = reward.getPointsCost();

        if (availableBalance < pointsCost) {
            throw new InsufficientPointsException(
                    String.format("Insufficient points. Required: %d, Available: %d", pointsCost, availableBalance));
        }

        List<PointLedger> availableEntries = pointLedgerRepository.findAvailablePointsFifo(customerId, now);

        Redemption redemption = Redemption.builder()
                .customerId(customerId)
                .rewardId(rewardId)
                .pointsSpent(pointsCost)
                .build();
        redemption = redemptionRepository.save(redemption);

        // FEFO consumption loop
        int remaining = pointsCost;
        List<PointLedger> modifiedLedgers = new ArrayList<>();
        List<RedemptionLine> redemptionLines = new ArrayList<>();

        for (PointLedger entry : availableEntries) {
            if (remaining <= 0) break;

            int consume = Math.min(entry.getPointsRemaining(), remaining);
            entry.setPointsRemaining(entry.getPointsRemaining() - consume);
            remaining -= consume;

            modifiedLedgers.add(entry);

            RedemptionLine line = RedemptionLine.builder()
                    .redemptionId(redemption.getId())
                    .pointLedgerId(entry.getId())
                    .pointsUsed(consume)
                    .build();
            redemptionLines.add(line);
        }

        pointLedgerRepository.saveAll(modifiedLedgers);
        redemptionLineRepository.saveAll(redemptionLines);

        return RedemptionResponse.builder()
                .id(redemption.getId())
                .customerId(customerId)
                .rewardName(reward.getName())
                .pointsSpent(pointsCost)
                .status(redemption.getStatus())
                .createdAt(redemption.getCreatedAt())
                .build();
    }

    public int earnPoints(Long customerId, Long purchaseId, BigDecimal amount, LocalDateTime purchasedAt) {
        int points = amount.intValue();

        PointLedger entry = PointLedger.builder()
                .customerId(customerId)
                .purchaseId(purchaseId)
                .points(points)
                .pointsRemaining(points)
                .type(LedgerType.EARN)
                .expiresAt(purchasedAt.plusMonths(12))
                .build();

        pointLedgerRepository.save(entry);
        return points;
    }

    @Transactional
    public int clawbackPoints(Long purchaseId) {
        List<PointLedger> earnEntries = pointLedgerRepository.findByPurchaseId(purchaseId);
        int totalClawedBack = 0;

        List<PointLedger> toSave = new ArrayList<>();

        for (PointLedger earn : earnEntries) {
            if (earn.getType() != LedgerType.EARN) continue;
            if (earn.getPointsRemaining() <= 0) continue;

            int clawbackPoints = earn.getPointsRemaining();
            totalClawedBack += clawbackPoints;

            PointLedger clawback = PointLedger.builder()
                    .customerId(earn.getCustomerId())
                    .purchaseId(purchaseId)
                    .points(clawbackPoints)
                    .pointsRemaining(0)
                    .type(LedgerType.CLAWBACK)
                    .expiresAt(earn.getExpiresAt())
                    .build();

            earn.setPointsRemaining(0);
            toSave.add(earn);
            toSave.add(clawback);
        }

        pointLedgerRepository.saveAll(toSave);
        return totalClawedBack;
    }
}
