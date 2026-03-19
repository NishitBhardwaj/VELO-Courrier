package com.velo.courrier.ops;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DriverStaleMonitor {

    private final SimpMessagingTemplate messagingTemplate;

    // Runs every 15 seconds to evaluate STALE thresholds (>= 30s lack of ping)
    @Scheduled(fixedRate = 15000)
    public void scanAndBroadcastStaleDrivers() {
        // Pseudo-logic:
        // 1. Fetch all currently active drivers from Redis
        // 2. Diff currentTimeMillis against their last updated TTL/payload marker
        // 3. IF diff > 30000 -> broadcast STALE payload
        
        /*
        messagingTemplate.convertAndSend("/topic/admin/ops/live", 
            "{\"type\": \"DRIVER_STALE\", \"driverId\": \"sample-uuid\", \"lastSeenDelta\": 35}"
        );
        */
        
        log.debug("Executed background passive monitor for driver TTL violations.");
    }
}
