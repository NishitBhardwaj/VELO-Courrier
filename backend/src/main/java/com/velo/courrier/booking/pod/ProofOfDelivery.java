package com.velo.courrier.booking.pod;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "proof_of_delivery")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProofOfDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "booking_id", nullable = false)
    private UUID bookingId;

    @Column(name = "driver_id", nullable = false)
    private UUID driverId;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "signature_url")
    private String signatureUrl;

    @Column(name = "otp_verified")
    private boolean otpVerified = false;

    @Column(name = "receiver_name")
    private String receiverName;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
