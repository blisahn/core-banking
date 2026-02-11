package com.devblo.customer.command.updatePersonalInfo;

import com.devblo.common.ICommandHandler;
import com.devblo.common.Result;
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
        var customerExists = customerWriteRepository
                .findById(CustomerId.of(command.customerId()));
        if (customerExists.isEmpty()) {
            return Result.failure("Customer with id " + command.customerId() + " does not exist");
        } else {
            try {
                var newPersonalInfo = PersonalInfo.of(command.firstName(), command.lastName(), command.email(), command.dateOfBirth());
                var customer = customerExists.get();
                customer.updatePersonalInfo(newPersonalInfo);
                customerWriteRepository.save(customer);
                return Result.success(customer.getPersonalInfo());
            } catch (IllegalArgumentException e) {
                return Result.failure(e.getMessage());
            }
        }
    }
}
