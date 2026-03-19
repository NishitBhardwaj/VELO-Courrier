package com.velo.courrier.booking;

import com.velo.courrier.booking.dto.BookingRequest;
import com.velo.courrier.booking.dto.BookingResponse;
import com.velo.courrier.common.enums.BookingStatus;
import com.velo.courrier.common.event.BookingConfirmedEvent;
import com.velo.courrier.pricing.PricingService;
import com.velo.courrier.user.AppUser;
import com.velo.courrier.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final PricingService pricingService;
    private final ApplicationEventPublisher eventPublisher;
    private final com.velo.courrier.ops.ZoneValidationService zoneValidationService;

    @Transactional
    public BookingResponse createBooking(UUID customerId, BookingRequest request) {
        AppUser customer = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Sprint 4: Geofence Validation
        if (!zoneValidationService.validateSingleStopRequest(request)) {
             throw new IllegalStateException("Requested route falls outside active delivery service zones.");
        }

        BigDecimal fare = pricingService.calculateEstimate(
                request.getVehicleCategoryId(), 
                request.getPickupAddress(), 
                request.getDropoffAddress()
        );

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setServiceType(request.getServiceType());
        booking.setPickupAddress(request.getPickupAddress());
        booking.setDropoffAddress(request.getDropoffAddress());
        booking.setFareEstimate(fare);
        booking.setStatus(BookingStatus.DRAFT);
        booking.setScheduledTime(request.getScheduledTime());

        booking = bookingRepository.save(booking);

        return mapToResponse(booking);
    }
    
    @Transactional
    public BookingResponse confirmBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
                
        if (booking.getStatus() != BookingStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT bookings can be confirmed");
        }
        
        booking.setStatus(BookingStatus.SEARCHING);
        booking = bookingRepository.save(booking);
        
        eventPublisher.publishEvent(new BookingConfirmedEvent(booking.getId()));
        
        return mapToResponse(booking);
    }

    @Transactional(readOnly = true)
    public BookingResponse getBookingDetails(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        return mapToResponse(booking);
    }

    private BookingResponse mapToResponse(Booking booking) {
        BookingResponse response = BookingResponse.builder()
                .id(booking.getId().toString())
                .status(booking.getStatus())
                .fareEstimate(booking.getFareEstimate())
                .serviceType(booking.getServiceType())
                .pickupAddress(booking.getPickupAddress())
                .dropoffAddress(booking.getDropoffAddress())
                .customerId(booking.getCustomer() != null ? booking.getCustomer().getId().toString() : null)
                .driverId(booking.getDriver() != null ? booking.getDriver().getId().toString() : null)
                .multiStopEnabled(booking.isMultiStopEnabled())
                .totalStops(booking.getTotalStops())
                .currentStopOrder(booking.getCurrentStopOrder())
                .build();
                
        // In a real expanded app, Stop Repository would be injected here or this logic lives in a Facade.
        // For the scaffolding, stops are dynamically hydrated at the facade layer when multiStopEnabled = true.
        return response;
    }
}
