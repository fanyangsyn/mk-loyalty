package com.mark43.loyalty.controller;

import com.mark43.loyalty.dto.RewardResponse;
import com.mark43.loyalty.entity.RewardCatalog;
import com.mark43.loyalty.repository.RewardCatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rewards")
@RequiredArgsConstructor
public class RewardCatalogController {

    private final RewardCatalogRepository rewardCatalogRepository;

    @GetMapping
    public ResponseEntity<List<RewardResponse>> listRewards() {
        List<RewardResponse> rewards = rewardCatalogRepository.findByActiveTrue().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(rewards);
    }

    private RewardResponse toResponse(RewardCatalog entity) {
        return RewardResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .pointsCost(entity.getPointsCost())
                .active(entity.isActive())
                .build();
    }
}
