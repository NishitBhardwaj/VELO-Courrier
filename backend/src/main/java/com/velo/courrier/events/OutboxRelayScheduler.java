package com.velo.courrier.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxRelayScheduler {

    private final EventOutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelayString = "2000") // Run every 2 seconds
    public void processPendingOutboxEvents() {
        // Pseudocode: 
        // 1. Fetch TOP 50 WHERE status = 'PENDING' FOR UPDATE SKIP LOCKED
        // 2. Iterate and publish to Kafka Topic depending on eventType
        // 3. Mark status = 'PUBLISHED'

        log.debug("OutboxRelayScheduler tick - scanning for PENDING transactional events...");

        List<EventOutbox> pendingEvents = outboxRepository.findTop50ByStatusOrderByCreatedAtAsc("PENDING");
        for (EventOutbox event : pendingEvents) {
            try {
                // E.g., topic name translates to 'velo.booking.events'
                String topic = "velo." + event.getAggregateType().toLowerCase() + ".events";
                
                kafkaTemplate.send(topic, event.getAggregateId().toString(), event.getPayload());
                
                event.setStatus("PUBLISHED");
                outboxRepository.save(event);
                
                log.info("Relayed Event [{}] {} to Topic {}", event.getId(), event.getEventType(), topic);
            } catch (Exception e) {
                event.setRetryCount(event.getRetryCount() + 1);
                if (event.getRetryCount() >= 3) {
                    event.setStatus("FAILED"); // Shunted to dead-letter review later
                }
                outboxRepository.save(event);
                log.error("Failed to relay event to Kafka", e);
            }
        }
    }
}
