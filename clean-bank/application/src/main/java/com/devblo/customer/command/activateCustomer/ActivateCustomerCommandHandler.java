package com.devblo.customer.command.activateCustomer;

import com.devblo.common.ICommandHandler;
import com.devblo.common.Result;
import com.devblo.customer.Customer;
import com.devblo.customer.CustomerId;
import com.devblo.customer.repository.ICustomerWriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActivateCustomerCommandHandler implements ICommandHandler<ActivateCustomerCommand, Result<Void>> {

    private final ICustomerWriteRepository customerWriteRepository;

    public ActivateCustomerCommandHandler(ICustomerWriteRepository customerWriteRepository) {
        this.customerWriteRepository = customerWriteRepository;
    }

    @Override
    @Transactional
    public Result<Void> handle(ActivateCustomerCommand command) {
        var optCustomer = customerWriteRepository
                .findById(CustomerId.of(command.customerId()));
        if (optCustomer.isEmpty()) {
            return Result.failure("Customer with id " + command.customerId() + " not found");
        }
        try {
            Customer customer = optCustomer.get();
            customer.activate();
            customerWriteRepository.save(customer);
            return Result.success(null);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }
}
