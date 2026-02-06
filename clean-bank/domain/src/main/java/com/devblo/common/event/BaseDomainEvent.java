package com.devblo.common.event;

import java.time.Instant;
import java.util.UUID;

public abstract class BaseDomainEvent {

    private final UUID eventId;
    private final Instant occurredOn;

    protected BaseDomainEvent(UUID eventId, Instant occurredOn) {
        this.eventId = eventId;
        this.occurredOn = occurredOn;
    }

    public UUID getEventId() {
        return eventId;
    }

    public Instant getOccurredOn() {
        return occurredOn;
    }
}
