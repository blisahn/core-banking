package com.devblo.infrastructure.persistence.customer;

import com.devblo.customer.CustomerId;
import com.devblo.customer.CustomerStatus;
import com.devblo.customer.repository.CustomerSummary;
import com.devblo.customer.repository.ICustomerReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaCustomerReadRepository implements ICustomerReadRepository {

    private final CustomerJpaRepository jpaRepo;
    private final CustomerEntityMapper mapper;

    @Override
    public Optional<CustomerSummary> findSummaryById(CustomerId id) {
        return jpaRepo.findById(id.value()).map(mapper::toSummary);
    }

    @Override
    public List<CustomerSummary> findAll() {
        return jpaRepo.findAll().stream()
                .map(mapper::toSummary)
                .toList();
    }

    @Override
    public List<CustomerSummary> findActiveCustomers() {
        return jpaRepo.findByStatus(CustomerStatus.ACTIVE).stream()
                .map(mapper::toSummary)
                .toList();
    }

    @Override
    public long countByCustomerId(CustomerId customerId) {
        return jpaRepo.existsById(customerId.value()) ? 1L : 0L;
    }
}
