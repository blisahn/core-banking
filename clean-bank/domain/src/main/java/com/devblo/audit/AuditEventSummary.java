package com.devblo.audit;

import java.time.Instant;
import java.util.UUID;

public record AuditEventSummary(UUID id,
                                String eventType,
                                String aggregateType,
                                UUID aggregateId,
                                UUID actorId,
                                String actorRole,
                                String summary,
                                String severity,
                                Instant occurredOn) {
}
