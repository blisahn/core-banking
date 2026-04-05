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
    @Id
    private UUID id;
    @Column(name = "aggregate_type", nullable = false)
    private String aggregateType;
    @Column(name = "aggregate_id", nullable = false)
    private UUID aggregateId;
    @Column(name = "event_type", nullable = false)
    private String eventType;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String payload;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    @Column(name = "processed", nullable = false)
    private boolean processed;
    @Column(name = "processed_at")
    private Instant processedAt;

    public OutboxEvent() {
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

    public void markProcessed() {
        this.processed = true;
        this.processedAt = Instant.now();
    }
}
