package com.devblo.customer;

import com.devblo.common.BaseAggregateRoot;
import com.devblo.common.result.Result;
import com.devblo.customer.event.CustomerAddressUpdatedEvent;
import com.devblo.customer.event.CustomerPersonalInfoUpdatedEvent;
import com.devblo.customer.event.CustomerRegisteredEvent;
import com.devblo.customer.event.CustomerStatusChangedEvent;

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

    public Result<Void> updatePersonalInfo(PersonalInfo personalInfo) {
        Objects.requireNonNull(personalInfo, "PersonalInfo must not be null");
        var isActiveResult = validateActive();
        if (isActiveResult.isFailure()) {
            return Result.failure(isActiveResult.getError());
        }
        this.personalInfo = personalInfo;
        markUpdated();
        registerEvent(new CustomerPersonalInfoUpdatedEvent(this.getId(), personalInfo));
        return Result.success();
    }

    public Result<Void> updateAddress(Address address) {
        Objects.requireNonNull(address, "Address must not be null");
        var isActiveResult = validateActive();
        if (!isActiveResult.isSuccess()) {
            return Result.failure(isActiveResult.getError());
        }
        this.address = address;
        markUpdated();
        registerEvent(new CustomerAddressUpdatedEvent(this.getId(), address));
        return Result.success();
    }

    public Result<Void> suspend() {
        if (this.status == CustomerStatus.CLOSED)
            return Result.failure("Cannot close a already closed customer");
        if (this.status == CustomerStatus.SUSPENDED)
            return Result.failure("Cannot suspend a already suspended customer");
        CustomerStatus oldStatus = this.status;
        this.status = CustomerStatus.SUSPENDED;
        markUpdated();
        registerEvent(new CustomerStatusChangedEvent(oldStatus, this.status, this.getId()));
        return Result.success();
    }

    public Result<Void> activate() {
        if (this.status == CustomerStatus.CLOSED)
            return Result.failure("Cannot activate a closed customer");
        if (this.status == CustomerStatus.ACTIVE)
            return Result.failure("Cannot activate an already active customer");
        CustomerStatus oldStatus = this.status;
        this.status = CustomerStatus.ACTIVE;
        markUpdated();
        registerEvent(new CustomerStatusChangedEvent(oldStatus, this.status, this.getId()));
        return Result.success();
    }

    public Result<Void> close() {
        if (this.status == CustomerStatus.CLOSED)
            return Result.failure("Cannot close an already closed customer");
        CustomerStatus oldStatus = this.status;
        this.status = CustomerStatus.CLOSED;
        markUpdated();
        registerEvent(new CustomerStatusChangedEvent(oldStatus, this.status, this.getId()));
        return Result.success();
    }

    private Result<Void> validateActive() {
        if (this.status != CustomerStatus.ACTIVE)
            return Result.failure("Cannot activate a customer that has been closed");
        return Result.success();
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
