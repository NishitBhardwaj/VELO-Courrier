package com.velo.courrier.booking.multistop;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "booking_timeline_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingTimelineEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "booking_id", nullable = false)
    private UUID bookingId;

    @Column(name = "stop_id")
    private UUID stopId;

    @Column(name = "event_type", nullable = false)
    private String eventType; // e.g. CREATED, STOP_COMPLETED, FAILED, ADMIN_OVERRIDE

    @Column(name = "actor_type", nullable = false)
    private String actorType; // DRIVER, CUSTOMER, ADMIN, SYSTEM

    @Column(name = "actor_id", nullable = false)
    private UUID actorId;

    @Column(name = "customer_visible")
    private boolean customerVisible = false;

    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
