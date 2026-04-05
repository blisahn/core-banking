package com.devblo.infrastructure.events.outbox;

import com.devblo.common.event.BaseDomainEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class OutboxEventService {

    private final OutboxJpaRepository outboxJpaRepository;

    public OutboxEventService(OutboxJpaRepository outboxJpaRepository) {
        this.outboxJpaRepository = outboxJpaRepository;
    }

    @SneakyThrows
    @Transactional
    public void saveEvent(BaseDomainEvent baseDomainEvent, String aggregateType, UUID aggregateId) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        OutboxEvent outboxEvent = new OutboxEvent(
                UUID.randomUUID(),
                aggregateType,
                aggregateId,
                baseDomainEvent.getClass().getSimpleName(),
                mapper.writeValueAsString(baseDomainEvent),
                Instant.now(),
                false,
                null
        );
        outboxJpaRepository.save(outboxEvent);
    }
}
