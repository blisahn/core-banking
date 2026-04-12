package com.devblo.infrastructure.events.sse;

import com.devblo.audit.AuditEventSummary;
import com.devblo.common.event.BaseDomainEvent;
import com.devblo.infrastructure.events.audit.AuditEventDescriptor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SseEventNotifier {

    private static final Logger log = LoggerFactory.getLogger(SseEventNotifier.class);
    private final SseEmitterManager emitterManager;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onDomainEvent(BaseDomainEvent event) {
        AuditEventSummary summary = new AuditEventSummary(
                UUID.randomUUID(),
                event.getClass().getSimpleName(),
                AuditEventDescriptor.resolveAggregateType(event),
                AuditEventDescriptor.resolveAggregateId(event),
                null,
                null,
                AuditEventDescriptor.buildSummary(event),
                AuditEventDescriptor.resolveSeverity(event),
                Instant.now()
        );

        log.debug("Broadcasting audit event via SSE: {}", summary.eventType());
        emitterManager.broadcast(summary);
    }
}
