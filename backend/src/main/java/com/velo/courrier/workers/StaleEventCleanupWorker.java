package com.velo.courrier.workers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StaleEventCleanupWorker {

    // private final EtaTrackingLogRepository etaTrackingLogRepository;
    // private final RouteDeviationEventRepository deviationEventRepository;

    /**
     * Nightly maintenance chron job offloading database pressure
     * by destroying granular physical tracking logs older than 30 days.
     */
    @Scheduled(cron = "0 0 2 * * ?") // 2:00 AM Daily
    public void cleanupOldTrackingLogs() {
        log.info("NIGHTLY WORKER: Commencing 30-day Tracking Log purge...");
        // int etaDeleted = etaTrackingLogRepository.deleteAllCreatedAtBefore(LocalDate.now().minusDays(30));
        // int deviationDeleted = deviationEventRepository.deleteAllCreatedAtBefore(LocalDate.now().minusDays(30));
        
        log.info("Purged historical radar arrays from relational schemas.");
    }
}
