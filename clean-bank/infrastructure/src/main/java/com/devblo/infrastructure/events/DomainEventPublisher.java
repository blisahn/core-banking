package com.devblo.infrastructure.events;

import com.devblo.common.BaseAggregateRoot;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DomainEventPublisher {
    private final ApplicationEventPublisher springPublisher;

    public void publishAll(BaseAggregateRoot<?> aggregate) {
        aggregate.getDomainEvents().forEach(springPublisher::publishEvent);
        aggregate.clearDomainEvents();
    }
}
