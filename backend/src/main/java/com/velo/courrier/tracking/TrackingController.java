package com.velo.courrier.tracking;

import com.velo.courrier.tracking.dto.LocationUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tracking")
@RequiredArgsConstructor
public class TrackingController {

    private final RedisTemplate<String, Object> redisTemplate;

    // HTTP Endpoint for fallback updates
    @PostMapping("/ping")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<Void> updateLocationHttp(
            Authentication authentication,
            @RequestBody LocationUpdateRequest request
    ) {
        String driverId = authentication.getName();
        updateRedisGeo(driverId, request.getLat(), request.getLon());
        return ResponseEntity.ok().build();
    }

    // WebSocket Endpoint for high-frequency updates
    @MessageMapping("/tracking.ping")
    public void updateLocationWs(Authentication authentication, LocationUpdateRequest request) {
        if (authentication != null && authentication.getName() != null) {
            updateRedisGeo(authentication.getName(), request.getLat(), request.getLon());
        }
    }

    private void updateRedisGeo(String driverId, double lat, double lon) {
        // In real app, we'd also store the driver's vehicle category in Redis hash
        // For matching, we index the driver's location
        redisTemplate.opsForGeo().add("driver_locations", new Point(lon, lat), driverId);
    }
}
