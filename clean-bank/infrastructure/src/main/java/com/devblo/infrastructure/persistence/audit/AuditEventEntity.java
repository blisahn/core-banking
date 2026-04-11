package com.devblo.infrastructure.persistence.audit;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditEventEntity {
    @Id
    private UUID id;
    private String eventType;
    private String aggregateType;
    private UUID aggregateId;
    private UUID actorId;
    private String actorRole;
    private String summary;
    private String severity;
    private String payload;
    private Instant occurredOn;

}
