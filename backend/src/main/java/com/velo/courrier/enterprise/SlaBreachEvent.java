package com.velo.courrier.enterprise;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sla_breach_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlaBreachEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "booking_id", nullable = false)
    private UUID bookingId;

    @Column(name = "contract_id")
    private UUID contractId;

    @Column(name = "breach_type", nullable = false)
    private String breachType; // ETA_VIOLATION, OFFLINE_ABANDONMENT

    @Column(name = "severity_level", nullable = false)
    private String severityLevel; // MEDIUM, HIGH, CRITICAL

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
