package com.velo.courrier.booking.pod;

import com.velo.courrier.booking.Booking;
import com.velo.courrier.booking.BookingRepository;
import com.velo.courrier.common.enums.BookingStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PodService {

    private final ProofOfDeliveryRepository podRepository;
    private final BookingRepository bookingRepository;
    private final StorageService storageService;

    @Transactional
    public void submitProofOfDelivery(
            UUID bookingId, String driverIdStr, MultipartFile photo, 
            MultipartFile signature, String receiverName, String otp) {
        
        UUID driverId = UUID.fromString(driverIdStr);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
        
        // Ensure driver is assigned to this booking
        if (booking.getDriver() == null || !booking.getDriver().getId().equals(driverId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Driver not assigned to this booking");
        }

        // OTP Validation logic
        boolean isOtpValid = validateOtp(booking, otp);
        if (!isOtpValid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid OTP provided");
        }

        String photoUrl = storageService.uploadFileAndGetSignedUrl(photo, "photo");
        String signatureUrl = storageService.uploadFileAndGetSignedUrl(signature, "signature");

        ProofOfDelivery pod = ProofOfDelivery.builder()
                .bookingId(bookingId)
                .driverId(driverId)
                .photoUrl(photoUrl)
                .signatureUrl(signatureUrl)
                .receiverName(receiverName)
                .otpVerified(true)
                .build();
        
        podRepository.save(pod);

        // Mark booking as completed
        booking.setStatus(BookingStatus.COMPLETED);
        bookingRepository.save(booking);

        log.info("Proof of delivery stored and booking {} marked as COMPLETED", bookingId);
    }

    private boolean validateOtp(Booking booking, String otp) {
        // In a real application, compare against generated booking OTP
        // Returning true for this mock phase.
        return true; 
    }
}
