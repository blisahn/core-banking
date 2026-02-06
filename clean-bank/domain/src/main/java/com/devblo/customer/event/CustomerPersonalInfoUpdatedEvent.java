package com.devblo.customer.event;

import com.devblo.common.event.BaseDomainEvent;
import com.devblo.customer.CustomerId;
import com.devblo.customer.PersonalInfo;

import java.time.Instant;
import java.util.UUID;

public class CustomerPersonalInfoUpdatedEvent extends BaseDomainEvent {
    private final CustomerId customerId;
    private final PersonalInfo newInfo;
    public CustomerPersonalInfoUpdatedEvent(CustomerId customerId, PersonalInfo newInfo) {
        super(UUID.randomUUID(), Instant.now());
        this.customerId = customerId;
        this.newInfo = newInfo;
    }

    public PersonalInfo getNewInfo() {
        return newInfo;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

}
