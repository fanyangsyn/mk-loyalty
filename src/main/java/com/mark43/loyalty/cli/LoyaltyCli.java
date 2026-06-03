package com.mark43.loyalty.cli;

import com.mark43.loyalty.dto.*;
import com.mark43.loyalty.entity.RewardCatalog;
import com.mark43.loyalty.repository.RewardCatalogRepository;
import com.mark43.loyalty.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class LoyaltyCli {

    private final CustomerService customerService;
    private final PurchaseService purchaseService;
    private final PointService pointService;
    private final RewardCatalogRepository rewardCatalogRepository;

    @ShellMethod(key = "register", value = "Register a new customer")
    public String register(
            @ShellOption("--email") String email,
            @ShellOption("--first") String firstName,
            @ShellOption("--last") String lastName) {
        CustomerRequest request = new CustomerRequest();
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setEmail(email);
        CustomerResponse response = customerService.createCustomer(request);
        return String.format("Customer registered: ID=%d, Name=%s %s, Email=%s",
                response.getId(), response.getFirstName(), response.getLastName(), response.getEmail());
    }

    @ShellMethod(key = "purchase", value = "Record a purchase and earn points")
    public String purchase(
            @ShellOption("--customer-id") Long customerId,
            @ShellOption("--amount") BigDecimal amount,
            @ShellOption("--ref") String referenceNumber) {
        PurchaseRequest request = new PurchaseRequest();
        request.setCustomerId(customerId);
        request.setAmount(amount);
        request.setReferenceNumber(referenceNumber);
        request.setPurchasedAt(LocalDateTime.now());
        PurchaseResponse response = purchaseService.createPurchase(request);
        return String.format("Purchase recorded: ID=%d, Amount=$%.2f, Points Earned=%d",
                response.getId(), response.getAmount(), response.getPointsEarned());
    }

    @ShellMethod(key = "balance", value = "Check customer balance and tier")
    public String balance(@ShellOption("--customer-id") Long customerId) {
        BalanceResponse response = pointService.getBalance(customerId);
        return String.format("Customer ID=%d | Balance: %d points | Tier: %s | Rolling 12-month spend: $%.2f",
                response.getCustomerId(), response.getAvailablePoints(),
                response.getTier(), response.getRollingSpend());
    }

    @ShellMethod(key = "redeem", value = "Redeem points for a reward")
    public String redeem(
            @ShellOption("--customer-id") Long customerId,
            @ShellOption("--reward-id") Long rewardId) {
        RedemptionRequest request = new RedemptionRequest();
        request.setCustomerId(customerId);
        request.setRewardId(rewardId);
        RedemptionResponse response = pointService.redeemPoints(request);
        return String.format("Redemption successful: ID=%d, Reward='%s', Points Spent=%d",
                response.getId(), response.getRewardName(), response.getPointsSpent());
    }

    @ShellMethod(key = "refund", value = "Refund a purchase and claw back points")
    public String refund(@ShellOption("--ref") String referenceNumber) {
        RefundResponse response = purchaseService.refundPurchase(referenceNumber);
        return String.format("Refund processed: Purchase ID=%d, Ref=%s, Amount=$%.2f, Points Clawed Back=%d",
                response.getPurchaseId(), response.getReferenceNumber(),
                response.getAmountRefunded(), response.getPointsClawedBack());
    }

    @ShellMethod(key = "rewards", value = "List available rewards")
    public String rewards() {
        List<RewardCatalog> rewardList = rewardCatalogRepository.findByActiveTrue();
        if (rewardList.isEmpty()) {
            return "No rewards available.";
        }
        StringBuilder sb = new StringBuilder("Available Rewards:\n");
        sb.append(String.format("%-5s %-30s %-15s %s%n", "ID", "Name", "Points Cost", "Description"));
        sb.append("-".repeat(80)).append("\n");
        for (RewardCatalog r : rewardList) {
            sb.append(String.format("%-5d %-30s %-15d %s%n",
                    r.getId(), r.getName(), r.getPointsCost(), r.getDescription()));
        }
        return sb.toString();
    }
}
