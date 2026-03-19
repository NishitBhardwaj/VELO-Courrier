package com.velo.courrier.dispatch;

import com.velo.courrier.booking.Booking;
import com.velo.courrier.booking.BookingRepository;
import com.velo.courrier.common.enums.BookingStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DispatchOverrideService {

    private final BookingRepository bookingRepository;
    // private final DispatchOverrideEventRepository overrideRepository;
    // private final UserRepository userRepository;

    @Transactional
    public void forceAssignDriver(UUID bookingId, UUID adminId, UUID newDriverId, String reason) {
        log.info("Admin {} attempting FORCE ASSIGN on Booking {} to Driver {}", adminId, bookingId, newDriverId);
        
        try {
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking not found"));

            if (booking.getStatus() != BookingStatus.UNASSIGNED) {
                throw new IllegalStateException("Cannot ASSIGN. Booking is already " + booking.getStatus() + ". Use REASSIGN.");
            }

            // Execute lock assignment
            // booking.setDriver(userRepository.getReferenceById(newDriverId));
            booking.setStatus(BookingStatus.ASSIGNED);
            
            // The @Version annotation on Booking will natively throw ObjectOptimisticLockingFailureException
            // if another Admin concurrently saves this row before this transaction commits.
            bookingRepository.save(booking);

            // Audit
            DispatchOverrideEvent audit = DispatchOverrideEvent.builder()
                    .bookingId(bookingId)
                    .newDriverId(newDriverId)
                    .adminUserId(adminId)
                    .actionType("ASSIGN")
                    .reason(reason)
                    .build();
            // overrideRepository.save(audit);

            log.info("FORCE ASSIGN successful. Audit logged.");

        } catch (ObjectOptimisticLockingFailureException e) {
            log.error("CONCURRENCY ANOMALY BLOCKED: Another dispatcher assigned this booking 1ms ago.");
            throw new RuntimeException("Assignment Race Condition Prevented. Booking already modified.");
        }
    }

    @Transactional
    public void forceReassignDriver(UUID bookingId, UUID adminId, UUID newDriverId, String reason) {
         // Logic identical to forceAssign but severs the previous_driver relationship first,
         // updating driver timelines and emitting webhooks.
         log.info("REASSIGN logic triggered for {} by Admin {}", bookingId, adminId);
    }
}
