package com.devblo.customer.command.updatePersonalInfo;

import com.devblo.common.ICommandHandler;
import com.devblo.common.result.Result;
import com.devblo.customer.Customer;
import com.devblo.customer.CustomerId;
import com.devblo.customer.PersonalInfo;
import com.devblo.customer.repository.ICustomerWriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdatePersonalInfoCommandHandler implements ICommandHandler<UpdatePersonalInfoCommand, Result<PersonalInfo>> {
    private final ICustomerWriteRepository customerWriteRepository;

    public UpdatePersonalInfoCommandHandler(ICustomerWriteRepository customerWriteRepository) {
        this.customerWriteRepository = customerWriteRepository;
    }

    @Override
    @Transactional
    public Result<PersonalInfo> handle(UpdatePersonalInfoCommand command) {

        Result<Customer> customerResult = customerWriteRepository
                .findById(CustomerId.of(command.customerId()))
                .map(Result::success)
                .orElseGet(() -> Result.failure("Customer not found"));
        if (customerResult.isFailure())
            return Result.failure(customerResult.getError());
        Customer customer = customerResult.getValue();
        PersonalInfo newPersonalInfo = PersonalInfo.of(
                command.firstName(),
                command.lastName(),
                command.email(),
                command.dateOfBirth()
        );
        Result<Void> updatedResult = customer.updatePersonalInfo(newPersonalInfo);
        if (updatedResult.isFailure())
            return Result.failure(updatedResult.getError());
        customerWriteRepository.save(customer);
        return Result.success(customer.getPersonalInfo());
    }
}
