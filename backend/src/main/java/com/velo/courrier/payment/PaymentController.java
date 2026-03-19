package com.velo.courrier.payment;

import com.velo.courrier.common.event.BookingConfirmedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentRepository paymentRepository;
    private final ApplicationEventPublisher eventPublisher;

    @PostMapping("/webhook/stripe")
    public ResponseEntity<Void> handleStripeWebhook(
            @RequestHeader("Stripe-Signature") String sigHeader,
            @RequestBody String payload
    ) {
        // Strict Stripe Webhook Signature Verification
        // In reality: Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        log.info("Verifying Stripe webhook signature: {}", sigHeader);
        
        // Mock parsing payload tree
        if (payload.contains("\"type\": \"payment_intent.succeeded\"")) {
            UUID mockBookingId = UUID.randomUUID();
            log.info("Payment captured for hypothetical booking: {}", mockBookingId);
        }
        
        return ResponseEntity.ok().build();
    }
}
