package com.velo.courrier.tracking.service;

import com.velo.courrier.booking.Booking;
import com.velo.courrier.booking.BookingRepository;
import com.velo.courrier.tracking.dto.TrackingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackingService {

    private final BookingRepository bookingRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String GEO_KEY = "drivers:locations";

    public TrackingResponse getTrackingInfo(UUID bookingId, String userIdStr, boolean isAdmin) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));

        if (!isAdmin && !booking.getCustomer().getId().toString().equals(userIdStr)) {
            log.warn("Unauthorized tracking access attempt by user {} for booking {}", userIdStr, bookingId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only track your own bookings");
        }

        if (booking.getDriver() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No driver assigned to this booking yet");
        }

        String driverIdStr = booking.getDriver().getId().toString();
        
        List<Point> positions = redisTemplate.opsForGeo().position(GEO_KEY, driverIdStr);
        
        TrackingResponse.DriverPoint driverPoint = null;
        if (positions != null && !positions.isEmpty() && positions.get(0) != null) {
            Point p = positions.get(0);
            driverPoint = TrackingResponse.DriverPoint.builder()
                    .lat(p.getY())
                    .lng(p.getX())
                    .build();
        } else {
            // In a real scenario, fallback to booking_tracking_snapshots table or last known driver location in PostgreSQL
            driverPoint = TrackingResponse.DriverPoint.builder()
                    .lat(0.0)
                    .lng(0.0)
                    .build();
        }

        // Return a mock ETA of 600 for now. Over time, integrate Google Distance Matrix API
        return TrackingResponse.builder()
                .driver(driverPoint)
                .status(booking.getStatus().name())
                .eta(600)
                .build();
    }
}
