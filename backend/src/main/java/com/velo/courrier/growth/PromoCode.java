package com.velo.courrier.growth;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "promo_codes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromoCode {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "code", nullable = false, unique = true)
    private String code; // e.g. "WELCOME20"

    @Column(name = "discount_amount", precision = 5, scale = 2, nullable = false)
    private BigDecimal discountAmount;

    @Column(name = "type", nullable = false)
    private String type; // PERCENTAGE, FLAT

    @Column(name = "global_limit")
    private Integer globalLimit; // e.g. Max 1000 uses overall

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name = "active")
    private Boolean active;
}
