package com.velo.courrier.analytics;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZoneEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "id")
    private UUID id;

    @Column(name = "driverId")
    private UUID driverId;

    @Column(name = "zoneId")
    private UUID zoneId;


}
