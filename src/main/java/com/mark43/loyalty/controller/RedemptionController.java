package com.mark43.loyalty.controller;

import com.mark43.loyalty.dto.RedemptionRequest;
import com.mark43.loyalty.dto.RedemptionResponse;
import com.mark43.loyalty.service.PointService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/redemptions")
@RequiredArgsConstructor
public class RedemptionController {

    private final PointService pointService;

    @PostMapping
    public ResponseEntity<RedemptionResponse> redeemPoints(@Valid @RequestBody RedemptionRequest request) {
        RedemptionResponse response = pointService.redeemPoints(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
