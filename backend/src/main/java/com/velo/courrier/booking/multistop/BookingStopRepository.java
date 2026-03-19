package com.velo.courrier.booking.multistop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingStopRepository extends JpaRepository<BookingStop, UUID> {
    List<BookingStop> findByBookingIdOrderByStopOrderAsc(UUID bookingId);
    Optional<BookingStop> findByBookingIdAndStopOrder(UUID bookingId, Integer stopOrder);
}
