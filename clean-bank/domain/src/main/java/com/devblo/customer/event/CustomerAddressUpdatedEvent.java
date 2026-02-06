package com.devblo.customer.event;

import com.devblo.common.event.BaseDomainEvent;
import com.devblo.customer.Address;
import com.devblo.customer.CustomerId;

import java.time.Instant;
import java.util.UUID;

public class CustomerAddressUpdatedEvent extends BaseDomainEvent {
    private final CustomerId customerId;
    private final Address newAddress;

    public CustomerAddressUpdatedEvent(CustomerId customerId, Address newAddress) {
        super(UUID.randomUUID(), Instant.now());
        this.customerId = customerId;
        this.newAddress = newAddress;
    }

    public Address getNewAddress() {
        return newAddress;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

}
