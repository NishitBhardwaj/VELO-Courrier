package com.velo.courrier.dispatch;

import com.velo.courrier.booking.Booking;
import com.velo.courrier.booking.BookingRepository;
import com.velo.courrier.common.enums.BookingStatus;
import com.velo.courrier.common.event.BookingConfirmedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DispatchService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;
    private final BookingRepository bookingRepository;

    @Async
    @EventListener
    public void handleBookingConfirmed(BookingConfirmedEvent event) {
        dispatchBooking(event.getBookingId());
    }

    public void dispatchBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getPickupAddress() == null || booking.getPickupAddress().getLatitude() == null) {
            log.error("Cannot dispatch booking {} without coordinates", bookingId);
            return;
        }

        double pickupLat = booking.getPickupAddress().getLatitude();
        double pickupLon = booking.getPickupAddress().getLongitude();

        Distance radius = new Distance(5.0, Metrics.KILOMETERS);

        RedisGeoCommands.GeoSearchCommandArgs args = RedisGeoCommands.GeoSearchCommandArgs.newGeoSearchArgs()
                .sortAscending().limit(10); // Find top 10 nearest quickly

        RedisGeoCommands.GeoReference<String> reference = RedisGeoCommands.GeoReference.fromCoordinate(pickupLon, pickupLat);
        RedisGeoCommands.GeoShape shape = RedisGeoCommands.GeoShape.byRadius(radius);

        GeoResults<RedisGeoCommands.GeoLocation<Object>> nearestDrivers = 
                redisTemplate.opsForGeo().search("driver_locations", reference, shape, args);

        if (nearestDrivers == null || nearestDrivers.getContent().isEmpty()) {
            log.warn("No drivers found within 5km for booking {}", bookingId);
            return;
        }

        String targetDriverId = (String) nearestDrivers.getContent().get(0).getContent().getName();
        
        log.info("Offering booking {} to driver {}", bookingId, targetDriverId);
        
        // Push notification (Dispatch Offer) via WebSocket to driver's private queue
        messagingTemplate.convertAndSendToUser(
                targetDriverId, 
                "/queue/dispatch.offers", 
                booking
        );
    }
}
