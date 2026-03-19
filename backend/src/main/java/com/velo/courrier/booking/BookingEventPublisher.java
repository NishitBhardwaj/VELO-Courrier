package com.velo.courrier.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingEventPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    // V1 Legacy payload
    public void publishV1StatusUpdate(UUID bookingId, String status) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "v1.booking.status");
        payload.put("bookingId", bookingId.toString());
        payload.put("status", status);

        log.info("Broadcasting V1 booking event to /topic/booking/{}", bookingId);
        messagingTemplate.convertAndSend("/topic/booking/" + bookingId, payload);
    }

    // V2 Multi-stop payload
    public void publishV2StopUpdate(UUID bookingId, UUID stopId, Integer stopOrder, String status, Integer nextStopOrder) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "v2.booking.stop.updated");
        payload.put("bookingId", bookingId.toString());
        payload.put("stopId", stopId != null ? stopId.toString() : null);
        payload.put("stopOrder", stopOrder);
        payload.put("status", status);
        payload.put("nextStopOrder", nextStopOrder);

        log.info("Broadcasting V2 multi-stop event to /topic/booking/{}", bookingId);
        messagingTemplate.convertAndSend("/topic/booking/" + bookingId, payload);
    }

    // V2 Predictive ETA Updates
    public void publishV2EtaUpdate(UUID bookingId, Integer currentEtaSeconds, Integer delaySeconds) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "v2.booking.eta.updated");
        payload.put("etaSeconds", currentEtaSeconds);
        payload.put("delaySeconds", delaySeconds);

        log.info("Broadcasting V2 ETA recalculation to /topic/booking/{}", bookingId);
        messagingTemplate.convertAndSend("/topic/booking/" + bookingId, payload);
    }

    // V2 Predictive Deviation Updates
    public void publishV2DeviationAlert(UUID bookingId, String severity, Integer distanceMeters) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "v2.booking.deviation.alert");
        payload.put("severity", severity);
        payload.put("distanceMeters", distanceMeters);

        log.info("Broadcasting V2 DEVIATION alert to anomaly queues /topic/booking/{}", bookingId);
        messagingTemplate.convertAndSend("/topic/booking/" + bookingId, payload);
    }
}
