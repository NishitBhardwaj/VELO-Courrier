package com.velo.courrier.governance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverDocumentService {

    private final DriverDocumentRepository documentRepository;
    private final DriverRepository driverRepository;
    private final TransactionalEventPublisher outboxPublisher;

    /**
     * Governs document expiration policies cleanly.
     * Fires nightly directly sweeping the document signatures mapping invalidation states.
    @Scheduled(cron = "0 30 2 * * ?") // 2:30 AM Daily
    public void executeDocumentExpiryLifecycle() {
        log.info("GOVERNANCE DAEMON: Commencing KYC Driver Expiration sweeping routine...");
        
        LocalDate today = LocalDate.now();

        // 1. Identify EXPIRED documents natively
        // List<DriverDocument> expiredDocs = documentRepository.findAllByExpiryDateBeforeAndStatus(today, "APPROVED");

        for (DriverDocument doc : expiredDocs) {
            
            // Mark Document Stale
            doc.setStatus("EXPIRED");
            documentRepository.save(doc);

            // Fetch Driver & Force Offline State
            Driver driver = driverRepository.findById(doc.getDriverId()).orElseThrow();
            driver.setVerificationStatus("SUSPENDED_KYC");
            driver.setAvailabilityStatus("OFFLINE");
            driverRepository.save(driver);
            
            // Issue V9 Outbox Relay triggering immediate Kafka Mobile-Push invalidation hook
            outboxPublisher.publishEvent("DRIVER", driver.getId(), "KYC_EXPIRED", Map.of(
                 "driverId", driver.getId(),
                 "documentType", doc.getDocumentType()
            ));

            log.warn("governance_action: Suspended Operations for Driver {}. Document {} Expired.", driver.getId(), doc.getDocumentType());
        }

        // 2. T-7 Days Reminder Push Sweep
        // ... (Similar logic sweeping today.plusDays(7) hooking outbox Publisher reminder payloads)

        log.info("KYC Driver Expiration sweep cleanly closed.");
    }
}
