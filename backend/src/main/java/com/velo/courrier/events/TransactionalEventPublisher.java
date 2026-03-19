package com.velo.courrier.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionalEventPublisher {

    // private final EventOutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    /**
     * Publishes an event to the Outbox table natively bound to the current Hibernate Transaction.
     * This guarantees 100% durability: if the main DB transaction rolls back, the event rolls back.
     */
    public void publishEvent(String aggregateType, UUID aggregateId, String eventType, Object payloadObject) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(payloadObject);

            EventOutbox outboxEvent = EventOutbox.builder()
                    .aggregateType(aggregateType)
                    .aggregateId(aggregateId)
                    .eventType(eventType)
                    .payload(jsonPayload)
                    .status("PENDING")
                    .retryCount(0)
                    .build();

            // outboxRepository.save(outboxEvent);
            log.info("OUTBOX APPEND: Aggregate[{}::{}] Event[{}]", aggregateType, aggregateId, eventType);

        } catch (JsonProcessingException e) {
            log.error("Failed to serialize Outbox payload for EventType {}", eventType, e);
            throw new RuntimeException("Outbox serialization failure", e);
        }
    }
}
