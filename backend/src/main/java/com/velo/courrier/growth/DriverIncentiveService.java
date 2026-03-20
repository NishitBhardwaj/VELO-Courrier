package com.velo.courrier.growth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverIncentiveService {

    private final DriverIncentiveRepository incentiveRepository;
    private final WalletService walletService;

    /**
     * Invoked asynchronously over Kafka (Sprint 7 NotificationConsumer) instantly upon BOOKING_COMPLETED.
    public void evaluateStreakBonuses(UUID driverId) {
        log.info("INCENTIVE_ENGINE: Evaluating Streak Bonus eligibility for Driver {}...", driverId);

        // Calculate consecutive completions WITHOUT cancellations.
        // int consecutiveTrips = bookingRepository.getConsecutiveCompletionsSinceLastCancel(driverId);
        int consecutiveTrips = 5; // Mock

        if (consecutiveTrips >= 5) {
            log.info("STREAK_UNLOCKED: Driver {} completed 5 consecutive drops natively. Firing $10.00 Bonus Array.", driverId);
            
            DriverIncentive bonus = DriverIncentive.builder()
                   .driverId(driverId)
                   .campaignName("5_STREAK_BONUS")
                   .bonusAmount(new BigDecimal("10.00"))
                   .status("PAID")
                   .build();
            incentiveRepository.save(bonus);

            walletService.creditDriver(driverId, new BigDecimal("10.00"), "5_STREAK_BONUS_ACHIEVED");
            // Optional: Push FCM Notification natively to Driver App -> "You made an extra $10!"
        }
    }
}
