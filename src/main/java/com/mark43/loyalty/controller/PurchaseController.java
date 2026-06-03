package com.mark43.loyalty.controller;

import com.mark43.loyalty.dto.PurchaseRequest;
import com.mark43.loyalty.dto.PurchaseResponse;
import com.mark43.loyalty.dto.RefundResponse;
import com.mark43.loyalty.service.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity<PurchaseResponse> createPurchase(@Valid @RequestBody PurchaseRequest request) {
        PurchaseResponse response = purchaseService.createPurchase(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{referenceNumber}/refund")
    public ResponseEntity<RefundResponse> refundPurchase(@PathVariable String referenceNumber) {
        RefundResponse response = purchaseService.refundPurchase(referenceNumber);
        return ResponseEntity.ok(response);
    }
}
