package com.velo.courrier.governance;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "driver_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "driver_id", nullable = false)
    private UUID driverId;

    @Column(name = "document_type", nullable = false)
    private String documentType; // e.g. "DRIVERS_LICENSE", "VEHICLE_INSURANCE"

    @Column(name = "file_url", nullable = false)
    private String fileUrl; // Pointer to AWS S3/MinIO signed object

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "status")
    private String status; // PENDING_REVIEW, APPROVED, REJECTED, EXPIRED

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
