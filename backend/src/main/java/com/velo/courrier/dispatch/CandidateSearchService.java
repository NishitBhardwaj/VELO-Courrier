package com.velo.courrier.dispatch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandidateSearchService {

    private final StringRedisTemplate redisTemplate;

    public List<Map<String, Object>> findCandidatesForBooking(UUID bookingId, Double pickupLat, Double pickupLng) {
        // Pseudo-logic:
        // 1. Fetch available drivers within 10KM of pickup bounds
        // 2. Filter out STALE drivers using the TTL logs implemented in Sprint 4
        // 3. Filter by required Vehicle capabilities

        GeoResults<RedisGeoCommands.GeoLocation<String>> candidates = redisTemplate.opsForGeo()
                .radius("driver_locations", new Circle(new Point(pickupLng, pickupLat), 10000));

        List<Map<String, Object>> filteredDrivers = new ArrayList<>();
        
        if (candidates != null) {
            candidates.forEach(loc -> {
                String driverId = loc.getContent().getName();
                
                // Sprint 6: Stale Check
                // boolean isStale = redisTemplate.hasKey("stale_driver:" + driverId);
                // if (!isStale) { ... add to list ... }

                filteredDrivers.add(Map.of(
                    "driverId", driverId,
                    "distanceMeters", loc.getDistance().getValue(),
                    "status", "AVAILABLE",  // Mock enum wrap
                    "etaSeconds", (int) (loc.getDistance().getValue() / 5) // Mock routing math
                ));
            });
        }

        return filteredDrivers;
    }
}
