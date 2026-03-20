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
public class DriverIncentive {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "id")
    private UUID id;

    @Column(name = "driverId")
    private UUID driverId;

    @Column(name = "campaignName")
    private String campaignName;

    @Column(name = "bonusAmount")
    private java.math.BigDecimal bonusAmount;

    @Column(name = "status")
    private String status;


}
