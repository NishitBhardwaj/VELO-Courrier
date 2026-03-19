package com.velo.courrier.booking.multistop;

import com.velo.courrier.booking.dto.MultiStopBookingRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MultiStopBookingController {

    private final MultiStopBookingService multiStopBookingService;

    // 1. Create multi-stop booking
    @PostMapping("/bookings/multi-stop")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> createMultiStopBooking(
            @Valid @RequestBody MultiStopBookingRequest request,
            Authentication authentication
    ) {
        String customerId = authentication.getName();
        return ResponseEntity.ok(multiStopBookingService.createBooking(request, customerId));
    }

    // 2. Fetch booking with timeline and stops is integrated via an overarching GET /bookings/{id} 
    // OR we provide a specific endpoint for the timeline
    @GetMapping("/bookings/{id}/timeline")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'DRIVER', 'ADMIN')")
    public ResponseEntity<?> getBookingTimeline(@PathVariable("id") UUID bookingId) {
        return ResponseEntity.ok(multiStopBookingService.getTimelineForBooking(bookingId));
    }

    // 3. Update Stop Status
    @PatchMapping("/bookings/{id}/stops/{stopId}/status")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<?> updateStopStatus(
            @PathVariable("id") UUID bookingId,
            @PathVariable("stopId") UUID stopId,
            @RequestBody Map<String, String> body,
            Authentication authentication
    ) {
        String status = body.get("status");
        String driverIdStr = authentication.getName();
        multiStopBookingService.updateStopStatus(bookingId, stopId, status, driverIdStr, "DRIVER");
        return ResponseEntity.ok().build();
    }

    // 4. Admin Override
    @PostMapping("/admin/bookings/{id}/override-stop")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> adminOverrideStop(
            @PathVariable("id") UUID bookingId,
            @RequestParam("stopId") UUID stopId,
            @RequestParam("status") String forceStatus,
            Authentication authentication
    ) {
        String adminIdStr = authentication.getName();
        multiStopBookingService.updateStopStatus(bookingId, stopId, forceStatus, adminIdStr, "ADMIN");
        return ResponseEntity.ok().build();
    }
}
