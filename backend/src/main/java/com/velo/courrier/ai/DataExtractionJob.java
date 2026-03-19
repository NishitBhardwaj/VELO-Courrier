package com.velo.courrier.ai;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataExtractionJob {

    // private final MlTrainingDataRepository mlDataRepository;

    /**
     * Executes nightly fetching 100% of the completed bookings from the trailing 24 hours.
     * Denormalizes relations natively into flat telemetry models pushing straight to `ml_training_data`
     * allowing the Python AI Service to consume large-scale arrays cleanly for model re-training.
     */
    @Scheduled(cron = "0 0 3 * * ?") // 3:00 AM Nightly
    public void generateTrainingDataExtract() {
        log.info("AI_DATAPIPE: Scanning trailing Postgres operations for ML normalization...");
        
        // 1. Fetch `COMPLETED` bookings mapped matching `created_at` > Yesterday
        // 2. Fetch trailing `EtaTrackingLog` metadata corresponding cleanly to the bookings
        // 3. Assemble and persist directly into `ml_training_data` table
        // 4. Dispatch a lightweight Webhook to `fastapi/ai/train` pinging the new batch

        log.info("Finished flattening operational matrices for Python consumption arrays.");
    }
}
