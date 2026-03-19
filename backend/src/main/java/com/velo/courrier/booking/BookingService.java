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

    @Transactional
    public BookingResponse createBooking(UUID customerId, BookingRequest request) {
        AppUser customer = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

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

    private BookingResponse mapToResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId().toString())
                .status(booking.getStatus())
                .fareEstimate(booking.getFareEstimate())
                .serviceType(booking.getServiceType())
                .pickupAddress(booking.getPickupAddress())
                .dropoffAddress(booking.getDropoffAddress())
                .customerId(booking.getCustomer() != null ? booking.getCustomer().getId().toString() : null)
                .driverId(booking.getDriver() != null ? booking.getDriver().getUserId().toString() : null)
                .build();
    }
}
