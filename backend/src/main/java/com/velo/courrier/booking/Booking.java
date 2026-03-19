package com.velo.courrier.booking;

import com.velo.courrier.common.dto.Address;
import com.velo.courrier.common.entity.BaseEntity;
import com.velo.courrier.common.enums.BookingStatus;
import com.velo.courrier.driver.DriverProfile;
import com.velo.courrier.user.AppUser;
import com.velo.courrier.vehicle.Vehicle;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
public class Booking extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private AppUser customer;

    @Column(name = "service_type", length = 50)
    private String serviceType; // e.g., INSTANT, COURIER, MOVERS

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "pickup_address", columnDefinition = "jsonb")
    private Address pickupAddress;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "dropoff_address", columnDefinition = "jsonb")
    private Address dropoffAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status = BookingStatus.DRAFT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private DriverProfile driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Column(name = "estimated_fare", precision = 10, scale = 2)
    private BigDecimal estimatedFare;

    @Column(name = "final_fare", precision = 10, scale = 2)
    private BigDecimal finalFare;

    @Version
    @Column(name = "version")
    private Integer version;

    @Column(name = "scheduled_time")
    private LocalDateTime scheduledTime;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Sprint 2: Multi-Stop Additions
    @Column(name = "total_stops")
    private Integer totalStops = 1;

    @Column(name = "current_stop_order")
    private Integer currentStopOrder = 1;

    @Column(name = "multi_stop_enabled")
    private boolean multiStopEnabled = false;

    @Column(name = "route_distance_meters")
    private Integer routeDistanceMeters;

    @Column(name = "route_duration_seconds")
    private Integer routeDurationSeconds;
}
