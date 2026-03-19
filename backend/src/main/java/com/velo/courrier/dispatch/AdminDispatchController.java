package com.velo.courrier.dispatch;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/dispatch")
@RequiredArgsConstructor
public class AdminDispatchController {

    private final DispatchOverrideService dispatchOverrideService;
    private final CandidateSearchService candidateSearchService;

    @GetMapping("/bookings/{id}/candidates")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPS')")
    public ResponseEntity<?> getCandidates(
            @PathVariable("id") UUID bookingId,
            @RequestParam Double pickupLat,
            @RequestParam Double pickupLng) {
        
        return ResponseEntity.ok(candidateSearchService.findCandidatesForBooking(bookingId, pickupLat, pickupLng));
    }

    @PostMapping("/bookings/{id}/assign")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPS')")
    public ResponseEntity<?> forceAssign(
            @PathVariable("id") UUID bookingId,
            @RequestBody Map<String, String> payload) {
        
        UUID adminId = UUID.fromString(payload.get("adminId")); // In reality, extract from SecurityContext Auth
        UUID driverId = UUID.fromString(payload.get("driverId"));
        String reason = payload.get("reason");

        dispatchOverrideService.forceAssignDriver(bookingId, adminId, driverId, reason);
        return ResponseEntity.ok().build();
    }
}
