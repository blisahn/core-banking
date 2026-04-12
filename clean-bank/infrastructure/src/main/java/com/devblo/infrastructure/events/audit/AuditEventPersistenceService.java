package com.devblo.infrastructure.events.audit;

import com.devblo.common.event.BaseDomainEvent;
import com.devblo.infrastructure.persistence.audit.AuditEventEntity;
import com.devblo.infrastructure.persistence.audit.IAuditEventJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
public class AuditEventPersistenceService {

    private static final Logger log = LoggerFactory.getLogger(AuditEventPersistenceService.class);
    private final IAuditEventJpaRepository repository;
    private final ObjectMapper objectMapper;

    public AuditEventPersistenceService(IAuditEventJpaRepository repository) {
        this.repository = repository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @SneakyThrows
    @Transactional
    public void saveAuditEvent(BaseDomainEvent domainEvent, String aggregateType, UUID aggregateId) {
        UUID actorId = null;
        String actorRole = null;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Map<?, ?> principal) {
            Object userId = principal.get("userId");
            if (userId != null) {
                actorId = UUID.fromString(userId.toString());
            }
            Object role = principal.get("role");
            if (role != null) {
                actorRole = role.toString();
            }
        }

        String eventType = domainEvent.getClass().getSimpleName();

        AuditEventEntity auditEvent = new AuditEventEntity(
                UUID.randomUUID(),
                eventType,
                aggregateType,
                aggregateId,
                actorId,
                actorRole,
                AuditEventDescriptor.buildSummary(domainEvent),
                AuditEventDescriptor.resolveSeverity(domainEvent),
                objectMapper.writeValueAsString(domainEvent),
                Instant.now()
        );
        repository.save(auditEvent);
        log.info("Actor: [{}], with role: [{}] performed {}, EventId: [{}]", actorId, actorRole, eventType, auditEvent.getId());
    }

}
