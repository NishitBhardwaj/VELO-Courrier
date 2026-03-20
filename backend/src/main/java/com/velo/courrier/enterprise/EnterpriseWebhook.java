package com.velo.courrier.enterprise;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnterpriseWebhook {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "id")
    private UUID id;

    @Column(name = "customerId")
    private UUID customerId;

    @Column(name = "eventType")
    private String eventType;

    @Column(name = "callbackUrl")
    private String callbackUrl;

    @Column(name = "active")
    private Boolean active;


}
