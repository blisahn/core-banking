package com.devblo.infrastructure.persistence.customer;

import com.devblo.customer.Address;
import com.devblo.customer.Customer;
import com.devblo.customer.CustomerId;
import com.devblo.customer.PersonalInfo;
import com.devblo.customer.repository.CustomerSummary;
import com.devblo.shared.Email;
import org.springframework.stereotype.Component;

@Component
public class CustomerEntityMapper {

    public CustomerEntity toEntity(Customer customer) {
        CustomerEntity entity = new CustomerEntity();
        entity.setId(customer.getId().value());
        entity.setFirstName(customer.getPersonalInfo().firstName());
        entity.setLastName(customer.getPersonalInfo().lastName());
        entity.setEmail(customer.getPersonalInfo().email().value());
        entity.setDateOfBirth(customer.getPersonalInfo().dateOfBirth());
        entity.setStreet(customer.getAddress().street());
        entity.setDistrict(customer.getAddress().district());
        entity.setStatus(customer.getStatus());
        entity.setCreatedAt(customer.getCreatedAt());
        entity.setUpdatedAt(customer.getUpdatedAt());
        return entity;
    }

    public Customer toDomain(CustomerEntity entity) {
        PersonalInfo personalInfo = PersonalInfo.of(
                entity.getFirstName(),
                entity.getLastName(),
                Email.of(entity.getEmail()),
                entity.getDateOfBirth()
        );
        Address address = Address.of(entity.getStreet(), entity.getDistrict());
        return Customer.reconstitute(
                CustomerId.of(entity.getId()),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                personalInfo,
                entity.getStatus(),
                address
        );
    }

    public CustomerSummary toSummary(CustomerEntity entity) {
        return new CustomerSummary(
                CustomerId.of(entity.getId()),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getStatus()
        );
    }
}
