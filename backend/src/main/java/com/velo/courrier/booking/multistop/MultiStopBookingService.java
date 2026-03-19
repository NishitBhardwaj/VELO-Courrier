package com.velo.courrier.booking.multistop;

import com.velo.courrier.booking.Booking;
import com.velo.courrier.booking.BookingRepository;
import com.velo.courrier.booking.dto.MultiStopBookingRequest;
import com.velo.courrier.booking.dto.StopDto;
import com.velo.courrier.common.config.FeatureFlagService;
import com.velo.courrier.common.enums.BookingStatus;
import com.velo.courrier.customer.Customer;
import com.velo.courrier.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MultiStopBookingService {

    private final BookingRepository bookingRepository;
    private final BookingStopRepository stopRepository;
    private final BookingTimelineEventRepository timelineRepository;
    private final CustomerRepository customerRepository;
    private final FeatureFlagService featureFlagService;
    private final com.velo.courrier.booking.BookingEventPublisher eventPublisher;

    @Transactional
    public Booking createBooking(MultiStopBookingRequest request, String customerIdStr) {
        if (!featureFlagService.isMultiStopBackendEnabled()) {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Multi-stop creation is currently disabled.");
        }
        
        if (request.getStops() == null || request.getStops().size() < 1 || request.getStops().size() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A multi-stop booking must have between 1 and 5 stops.");
        }

        UUID customerId = UUID.fromString(customerIdStr);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        // Validate first stop is PICKUP
        if (!"PICKUP".equalsIgnoreCase(request.getStops().get(0).getType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The first stop must be a PICKUP.");
        }

        // Parent Booking Setup
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setStatus(BookingStatus.PENDING);
        booking.setMultiStopEnabled(true);
        booking.setTotalStops(request.getStops().size());
        booking.setCurrentStopOrder(1);
        booking = bookingRepository.save(booking);

        log.info("Created Multi-Stop Booking parent {}", booking.getId());

        // Save Stops
        UUID bookingId = booking.getId();
        for (StopDto sd : request.getStops()) {
            BookingStop stop = BookingStop.builder()
                    .bookingId(bookingId)
                    .stopOrder(sd.getOrder())
                    .stopType(sd.getType())
                    .contactName(sd.getContactName())
                    .contactPhone(sd.getContactPhone())
                    .addressJson(sd.getAddressJson())
                    .status("PENDING")
                    .build();
            stopRepository.save(stop);
        }

        // Timeline Log
        logTimelineEvent(bookingId, null, "CREATED", "CUSTOMER", customerIdStr, true, "Booking created with " + request.getStops().size() + " stops");
        
        return booking;
    }

    @Transactional
    public void updateStopStatus(UUID bookingId, UUID stopId, String newStatus, String actorIdStr, String actorType) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
        
        BookingStop stop = stopRepository.findById(stopId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stop not found"));

        if (!stop.getBookingId().equals(bookingId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stop does not belong to booking");
        }

        // Admin override check
        if ("ADMIN".equals(actorType) && !featureFlagService.isMultiStopAdminOverrideEnabled()) {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Admin multi-stop overrides are disabled.");
        }

        // 1. Sequence validation (only for drivers, admins can bypass)
        if ("DRIVER".equals(actorType)) {
            if (!stop.getStopOrder().equals(booking.getCurrentStopOrder())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must complete stops in exact sequence. Current active stop order: " + booking.getCurrentStopOrder());
            }
        }

        // 2. Perform Update
        stop.setStatus(newStatus.toUpperCase());
        stopRepository.save(stop);

        // 3. Orchestrate next steps
        if ("COMPLETED".equalsIgnoreCase(newStatus) || "SKIPPED".equalsIgnoreCase(newStatus) || "FAILED".equalsIgnoreCase(newStatus)) {
            booking.setCurrentStopOrder(booking.getCurrentStopOrder() + 1);
            if (booking.getCurrentStopOrder() > booking.getTotalStops()) {
                // Last stop fulfilled
                booking.setStatus(BookingStatus.COMPLETED);
                logTimelineEvent(bookingId, null, "BOOKING_COMPLETED", "SYSTEM", actorIdStr, true, "All stops fulfilled.");
            }
            bookingRepository.save(booking);
        }

        logTimelineEvent(bookingId, stopId, "STOP_" + newStatus.toUpperCase(), actorType, actorIdStr, true, "Stop " + stop.getStopOrder() + " marked as " + newStatus);

        // Event Versioning Broadcasts
        eventPublisher.publishV1StatusUpdate(bookingId, booking.getStatus().toString());
        eventPublisher.publishV2StopUpdate(
                bookingId, 
                stopId, 
                stop.getStopOrder(), 
                newStatus.toUpperCase(), 
                booking.getCurrentStopOrder()
        );
    }

    public List<BookingTimelineEvent> getTimelineForBooking(UUID bookingId) {
        return timelineRepository.findByBookingIdOrderByCreatedAtAsc(bookingId);
    }

    private void logTimelineEvent(UUID bookingId, UUID stopId, String eventType, String actorType, String actorId, boolean isVisible, String metadataRaw) {
        BookingTimelineEvent event = BookingTimelineEvent.builder()
                .bookingId(bookingId)
                .stopId(stopId)
                .eventType(eventType)
                .actorType(actorType)
                .actorId(UUID.fromString(actorId))
                .customerVisible(isVisible)
                .metadata("{\"note\":\"" + metadataRaw + "\"}") // Simple JSON string wrapper
                .build();
        timelineRepository.save(event);
    }
}
