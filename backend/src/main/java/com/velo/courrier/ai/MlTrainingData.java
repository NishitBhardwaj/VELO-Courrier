package com.velo.courrier.ai;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MlTrainingData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "bookingId")
    private UUID bookingId;

    @Column(name = "pickupZone")
    private String pickupZone;

    @Column(name = "dropZone")
    private String dropZone;

    @Column(name = "delaySeconds")
    private Integer delaySeconds;


}
