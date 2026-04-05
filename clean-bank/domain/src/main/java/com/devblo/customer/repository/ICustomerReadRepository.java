package com.devblo.customer.repository;

import com.devblo.common.PagedResult;
import com.devblo.customer.CustomerId;

import java.util.List;
import java.util.Optional;

public interface ICustomerReadRepository {
    Optional<CustomerSummary> findSummaryById(CustomerId id);
    Optional<CustomerSummary> findByEmail(String email);
    List<CustomerSummary> findAll();
    PagedResult<CustomerSummary> findAll(int page, int size);
    List<CustomerSummary> findActiveCustomers();
    long countByCustomerId(CustomerId customerId);
}
