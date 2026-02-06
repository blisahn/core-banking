package com.devblo.customer.repository;

import com.devblo.customer.CustomerId;
import com.devblo.customer.CustomerStatus;

public record CustomerSummary(
        CustomerId id,
        String firstName,
        String lastName,
        CustomerStatus status

) {
}
