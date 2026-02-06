package com.devblo.customer;

import com.devblo.common.BaseAggregateRoot;
import com.devblo.customer.event.CustomerAddressUpdatedEvent;
import com.devblo.customer.event.CustomerPersonalInfoUpdatedEvent;
import com.devblo.customer.event.CustomerRegisteredEvent;
import com.devblo.customer.event.CustomerStatusChangedEvent;
import com.devblo.exception.BusinessRuleViolationException;
import com.devblo.exception.CustomerNotActiveException;

import java.time.Instant;
import java.util.Objects;

public class Customer extends BaseAggregateRoot<CustomerId> {

    private PersonalInfo personalInfo;
    private CustomerStatus status;
    private Address address;

    private Customer(CustomerId customerId, PersonalInfo personalInfo, CustomerStatus status, Address address) {
        super(customerId);
        this.personalInfo = personalInfo;
        this.status = status;
        this.address = address;
    }

    private Customer(CustomerId customerId, Instant createdAt,
                     Instant updatedAt, PersonalInfo personalInfo,
                     CustomerStatus status, Address address) {
        super(customerId, createdAt, updatedAt);
        this.personalInfo = personalInfo;
        this.status = status;
        this.address = address;
    }

    public static Customer register(PersonalInfo personalInfo, Address address) {

        Objects.requireNonNull(personalInfo, "PersonalInfo must not be null");
        Objects.requireNonNull(address, "Address must not be null");
        CustomerId id = CustomerId.generate();

        Customer customer = new Customer(
                id,
                personalInfo,
                CustomerStatus.ACTIVE,
                address
        );

        customer.registerEvent(new CustomerRegisteredEvent(id, personalInfo, address));
        return customer;
    }

    public static Customer reconstitute(CustomerId customerId, Instant createdAt,
                                        Instant updatedAt, PersonalInfo personalInfo,
                                        CustomerStatus status, Address address) {
        return new Customer(customerId, createdAt, updatedAt, personalInfo, status, address);
    }

    public void updatePersonalInfo(PersonalInfo personalInfo) {
        validateActive();
        Objects.requireNonNull(personalInfo, "PersonalInfo must not be null");
        this.personalInfo = personalInfo;
        markUpdated();
        registerEvent(new CustomerPersonalInfoUpdatedEvent(this.getId(), personalInfo));
    }

    public void updateAddress(Address address) {
        validateActive();
        Objects.requireNonNull(address, "Address must not be null");
        this.address = address;
        markUpdated();
        registerEvent(new CustomerAddressUpdatedEvent(this.getId(), address));
    }

    public void suspend() {
        if (this.status == CustomerStatus.CLOSED)
            throw new BusinessRuleViolationException("Cannot suspend a closed customer");
        if (this.status == CustomerStatus.SUSPENDED)
            return;

        CustomerStatus oldStatus = this.status;
        this.status = CustomerStatus.SUSPENDED;
        markUpdated();
        registerEvent(new CustomerStatusChangedEvent(oldStatus, this.status, this.getId()));
    }

    public void activate() {
        if (this.status == CustomerStatus.CLOSED)
            throw new BusinessRuleViolationException("Cannot activate a customer that has been closed");
        if (this.status == CustomerStatus.ACTIVE)
            return;
        CustomerStatus oldStatus = this.status;
        this.status = CustomerStatus.ACTIVE;
        markUpdated();
        registerEvent(new CustomerStatusChangedEvent(oldStatus, this.status, this.getId()));
    }

    public void close() {
        if (this.status == CustomerStatus.CLOSED)
            return;
        CustomerStatus oldStatus = this.status;
        this.status = CustomerStatus.CLOSED;
        markUpdated();
        registerEvent(new CustomerStatusChangedEvent(oldStatus, this.status, this.getId()));
    }

    private void validateActive() {
        if (this.status != CustomerStatus.ACTIVE)
            throw new CustomerNotActiveException(this.status);
    }


    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public Address getAddress() {
        return address;
    }
}
