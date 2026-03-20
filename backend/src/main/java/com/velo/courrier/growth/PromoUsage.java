package com.velo.courrier.growth;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromoUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "id")
    private UUID id;

    @Column(name = "promoCodeId")
    private UUID promoCodeId;

    @Column(name = "customerId")
    private UUID customerId;


}
