package com.devblo.customer.event;

import com.devblo.common.event.BaseDomainEvent;
import com.devblo.customer.Address;
import com.devblo.customer.CustomerId;
import com.devblo.customer.PersonalInfo;

import java.time.Instant;
import java.util.UUID;

public class CustomerRegisteredEvent extends BaseDomainEvent {

    private final CustomerId customerId;
    private final PersonalInfo personalInfo;
    private final Address address;

    public CustomerRegisteredEvent(CustomerId customerId, PersonalInfo personalInfo, Address address) {
        super(UUID.randomUUID(), Instant.now());
        this.customerId = customerId;
        this.personalInfo = personalInfo;
        this.address = address;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public Address getAddress() {
        return address;
    }
}

