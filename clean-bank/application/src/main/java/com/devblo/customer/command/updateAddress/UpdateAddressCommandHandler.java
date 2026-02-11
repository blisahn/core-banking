package com.devblo.customer.command.updateAddress;

import com.devblo.common.ICommandHandler;
import com.devblo.common.Result;
import com.devblo.customer.Address;
import com.devblo.customer.Customer;
import com.devblo.customer.CustomerId;
import com.devblo.customer.repository.ICustomerWriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateAddressCommandHandler implements ICommandHandler<UpdateAddressCommand, Result<Address>> {

    private final ICustomerWriteRepository customerRepository;

    public UpdateAddressCommandHandler(ICustomerWriteRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public Result<Address> handle(UpdateAddressCommand command) {
        var customerExists = customerRepository
                .findById(CustomerId.of(command.customerId()));
        if (customerExists.isEmpty()) {
            return Result.failure("Customer with ID " + command.customerId() + " does not exist");
        } else {
            var address = Address.of(command.street(), command.district());
            try {
                Customer customer = customerExists.get();
                customer.updateAddress(address);
                customerRepository.save(customer);
                return Result.success(customer.getAddress());
            } catch (Exception e) {
                return Result.failure(e.getMessage());
            }
        }

    }
}
