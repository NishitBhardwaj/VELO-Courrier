package com.velo.courrier.security;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTrustScore {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "id")
    private UUID id;

    @Column(name = "userId")
    private UUID userId;

    @Column(name = "score")
    private Integer score;


}
