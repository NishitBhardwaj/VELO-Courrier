package com.velo.courrier.tracking;

import com.velo.courrier.tracking.dto.DriverLocationRequest;
import com.velo.courrier.tracking.service.DriverLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.velo.courrier.tracking.dto.TrackingResponse;
import com.velo.courrier.tracking.service.TrackingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TrackingController {

    private final DriverLocationService driverLocationService;
    private final TrackingService trackingService;

    // HTTP Endpoint for driver location updates as per Step 1 constraints
    @PostMapping("/drivers/location")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<Void> updateLocationHttp(
            Authentication authentication,
            @RequestBody DriverLocationRequest request
    ) {
        String driverId = authentication.getName();
        driverLocationService.updateLocation(driverId, request);
        return ResponseEntity.ok().build();
    }

    // Step 3 Fallback API
    @GetMapping("/bookings/{id}/tracking")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<TrackingResponse> getBookingTracking(
            @PathVariable("id") UUID id,
            Authentication authentication
    ) {
        String userId = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        TrackingResponse response = trackingService.getTrackingInfo(id, userId, isAdmin);
        return ResponseEntity.ok(response);
    }

    // WebSocket Endpoint alternative
    @MessageMapping("/tracking.ping")
    public void updateLocationWs(Authentication authentication, DriverLocationRequest request) {
        if (authentication != null && authentication.getName() != null) {
            driverLocationService.updateLocation(authentication.getName(), request);
        }
    }
}
