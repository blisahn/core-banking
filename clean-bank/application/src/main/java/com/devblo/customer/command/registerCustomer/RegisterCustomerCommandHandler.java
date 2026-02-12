package com.devblo.customer.command.registerCustomer;

import com.devblo.common.ICommandHandler;
import com.devblo.common.result.Result;
import com.devblo.customer.Address;
import com.devblo.customer.Customer;
import com.devblo.customer.CustomerId;
import com.devblo.customer.PersonalInfo;
import com.devblo.customer.repository.ICustomerWriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterCustomerCommandHandler implements ICommandHandler<RegisterCustomerCommand, Result<CustomerId>> {

    private final ICustomerWriteRepository customerWriteRepository;

    public RegisterCustomerCommandHandler(ICustomerWriteRepository customerWriteRepository) {
        this.customerWriteRepository = customerWriteRepository;
    }

    @Override
    @Transactional
    public Result<CustomerId> handle(RegisterCustomerCommand command) {
        Result<Customer> customerExistsResult = customerWriteRepository
                .findByEmail(command.email())
                .map(Result::success)
                .orElseGet(() -> Result.failure("Customer not found"));
        if (customerExistsResult.isFailure()) {
            return Result.failure("Customer with email " + command.email() + " already exists");
        }
        var personalInfo = PersonalInfo.of(
                command.firstName(),
                command.lastName(),
                command.email(),
                command.dateOfBirth()
        );
        Address address = Address.of(command.street(), command.district());
        var customer = Customer.register(personalInfo, address);
        customerWriteRepository.save(customer);
        return Result.success(customer.getId());
    }
}

