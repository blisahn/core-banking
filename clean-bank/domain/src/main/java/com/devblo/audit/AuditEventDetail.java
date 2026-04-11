package com.devblo.audit;

import java.time.Instant;
import java.util.UUID;

public record AuditEventDetail(UUID id,
                               String eventType,
                               String aggregateType,
                               UUID aggregateId,
                               UUID actorId,
                               String actorRole,
                               String summary,
                               String severity,
                               String payload,
                               Instant occurredOn) {
}
