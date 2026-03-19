package com.velo.courrier.booking;

import com.velo.courrier.booking.dto.BookingRequest;
import com.velo.courrier.booking.dto.BookingResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'BUSINESS')")
    public ResponseEntity<BookingResponse> createBooking(
            Authentication authentication,
            @Valid @RequestBody BookingRequest request
    ) {
        UUID customerId = UUID.fromString(authentication.getName()); // Mocked extraction
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingService.createBooking(customerId, request));
    }

    @PostMapping("/{id}/confirm")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'BUSINESS')")
    public ResponseEntity<BookingResponse> confirmBooking(
            @PathVariable UUID id
    ) {
        // Validation that the booking belongs to the caller should exist here
        return ResponseEntity.ok(bookingService.confirmBooking(id));
    }
}
