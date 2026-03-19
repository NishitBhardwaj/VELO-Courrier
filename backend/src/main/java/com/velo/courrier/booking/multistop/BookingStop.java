package com.velo.courrier.booking.multistop;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "booking_stops")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingStop {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "booking_id", nullable = false)
    private UUID bookingId;

    @Column(name = "stop_order", nullable = false)
    private Integer stopOrder;

    @Column(name = "stop_type", nullable = false)
    private String stopType; // PICKUP or DROP_OFF

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "address_json", columnDefinition = "jsonb")
    private String addressJson;

    @Column(name = "status")
    private String status = "PENDING"; // PENDING, ARRIVED, COMPLETED, FAILED, SKIPPED

    @Column(name = "eta_at_creation")
    private Integer etaAtCreation;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "notes")
    private String notes;
}
