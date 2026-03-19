package com.velo.courrier.ops;

import com.velo.courrier.tracking.dto.TrackingResponse;
import com.velo.courrier.tracking.service.TrackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminOpsService {

    private final StringRedisTemplate redisTemplate;
    // Inject Booking/Driver repos as necessary to build the unified JSON payloads
    
    // Simplistic snapshot method for all Active Geo markers
    public Map<String, Object> getGlobalLiveMap() {
        Map<String, Object> opsSnapshot = new HashMap<>();
        
        // Scan driver_locations GEO store (e.g. radius of 10,000 KM acting as global pull)
        GeoResults<RedisGeoCommands.GeoLocation<String>> allDrivers = redisTemplate.opsForGeo()
            .radius("driver_locations", new Circle(new Point(0, 0), 20000000));
            
        List<Map<String, Object>> mappedDrivers = allDrivers.getContent().stream().map(loc -> {
             Map<String, Object> driverNode = new HashMap<>();
             driverNode.put("driverId", loc.getContent().getName());
             driverNode.put("lat", loc.getContent().getPoint().getY());
             driverNode.put("lng", loc.getContent().getPoint().getX());
             // For scaffold: query full state or assume IN_TRANSIT if in Redis
             driverNode.put("status", "AVAILABLE"); 
             return driverNode;
        }).collect(Collectors.toList());

        opsSnapshot.put("drivers", mappedDrivers);
        opsSnapshot.put("activeBookings", List.of()); // Hydrate with actual LIVE bookings
        opsSnapshot.put("timestamp", System.currentTimeMillis());

        return opsSnapshot;
    }
}
