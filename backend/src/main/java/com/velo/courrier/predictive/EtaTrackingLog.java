package com.velo.courrier.predictive;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "eta_tracking_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EtaTrackingLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "booking_id", nullable = false)
    private UUID bookingId;

    @Column(name = "stop_order", nullable = false)
    private Integer stopOrder;

    @Column(name = "original_eta")
    private Integer originalEta;

    @Column(name = "current_eta")
    private Integer currentEta;

    @Column(name = "delay_seconds")
    private Integer delaySeconds;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
