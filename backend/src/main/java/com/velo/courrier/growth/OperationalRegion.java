package com.velo.courrier.growth;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "operational_regions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationalRegion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    private String name; // e.g. "Lyon", "Paris"

    @Column(name = "timezone", nullable = false)
    private String timezone; // e.g. "Europe/Paris"

    @Column(name = "currency_code", length = 10)
    private String currencyCode; // e.g. "EUR", "USD"

    @Column(name = "base_fare_override", precision = 5, scale = 2)
    private BigDecimal baseFareOverride;

    @Column(name = "active")
    private Boolean active;
}
