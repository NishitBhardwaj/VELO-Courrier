package com.velo.courrier.predictive;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "route_deviation_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteDeviationEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "booking_id", nullable = false)
    private UUID bookingId;

    @Column(name = "driver_id", nullable = false)
    private UUID driverId;

    @Column(name = "deviation_distance")
    private Integer deviationDistance;

    @Column(name = "severity", nullable = false)
    private String severity; // WARNING, ALERT, CRITICAL

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lng")
    private Double lng;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
