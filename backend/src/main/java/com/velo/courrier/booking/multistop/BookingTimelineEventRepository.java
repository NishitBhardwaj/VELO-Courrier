package com.velo.courrier.booking.multistop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookingTimelineEventRepository extends JpaRepository<BookingTimelineEvent, UUID> {
    List<BookingTimelineEvent> findByBookingIdOrderByCreatedAtAsc(UUID bookingId);
}
