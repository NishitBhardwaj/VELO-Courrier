package com.velo.courrier.ops;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "zone_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZoneEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "booking_id")
    private UUID bookingId;

    @Column(name = "driver_id")
    private UUID driverId;

    @Column(name = "zone_id")
    private UUID zoneId;

    @Column(name = "event_type", nullable = false)
    private String eventType; // e.g. BREACH, ENTRY, OVERRIDE

    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
