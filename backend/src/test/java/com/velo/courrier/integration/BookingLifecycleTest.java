package com.velo.courrier.integration;

import com.velo.courrier.booking.Booking;
import com.velo.courrier.booking.BookingRepository;
import com.velo.courrier.booking.BookingStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class BookingLifecycleTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.data.redis.repositories.enabled", () -> false);
    }

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void testBookingStateTransitions() {
        // 1. Create Draft
        Booking booking = new Booking();
        booking.setId(UUID.randomUUID());
        booking.setStatus(BookingStatus.DRAFT);
        bookingRepository.save(booking);

        Booking savedDraft = bookingRepository.findById(booking.getId()).orElseThrow();
        assertThat(savedDraft.getStatus()).isEqualTo(BookingStatus.DRAFT);

        // 2. Customer confirms, trigger Payment, enter SEARCHING
        savedDraft.setStatus(BookingStatus.SEARCHING);
        bookingRepository.save(savedDraft);
        
        Booking searchingBooking = bookingRepository.findById(booking.getId()).orElseThrow();
        assertThat(searchingBooking.getStatus()).isEqualTo(BookingStatus.SEARCHING);
        
        // 3. Driver accepts (Audit trail should ideally capture this Driver ID separately)
        searchingBooking.setStatus(BookingStatus.ACCEPTED);
        bookingRepository.save(searchingBooking);

        assertThat(bookingRepository.findById(booking.getId()).orElseThrow().getStatus())
            .isEqualTo(BookingStatus.ACCEPTED);
    }
}
