package com.devblo.infrastructure.persistence.customer;

import com.devblo.customer.CustomerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, UUID> {
    Optional<CustomerEntity> findByEmail(String email);
    List<CustomerEntity> findByStatus(CustomerStatus status);
    List<CustomerEntity> findByStreetAndDistrict(String street, String district);
}
