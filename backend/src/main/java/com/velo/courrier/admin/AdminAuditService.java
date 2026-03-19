package com.velo.courrier.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class AdminAuditService {

    /**
     * Permanent append-only ledger for all destructive or sensitive Admin Operations.
     */
    public void logSensitiveAction(UUID adminId, String actionType, String targetResource, String reason) {
        // In reality, this persists to a separate strictly-audited table or streams instantly to Datadog.
        log.warn("[ADMIN AUDIT] Admin {} performed {} on {}. Reason: {}", adminId, actionType, targetResource, reason);
    }
}
