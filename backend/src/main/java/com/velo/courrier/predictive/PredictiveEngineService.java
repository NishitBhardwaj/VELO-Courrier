package com.velo.courrier.predictive;

import com.velo.courrier.booking.BookingEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PredictiveEngineService {

    private final BookingEventPublisher eventPublisher;

    // @CircuitBreaker(name="mapsApi", fallbackMethod="cacheFallback")
    public void recomputeEta(UUID bookingId, Double driverLat, Double driverLng, Double destLat, Double destLng) {
        // 1. Mock External Maps API Call
        // In reality: mapsClient.getDirections(driverLocation, nextStopLocation);
        int computedEtaSeconds = 900; // Simulated 15 minutes
        int originalEtaSeconds = 600; // Expected 10 minutes
        int delaySeconds = computedEtaSeconds - originalEtaSeconds;

        if (delaySeconds > 300) { // 5 minutes threshold
            log.warn("ETA Threshold Crossed | Booking {} delayed by {} seconds", bookingId, delaySeconds);
            // In reality: Save to EtaTrackingLog repository & AlertEvent repository
            
            // Fire WebSockets
            eventPublisher.publishV2EtaUpdate(bookingId, computedEtaSeconds, delaySeconds);
        }
    }

    public void cacheFallback(UUID bookingId, Double driverLat, Double driverLng, Double destLat, Double destLng, Throwable t) {
        log.warn("Circuit Breaker Tripped! External Maps API is failing. Using cached ETA for {}", bookingId);
        // Returns the last known ETA from Redis instead of recalculating
    }

    public void detectRouteDeviation(UUID bookingId, UUID driverId, Double driverLat, Double driverLng) {
        // 1. Fetch exact polyline trace bounds from cache/DB
        // 2. Compute Point-To-Line geometric distance
        int deviationDistanceMeters = 350; // Simulated off-route metric

        if (deviationDistanceMeters > 100) {
            String severity = deviationDistanceMeters > 500 ? "CRITICAL" : 
                              deviationDistanceMeters > 300 ? "HIGH" : "WARNING";

            log.warn("DEVIATION DETECTED: Booking {} | Driver {} | Dist: {}m | Sev: {}", 
                     bookingId, driverId, deviationDistanceMeters, severity);

            // In reality: Save to RouteDeviationEvent repository & AlertEvent repository
            
            // Fire WebSockets
            eventPublisher.publishV2DeviationAlert(bookingId, severity, deviationDistanceMeters);
        }
    }
}
