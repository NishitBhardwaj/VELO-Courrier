package com.velo.courrier.events.consumers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
// import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumerService {

    // private final NotificationQueueRepository notificationQueueRepository;
    // private final FcmPushClient fcmPushClient;

    /**
     * Fully decoupled from the main Booking Saving JVM.
     * Consumes native Kafka messages originally produced by the OutboxRelayScheduler.
     */
    // @KafkaListener(topics = "velo.booking.events", groupId = "notification-group")
    public void consumeBookingEvents(String messagePayload) {
        log.info("NOTIFICATION-CONSUMER RECEIVED Payload: {}", messagePayload);

        // Pseudocode: 
        // 1. Parse JSON payload
        // 2. Determine Event Type (e.g. BOOKING_CREATED, MANUAL_ASSIGNMENT_COMPLETED)
        // 3. Fire FCM Push Notification logic
        
        /*
        try {
            Map<String, Object> eventData = objectMapper.readValue(messagePayload, Map.class);
            String eventType = (String) eventData.get("eventType");
            
            if ("MANUAL_ASSIGNMENT_COMPLETED".equals(eventType)) {
                // Native Push to Driver
                fcmPushClient.sendPush(
                        (String) eventData.get("driverId"), 
                        "Admin Reassignment", 
                        "You have been manually dispatched to Booking ID " + eventData.get("bookingId")
                );
            }

            // Acknowledge Kafka Offset implicitly via Spring
        } catch (Exception e) {
            // Dead Letter Queue Fallback: write to notification_queue
            log.error("Notification dispatch failed, sending to DLQ.", e);
            // notificationQueueRepository.save(...);
            throw new RuntimeException("Triggering Kafka redelivery sequence", e);
        }
        */
    }
}
