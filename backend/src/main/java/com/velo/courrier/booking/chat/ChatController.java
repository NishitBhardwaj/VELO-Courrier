package com.velo.courrier.booking.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings/{id}/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'DRIVER', 'ADMIN')")
    public ResponseEntity<?> getChatHistory(
            @PathVariable("id") UUID bookingId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(chatService.getBookingHistory(bookingId, authentication.getName()));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'DRIVER')")
    public ResponseEntity<?> sendMessage(
            @PathVariable("id") UUID bookingId,
            @RequestBody Map<String, String> payload,
            Authentication authentication
    ) {
        String messageBody = payload.get("message");
        if (messageBody == null || messageBody.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Message cannot be empty");
        }

        return ResponseEntity.ok(chatService.sendMessage(bookingId, authentication.getName(), messageBody));
    }
}
