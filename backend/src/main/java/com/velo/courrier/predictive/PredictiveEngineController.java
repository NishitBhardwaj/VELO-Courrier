package com.velo.courrier.predictive;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PredictiveEngineController {

    private final PredictiveEngineService predictiveEngineService;

    @GetMapping("/bookings/{id}/eta")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'DRIVER', 'ADMIN')")
    public ResponseEntity<?> getBookingEtaDiagnostics(@PathVariable("id") UUID bookingId) {
        // Scaffold returning mocked history block
        return ResponseEntity.ok(Map.of(
            "originalEta", 1200,
            "currentEta", 1500,
            "delaySeconds", 300
        ));
    }

    @GetMapping("/admin/bookings/{id}/deviation")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getBookingDeviationHistory(@PathVariable("id") UUID bookingId) {
        // Scaffold returning an empty array of deviations for testing UI bound state
        return ResponseEntity.ok(Map.of("deviations", new Object[0]));
    }
}
