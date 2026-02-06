package com.devblo.exception;

import com.devblo.common.exception.DomainException;
import com.devblo.customer.CustomerStatus;

public class CustomerNotActiveException extends DomainException {
    private CustomerStatus customerStatus;

    public CustomerNotActiveException(CustomerStatus customerStatus) {
        super(String.format("Customer is not active. Current status is %s", customerStatus));
        this.customerStatus = customerStatus;
    }

    public CustomerStatus getCustomerStatus() {
        return customerStatus;
    }
}
