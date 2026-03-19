package com.velo.courrier.governance;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "security_audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "actor_id", nullable = false)
    private UUID actorId;

    @Column(name = "target_entity")
    private String targetEntity; // e.g. "DRIVER", "USER", "CONTRACT"

    @Column(name = "target_id")
    private UUID targetId;

    @Column(name = "action", nullable = false)
    private String action; // e.g. "SUSPEND_DRIVER", "UPDATE_PRICING"

    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata; // Stores JSON state Before/After mutations

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
