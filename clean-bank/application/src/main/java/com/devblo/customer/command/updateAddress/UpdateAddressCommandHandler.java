package com.devblo.customer.command.updateAddress;

import com.devblo.common.ICommandHandler;
import com.devblo.common.result.Result;
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
        Result<Customer> customerExistsResult = customerRepository
                .findById(CustomerId.of(command.customerId()))
                .map(Result::success)
                .orElseGet(() -> Result.failure("Customer not found"));
        if (customerExistsResult.isFailure()) {
            return Result.failure(customerExistsResult.getError());
        }
        Customer customer = customerExistsResult.getValue();
        Address address = Address.of(
                command.street(),
                command.district()
        );
        Result<Void> updateAddressResult = customer.updateAddress(address);
        if (updateAddressResult.isFailure()) {
            return Result.failure(updateAddressResult.getError());
        }
        customerRepository.save(customer);
        return Result.success(customer.getAddress());
    }
}
