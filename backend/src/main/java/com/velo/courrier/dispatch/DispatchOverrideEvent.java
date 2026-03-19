package com.velo.courrier.dispatch;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "dispatch_override_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DispatchOverrideEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "booking_id", nullable = false)
    private UUID bookingId;

    @Column(name = "previous_driver_id")
    private UUID previousDriverId;

    @Column(name = "new_driver_id")
    private UUID newDriverId;

    @Column(name = "admin_user_id", nullable = false)
    private UUID adminUserId;

    @Column(name = "action_type", nullable = false)
    private String actionType; // ASSIGN, REASSIGN, RELEASE

    @Column(name = "reason", nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
