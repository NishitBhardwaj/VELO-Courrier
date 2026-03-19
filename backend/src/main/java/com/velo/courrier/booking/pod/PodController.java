package com.velo.courrier.booking.pod;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class PodController {

    private final PodService podService;

    // Step 6: POD Upload API
    @PostMapping(value = "/{id}/pod", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<Void> uploadProofOfDelivery(
            @PathVariable("id") UUID bookingId,
            @RequestParam("photo") MultipartFile photo,
            @RequestParam(value = "signature", required = false) MultipartFile signature,
            @RequestParam("receiverName") String receiverName,
            @RequestParam("otp") String otp,
            Authentication authentication
    ) {
        String driverId = authentication.getName();
        podService.submitProofOfDelivery(bookingId, driverId, photo, signature, receiverName, otp);
        return ResponseEntity.ok().build();
    }
}
