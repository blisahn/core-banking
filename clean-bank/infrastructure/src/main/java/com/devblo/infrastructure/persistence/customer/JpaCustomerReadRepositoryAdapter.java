package com.devblo.infrastructure.persistence.customer;

import com.devblo.common.PagedResult;
import com.devblo.customer.CustomerId;
import com.devblo.customer.CustomerStatus;
import com.devblo.customer.repository.CustomerSummary;
import com.devblo.customer.repository.ICustomerReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaCustomerReadRepositoryAdapter implements ICustomerReadRepository {

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
    public PagedResult<CustomerSummary> findAll(int page, int size) {
        Page<CustomerEntity> result = jpaRepo.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        List<CustomerSummary> content = result.getContent().stream()
                .map(mapper::toSummary)
                .toList();
        return new PagedResult<>(content, result.getNumber(), result.getSize(),
                result.getTotalElements(), result.getTotalPages());
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

    @Override
    public Optional<CustomerSummary> findByEmail(String email) {
        return jpaRepo.findByEmail(email).map(mapper::toSummary);
    }
}
