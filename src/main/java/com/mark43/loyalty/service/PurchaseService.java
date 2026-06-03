package com.mark43.loyalty.service;

import com.mark43.loyalty.dto.PurchaseRequest;
import com.mark43.loyalty.dto.PurchaseResponse;
import com.mark43.loyalty.dto.RefundResponse;
import com.mark43.loyalty.entity.Purchase;
import com.mark43.loyalty.enums.PurchaseStatus;
import com.mark43.loyalty.exception.PurchaseAlreadyRefundedException;
import com.mark43.loyalty.exception.ResourceNotFoundException;
import com.mark43.loyalty.repository.CustomerRepository;
import com.mark43.loyalty.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final CustomerRepository customerRepository;
    private final PointService pointService;

    @Transactional
    public PurchaseResponse createPurchase(PurchaseRequest request) {
        customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));

        LocalDateTime purchasedAt = request.getPurchasedAt() != null ? request.getPurchasedAt() : LocalDateTime.now();

        Purchase purchase = Purchase.builder()
                .customerId(request.getCustomerId())
                .amount(request.getAmount())
                .referenceNumber(request.getReferenceNumber())
                .status(PurchaseStatus.COMPLETED)
                .purchasedAt(purchasedAt)
                .build();

        purchase = purchaseRepository.save(purchase);

        int pointsEarned = pointService.earnPoints(
                request.getCustomerId(),
                purchase.getId(),
                request.getAmount(),
                purchasedAt
        );

        return PurchaseResponse.builder()
                .id(purchase.getId())
                .customerId(purchase.getCustomerId())
                .amount(purchase.getAmount())
                .referenceNumber(purchase.getReferenceNumber())
                .status(purchase.getStatus().name())
                .pointsEarned(pointsEarned)
                .purchasedAt(purchase.getPurchasedAt())
                .build();
    }

    @Transactional
    public RefundResponse refundPurchase(String referenceNumber) {
        Purchase purchase = purchaseRepository.findByReferenceNumber(referenceNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found with reference: " + referenceNumber));

        if (purchase.getStatus() == PurchaseStatus.REFUNDED) {
            throw new PurchaseAlreadyRefundedException("Purchase already refunded: " + referenceNumber);
        }

        purchase.setStatus(PurchaseStatus.REFUNDED);
        purchase.setRefundedAt(LocalDateTime.now());
        purchaseRepository.save(purchase);

        int pointsClawedBack = pointService.clawbackPoints(purchase.getId());

        return RefundResponse.builder()
                .purchaseId(purchase.getId())
                .referenceNumber(purchase.getReferenceNumber())
                .amountRefunded(purchase.getAmount())
                .pointsClawedBack(pointsClawedBack)
                .status(PurchaseStatus.REFUNDED.name())
                .build();
    }
}
