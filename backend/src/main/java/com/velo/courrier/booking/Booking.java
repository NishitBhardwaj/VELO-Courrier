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

    @Column(name = "scheduled_time")
    private LocalDateTime scheduledTime;

    @Column(name = "fare_estimate")
    private BigDecimal fareEstimate;
}
