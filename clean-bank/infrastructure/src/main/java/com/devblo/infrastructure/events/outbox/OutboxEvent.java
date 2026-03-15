package com.devblo.infrastructure.events.outbox;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
@Data
public class OutboxEvent {
    @Id private UUID id;
    private String aggregateType;
    private UUID aggregateId;
    private String eventType;
    @Column(columnDefinition = "TEXT") private String payload;
    private Instant createdAt;
    private boolean processed;
    private Instant processedAt; // ← bu da eklenmeli

    public OutboxEvent( ) {
    }

    public OutboxEvent(UUID id, String aggregateType, UUID aggregateId, String eventType, String payload, Instant createdAt, boolean processed, Instant processedAt) {
        this.id = id;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.createdAt = createdAt;
        this.processed = processed;
        this.processedAt = processedAt;
    }

    public void markProcessed(){
        this.processed = true;
        this.processedAt = Instant.now();
    }
}
