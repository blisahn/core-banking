package com.devblo.customer.event;

import com.devblo.common.event.BaseDomainEvent;
import com.devblo.customer.CustomerId;
import com.devblo.customer.CustomerStatus;

import java.time.Instant;
import java.util.UUID;

public class CustomerStatusChangedEvent extends BaseDomainEvent {
    private final CustomerStatus oldStatus;
    private final CustomerStatus newStatus;
    private final CustomerId customerId;

    public CustomerStatusChangedEvent(CustomerStatus oldStatus, CustomerStatus newStatus, CustomerId customerId) {
        super(UUID.randomUUID(), Instant.now());
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.customerId = customerId;
    }

    public CustomerStatus getOldStatus() {
        return oldStatus;
    }

    public CustomerStatus getNewStatus() {
        return newStatus;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

}