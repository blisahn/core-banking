package com.devblo.common;

import com.devblo.common.event.BaseDomainEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public abstract class BaseAggregateRoot<Id> extends BaseEntity<Id> {
    public final List<BaseDomainEvent> domainEvents = new ArrayList<>();

    protected BaseAggregateRoot(Id id) {
        super(id);
    }
    protected BaseAggregateRoot(Id id, Instant createAt, Instant updatedAt) {
        super(id,createAt,updatedAt);
    }
    protected void registerEvent(BaseDomainEvent event) {
        domainEvents.add(event);
    }

    public List<BaseDomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }
}
