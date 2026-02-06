package com.devblo.customer.repository;

import com.devblo.customer.CustomerId;

import java.util.List;
import java.util.Optional;

public interface ICustomerReadRepository {
    Optional<CustomerSummary> findSummaryById(CustomerId id);

    List<CustomerSummary> findAll();

    List<CustomerSummary> findActiveCustomers();
    long countByCustomerId(CustomerId customerId);
}
