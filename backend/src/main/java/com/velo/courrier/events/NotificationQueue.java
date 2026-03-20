package com.velo.courrier.events;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "id")
    private UUID id;

    @Column(name = "userId")
    private UUID userId;

    @Column(name = "type")
    private String type;

    @Column(name = "payload")
    private String payload;

    @Column(name = "status")
    private String status;


}
