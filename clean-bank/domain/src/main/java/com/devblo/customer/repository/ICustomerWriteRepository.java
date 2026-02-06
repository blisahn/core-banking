package com.devblo.customer.repository;

import com.devblo.customer.Address;
import com.devblo.customer.Customer;
import com.devblo.customer.CustomerId;

import java.util.List;
import java.util.Optional;

public interface ICustomerWriteRepository {
    Customer save(Customer customer);
    Optional<Customer> findById(CustomerId id);
    List<Customer> findCustomersByAddress(Address addresses);
    void delete(Customer customer);
    boolean existsById(CustomerId id);



}
