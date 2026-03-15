package com.devblo.infrastructure.events.outbox;

import com.devblo.common.event.BaseDomainEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OutboxEventService {

    private final OutboxJpaRepository outboxJpaRepository;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Transactional
    public void saveEvent(BaseDomainEvent baseDomainEvent, String aggregateType, UUID aggregateId) {
        OutboxEvent outboxEvent = new OutboxEvent(
                UUID.randomUUID(),
                aggregateType,
                aggregateId,
                baseDomainEvent.getClass().getSimpleName(),
                objectMapper.writeValueAsString(baseDomainEvent),
                Instant.now(),
                false,
                Instant.now()
        );
        outboxJpaRepository.save(outboxEvent);
    }
}
