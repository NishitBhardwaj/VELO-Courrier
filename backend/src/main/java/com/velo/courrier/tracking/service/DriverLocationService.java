package com.velo.courrier.tracking.service;

import com.velo.courrier.tracking.dto.DriverLocationRequest;
import com.velo.courrier.tracking.entity.DriverLocation;
import com.velo.courrier.tracking.repository.DriverLocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverLocationService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final DriverLocationRepository repository;
    private final SimpMessagingTemplate messagingTemplate;

    private static final String GEO_KEY = "drivers:locations";

    public void updateLocation(String driverIdStr, DriverLocationRequest request) {
        UUID driverId;
        try {
            driverId = UUID.fromString(driverIdStr);
        } catch (IllegalArgumentException e) {
            log.error("Invalid driver ID format: {}", driverIdStr);
            return;
        }

        // 1. Store in Redis GEO
        redisTemplate.opsForGeo().add(GEO_KEY, new Point(request.getLongitude(), request.getLatitude()), driverIdStr);

        // 2. Maintain last_seen with TTL
        String lastSeenKey = "driver:last_seen:" + driverIdStr;
        redisTemplate.opsForValue().set(lastSeenKey, System.currentTimeMillis(), 45, TimeUnit.SECONDS);

        // 3. Save to PostgreSQL for history
        DriverLocation location = DriverLocation.builder()
                .driverId(driverId)
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .heading(request.getHeading())
                .speed(request.getSpeed())
                .accuracy(request.getAccuracy())
                .build();
        repository.save(location);

        // 4. Publish WebSocket Event
        // Typically driver broadcasts to their own channel for tracking and to a specific booking channel 
        // if active. For now, we publish a generic location event for the driver.
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "v1.driver.location");
        payload.put("driverId", driverIdStr);
        payload.put("lat", request.getLatitude());
        payload.put("lng", request.getLongitude());
        payload.put("heading", request.getHeading());
        payload.put("timestamp", System.currentTimeMillis());

        messagingTemplate.convertAndSend("/topic/driver/" + driverIdStr, payload);
    }
}
