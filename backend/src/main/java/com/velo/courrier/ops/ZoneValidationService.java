package com.velo.courrier.ops;

import com.velo.courrier.booking.dto.BookingRequest;
import com.velo.courrier.booking.dto.MultiStopBookingRequest;
import com.velo.courrier.booking.dto.StopDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ZoneValidationService {

    private final ServiceZoneRepository zoneRepository;
    
    // In a production app, we would use PostGIS ST_Contains or a Ray-Casting algorithm in Java
    // to check if the point {lat, lng} is inside the `polygon_geojson`

    public boolean validateSingleStopRequest(BookingRequest request) {
        log.info("Validating booking pickup/dropoff against active Service Zones...");
        // Mocking intersection validation pass
        return true;
    }

    public boolean validateMultiStopRequest(MultiStopBookingRequest request) {
        log.info("Validating {} stops against active Service Zones...", request.getStops().size());
        for (StopDto stop : request.getStops()) {
            if (!isPointInActiveZone(stop.getAddressJson())) {
                return false;
            }
        }
        return true;
    }

    private boolean isPointInActiveZone(String addressJson) {
        // Pseudo-logic:
        // 1. Extract lat/lng from addressJson
        // 2. Query PostGIS: SELECT COUNT(*) FROM service_zones WHERE ST_Contains(ST_GeomFromGeoJSON(polygon_geojson), ST_MakePoint(lng, lat)) AND active = true;
        // 3. Return true if count > 0
        return true; // Scaffold
    }

    public void logZoneBreach(UUID driverId, UUID bookingId, String note) {
        log.warn("ZONE BREACH DETECTED: Driver {} | Booking {} | {}", driverId, bookingId, note);
        // Persist to ZoneEvent repository
    }
}
