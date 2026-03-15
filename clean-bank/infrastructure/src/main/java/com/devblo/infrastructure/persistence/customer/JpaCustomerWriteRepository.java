package com.devblo.infrastructure.persistence.customer;

import com.devblo.customer.Address;
import com.devblo.customer.Customer;
import com.devblo.customer.CustomerId;
import com.devblo.customer.repository.ICustomerWriteRepository;
import com.devblo.infrastructure.events.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaCustomerWriteRepository implements ICustomerWriteRepository {

    private final CustomerJpaRepository jpaRepo;
    private final CustomerEntityMapper mapper;
    private final DomainEventPublisher eventPublisher;

    @Override
    public Customer save(Customer customer) {
        CustomerEntity entity = mapper.toEntity(customer);
        CustomerEntity saved = jpaRepo.save(entity);
        eventPublisher.publishAll(customer);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Customer> findById(CustomerId id) {
        return jpaRepo.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return jpaRepo.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public List<Customer> findCustomersByAddress(Address address) {
        return jpaRepo.findByStreetAndDistrict(address.street(), address.district())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void delete(Customer customer) {
        jpaRepo.deleteById(customer.getId().value());
    }

    @Override
    public boolean existsById(CustomerId id) {
        return jpaRepo.existsById(id.value());
    }
}
