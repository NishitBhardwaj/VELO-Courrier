package com.velo.courrier.vehicle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vehicle_categories")
@Getter
@Setter
public class VehicleCategory {

    @Id
    private UUID id;

    @Column(unique = true, nullable = false, length = 50)
    private String name;

    @Column(name = "base_fare", nullable = false)
    private BigDecimal baseFare;

    @Column(name = "per_km_rate", nullable = false)
    private BigDecimal perKmRate;

    @Column(name = "per_min_rate", nullable = false)
    private BigDecimal perMinRate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
